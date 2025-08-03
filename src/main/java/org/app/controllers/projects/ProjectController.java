package org.app.controllers.projects;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.app.controllers.util.ProjectCommon;
import org.app.exceptions.JobPositionNotFoundException;
import org.app.exceptions.ProjectNotFoundException;
import org.app.exceptions.UserNotFoundException;
import org.app.model.Board;
import org.app.model.JobPosition;
import org.app.model.Project;
import org.app.model.User;
import org.app.model.enums.Roles;
import org.app.model.enums.Status;
import org.app.service.BoardService;
import org.app.service.JobPositionService;
import org.app.service.ProjectService;
import org.app.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

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

    // TODO: add board to the project as well changed
    @PostMapping("/create")
    public String createProject(@RequestParam("projectName") String projectName,
        @RequestParam(value = "description", required = false) String description,
        Model model) throws UserNotFoundException {
        User currentUser = userService.getCurrentUser();
        Project project = new Project(projectName);
        project.setProjectOwner(currentUser);
        project.setTeamMembers(Set.of(currentUser));
        currentUser.setRoles(Roles.ADMIN);
        for (Status status : Status.values()) {
            Board board = boardService.createDefaultBoardsForNewProject(status.getValue());
            project.getBoards().add(board);
        }
        project.setDescription(description);
        projectService.createProject(project);
        model.addAttribute("project", project);
        return "redirect:/";
    }

    @PostMapping("/add/member")
    public ResponseEntity<Project> insertTeamMember(@RequestParam("projectId") int projectId,
        @RequestParam(value = "userId") int userId,
        @RequestParam(value = "userRole") String userRole,
        @RequestParam(value = "jobId") int jobId
    ) throws UserNotFoundException, ProjectNotFoundException, JobPositionNotFoundException {
        Project teamProject = projectService.findProjectByProjectId(projectId);
        User user = userService.getUserById(userId);
        Set<User> currentMembers = teamProject.getTeamMembers();
        jobPositionService.updateJobPosition(jobId, user);
        userService.updatUserRole(userId, userRole, jobId);
        currentMembers.add(user);
        teamProject.setTeamMembers(currentMembers);
     //   userService.joinProject(userId, projectId);
        return ResponseEntity.ok(teamProject);
    }

    @GetMapping("/show")
    public String showProjectPage(Model model) throws UserNotFoundException {
        User currentUser = userService.getCurrentUser();
        model.addAttribute("currentUser", currentUser);
        return "create-project";
    }

    @GetMapping("/find/{projectId}")
    public String getProjectById(@PathVariable("projectId") int id, Model model)
        throws UserNotFoundException, ProjectNotFoundException {
        Project activeProject = projectService.findProjectByProjectId(id);
        return projectCommon.getHomePageUtility(model, activeProject);
    }

    @PostMapping("/add/position")
    public String insertJobPosition(@RequestParam("projectId") int projectId,
        @RequestParam("positions") String positions) throws ProjectNotFoundException {
        Project project = projectService.findProjectByProjectId(projectId);
        List<String> positionList = Arrays.asList(positions.split(","));
        List<JobPosition> addPositions = positionList.stream()
            .map(JobPosition::new)
            .toList();
        for (JobPosition jp : addPositions) {
            jp.setProject(project);
            jobPositionService.createJobPosition(jp);
        }
        project.setJobPositions(addPositions);
        return "redirect:/";
    }

    @GetMapping("/show/members")
    public ResponseEntity<List<User>> getTeamMembers(@RequestParam int projectId) throws ProjectNotFoundException {
        Project project = projectService.findProjectByProjectId(projectId);
        Set<User> members = project.getTeamMembers();
        return ResponseEntity.ok(new ArrayList<>(members));
    }

}
