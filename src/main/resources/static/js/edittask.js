function openEditTaskModal(taskElement) {
  // Get the task ID from the task element's data attribute
  var taskId = taskElement.getAttribute('data-task-id');

  // Validate the task ID
  if (!taskId || isNaN(taskId)) {
    console.error("Invalid taskId:", taskId);
    return;
  }

  // Make an AJAX request to fetch task details
  fetch(`/tasks/detail/${taskId}`, {
    method: 'GET',  // Use GET method since it's retrieving data
    headers: {
      'Content-Type': 'application/json',
    }
  })
  .then(response => {
    if (!response.ok) {
      throw new Error("Failed to fetch task details");
    }
    return response.json();
  })
  .then(task => {
    console.log('Task received:', task);
    // Check if task data is valid
    if (!task || typeof task !== 'object') {
      throw new Error("Invalid task data received");
    }

    // Populate the modal form with the task details
    document.getElementById('editTaskId').value = task.id || '';
    document.getElementById('editTaskTitle').value = task.title || '';
    document.getElementById('editTaskDescription').value = task.description
        || '';
    document.getElementById('editTaskPriority').value = task.priority || '';
    document.getElementById('editTaskStatus').value = task.status || '';
    document.getElementById('editTaskDeadline').value = task.deadline || '';

    // Open the modal (if using a modal library like Bootstrap)
    $('#editTaskModal').modal('show');
  })
  .catch((error) => {
    console.error('Error fetching task details:', error);
  });

  // Function to handle the "Save Changes" button click
  document.getElementById('saveChangesButton').addEventListener('click',
      function (event) {
        event.preventDefault();

        var taskId = document.getElementById('editTaskId').value;
        var title = document.getElementById('editTaskTitle').value;
        var description = document.getElementById('editTaskDescription').value;
        var priority = document.getElementById('editTaskPriority').value;
        var deadline = document.getElementById('editTaskDeadline').value;


        const params = new URLSearchParams({
          taskId: taskId,
          title: title,
          description: description,
          priority: priority,
          deadline: deadline
        });

        fetch(`/tasks/update?${params.toString()}`,
            {
              method: 'POST',
              headers: {
                'Content-Type': 'application/json',
              }
            })
        .then(response => {
          if (!response.ok) {
            throw new Error("Failed to update task");
          }
          return response.text();
        })
        .then(() => {
          $('#editTaskModal').modal('hide');
          location.reload();
        })
        .catch((error) => {
          console.error('Error updating task:', error);
        });
      });
}
