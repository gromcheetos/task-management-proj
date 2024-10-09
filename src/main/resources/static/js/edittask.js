//
//    document.addEventListener('DOMContentLoaded', function () {
//        const taskElements = document.querySelectorAll('.task');
//
//        taskElements.forEach(task => {
//            task.addEventListener('click', function () {
//                const taskId = this.getAttribute('data-task-id');
//                const taskTitle = this.querySelector('p').innerText;
//                const taskPriority = this.querySelector('.tag').innerText.toUpperCase();
//                const taskDeadline = this.querySelector('.date').innerText;
//                const taskDescription = "";
//                const taskStatus = "";
//
//                // Populate the modal fields
//                document.getElementById('editTaskId').value = taskId;
//                document.getElementById('editTaskTitle').value = taskTitle;
//                document.getElementById('editTaskDescription').value = taskDescription;
//                document.getElementById('editTaskPriority').value = taskPriority;
//                document.getElementById('editTaskStatus').value = taskStatus;
//                document.getElementById('editTaskDeadline').value = taskDeadline;
//            });
//        });
//    });
//
//    document.getElementById('editTaskForm').addEventListener('submit', function (event) {
//        event.preventDefault();
//        const taskId = document.getElementById('editTaskId').value;
//        const taskTitle = document.getElementById('editTaskTitle').value;
//        const taskDescription = document.getElementById('editTaskDescription').value;
//        const taskPriority = document.getElementById('editTaskPriority').value;
//        const taskStatus = document.getElementById('editTaskStatus').value;
//        const taskDeadline = document.getElementById('editTaskDeadline').value;
//
//
//        const params = new URLSearchParams({
//                taskId: taskId,
//                taskTitle: taskTitle,
//                taskDescription : taskDescription,
//                taskPriority : taskPriority,
//                taskStatus : taskStatus,
//                taskDeadline : taskDeadline
//            });
//        fetch(`/tasks/update?${params.toString()}`, {
//            method: 'POST',
//            headers: {
//                'Content-Type': 'text/html'
//            }
//        })
//        .then(response => {
//            if (response.ok) {
//                return response.text();
//            }
//            throw new Error('Network response was not ok.');
//        })
//        .then(html => {
//            document.getElementById('boardList').innerHTML = html;
//        })
//        .catch(error => {
//            console.error('There was a problem with the fetch operation:', error);
//        });
//
//        $('#editTaskModal').modal('hide');
//    });
//

function openEditTaskModal(taskElement) {
    var taskId = taskElement.getAttribute('data-task-id');
 if (!taskId || isNaN(taskId)) {
        console.error("Invalid taskId:", taskId);
        return;
    }

    // Make an AJAX request to fetch task details
    fetch(`/tasks/detail/{taskId}`)
        .then(response => response.json())
        .then(task => {
            // Populate the modal form with the task details
            document.getElementById('editTaskId').value = task.id;
            document.getElementById('editTaskTitle').value = task.title;
            document.getElementById('editTaskDescription').value = task.description;
            document.getElementById('editTaskPriority').value = task.priority;
            document.getElementById('editTaskStatus').value = task.status;
            document.getElementById('editTaskDeadline').value = task.deadline;
        })
        .catch(error => console.error('Error fetching task details:', error));
}
