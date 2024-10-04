
    // Function to open modal and populate data
    document.addEventListener('DOMContentLoaded', function () {
        const taskElements = document.querySelectorAll('.task');

        taskElements.forEach(task => {
            task.addEventListener('click', function () {
                const taskId = this.getAttribute('data-task-id');
                const taskTitle = this.querySelector('p').innerText;
                const taskPriority = this.querySelector('.tag').innerText.toUpperCase();
                const taskDeadline = this.querySelector('.date').innerText;
                const taskDescription = "";
                const taskStatus = "";

                // Populate the modal fields
                document.getElementById('editTaskId').value = taskId;
                document.getElementById('editTaskTitle').value = taskTitle;
                document.getElementById('editTaskDescription').value = taskDescription;
                document.getElementById('editTaskPriority').value = taskPriority;
                document.getElementById('editTaskStatus').value = taskStatus;
                document.getElementById('editTaskDeadline').value = taskDeadline;
            });
        });
    });

    document.getElementById('editTaskForm').addEventListener('submit', function (event) {
        event.preventDefault();
        const taskId = document.getElementById('editTaskId').value;
        const taskTitle = document.getElementById('editTaskTitle').value;
        const taskDescription = document.getElementById('editTaskDescription').value;
        const taskPriority = document.getElementById('editTaskPriority').value;
        const taskStatus = document.getElementById('editTaskStatus').value;
        const taskDeadline = document.getElementById('editTaskDeadline').value;


        const params = new URLSearchParams({
                taskId: taskId,
                taskTitle: taskTitle,
                taskDescription : taskDescription,
                taskPriority : taskPriority,
                taskStatus : taskStatus,
                taskDeadline : taskDeadline
            });
        fetch(`/tasks/update?${params.toString()}`, {
            method: 'Post',
            headers: {
                'Content-Type': 'text/html'
            }
        })
        .then(response => {
            if (response.ok) {
                return response.text();
            }
            throw new Error('Network response was not ok.');
        })
        .then(html => {
            document.getElementById('boardList').innerHTML = html;
        })
        .catch(error => {
            console.error('There was a problem with the fetch operation:', error);
        });

        $('#editTaskModal').modal('hide');
    });

