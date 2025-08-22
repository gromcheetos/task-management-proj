package org.app.controllers.projects;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.app.controllers.util.ProjectCommon;
import org.app.exceptions.JobPositionNotFoundException;
import org.app.exceptions.ProjectNotFoundException;
import org.app.exceptions.UserNotFoundException;
import org.app.model.JobPosition;
import org.app.model.Project;
import org.app.model.User;
import org.app.model.dto.DuplicateCheckDto;
import org.app.model.dto.UserDto;
import org.app.service.BoardService;
import org.app.service.JobPositionService;
import org.app.service.ProjectService;
import org.app.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@Slf4j
@AllArgsConstructor
@RequestMapping("/project")
public class ProjectController {

    private final ProjectService projectService;
    private final UserService userService;
    private final BoardService boardService;
    private final ProjectCommon projectCommon;
    private final JobPositionService jobPositionService;

    @PostMapping("/create")
    public String createProject(@RequestParam("projectName") String projectName,
        @RequestParam(value = "description", required = false) String description,
        HttpSession session) throws UserNotFoundException {
        User currentUser = userService.getCurrentUser();
        Project project = new Project(projectName);
        if(description != null){
            project.setDescription(description);
        }
        project.setProjectOwner(currentUser);
        log.info("Project owner: {}", currentUser.getName());
        project = projectService.createOrUpdateProject(project);
        log.info("Project created successfully");

        userService.updateUserProject(currentUser.getId(), project);
        log.info("User has new project assigned");
        session.setAttribute("currentProject", project);
        return "redirect:/";
    }

    @PostMapping("/add/member")
    public ResponseEntity<Map<String, Object>> insertTeamMember(@RequestParam("projectId") int projectId,
        @RequestParam(value = "userId") int userId,
        @RequestParam(value = "userRole") String userRole,
        @RequestParam(value = "jobId") Integer jobId)
        throws UserNotFoundException, ProjectNotFoundException, JobPositionNotFoundException {
        log.info("[insertTeamMember] - Request received to add member to the project");
        Project teamProject = projectService.findProjectByProjectId(projectId);
        User user = userService.getUserById(userId);
        Set<User> currentMembers = teamProject.getTeamMembers();
        currentMembers.add(user);
        teamProject.setTeamMembers(currentMembers);
        userService.joinProject(userId, projectId, userRole);
        log.info("Current members: {}", currentMembers);

        if(jobId != null){
            jobPositionService.updateJobPosition(jobId, user);
            userService.updateUserPosition(userId, jobId);
        }

        projectService.createOrUpdateProject(teamProject);
        log.info("[insertTeamMember] - Added member to the project");

        return ResponseEntity.ok(Collections.singletonMap("projectId", teamProject.getProjectId()));
    }

    @GetMapping("/show")
    public String showProjectPage(Model model) throws UserNotFoundException {
        User currentUser = userService.getCurrentUser();
        if(!currentUser.getProjects().isEmpty() || !currentUser.getOwnedProjects().isEmpty()){
            log.info("The user has projects. Redirecting to /project/select");
            showProjectList(model);
        }
        model.addAttribute("currentUser", currentUser);
        return "create-project";
    }

    @GetMapping("/select")
    public String showProjectList(Model model) throws UserNotFoundException {
        User currentUser = userService.getCurrentUser();
        List<Project> userProjects = projectService.findProjectsByUserId(currentUser.getId());
        log.info("userProjects: " + userProjects);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("userProjects", userProjects);
        return "select-projectList";
    }

    @GetMapping("/find/{projectId}")
    public String getProjectById(@PathVariable("projectId") int id, Model model)
        throws UserNotFoundException, ProjectNotFoundException {
        Project currentProject = projectService.findProjectByProjectId(id);
        return projectCommon.getHomePageUtility(model, currentProject);
    }

    @PostMapping("/add/position")
    public String insertJobPosition(@RequestParam("projectId") int projectId,
        @RequestParam("positions") String positions) throws ProjectNotFoundException {
        log.info("[Add Job position] - Request received to add position to the project");
        Project project = projectService.findProjectByProjectId(projectId);
        List<String> positionList = Arrays.asList(positions.split(","));
        List<JobPosition> addPositions = positionList.stream()
            .map(JobPosition::new)
            .toList();
        for (JobPosition jp : addPositions) {
            jp.setProject(project);
            jobPositionService.addJobPosition(jp, projectId);
        }
        project.setJobPositions(addPositions);
        log.info("[Add Job position] - added positions to the project");
        return "redirect:/";
    }

    @GetMapping("/show/members")
    @ResponseBody
    public Set<UserDto> getTeamMembers(@RequestParam int projectId) throws ProjectNotFoundException {
        Project project = projectService.findProjectByProjectId(projectId);
        return project.getTeamMembers().stream()
                .map(user -> new UserDto(
                        user.getId(),
                        user.getUsername(),
                        user.getName(),
                        user.getRoles().name(),
                        user.getJobPosition() != null ? user.getJobPosition().getTitle() : null
                ))
                .collect(Collectors.toSet());
    }

    @PostMapping("/check/duplicate/positions")
    @ResponseBody
    public ResponseEntity<?> checkDuplicatePositions(@RequestBody DuplicateCheckDto dto) {
        List<String> duplicates = jobPositionService.findExistingTitles(dto.getProjectId(), dto.getPositions());
        if (!duplicates.isEmpty()) {
            String msg = "Already exist: " + String.join(", ", duplicates);
            return ResponseEntity.badRequest().body(msg);
        }
        return ResponseEntity.ok("No duplicates");
    }
}
