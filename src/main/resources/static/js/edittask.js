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
    document.getElementById('editTaskDescription').value = task.description || '';
    document.getElementById('editTaskPriority').value = task.priority || '';
    document.getElementById('editTaskStatus').value = task.status || '';
    document.getElementById('editTaskDeadline').value = task.deadline || '';

    // Open the modal (if using a modal library like Bootstrap)
    $('#editTaskModal').modal('show');
  })
  .catch((error) => {
    console.error('Error fetching task details:', error);
  });
}
