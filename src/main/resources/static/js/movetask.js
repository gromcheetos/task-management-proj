
function allowDrop(event) {
    event.preventDefault();
}

function drag(event) {
    event.dataTransfer.setData("text", event.target.getAttribute('data-task-id'));
}

function drop(event) {
    event.preventDefault();
    const taskId = event.dataTransfer.getData("text");
    const taskElement = document.querySelector(`.task[data-task-id='${taskId}']`);
    const newBoardElement = event.target.closest('.column');

    if (!newBoardElement) {
        return;
    }

    const newBoardId = newBoardElement.getAttribute('data-board-id');
    newBoardElement.querySelector('.task-list').appendChild(taskElement);

    const params = new URLSearchParams({
        taskId: taskId,
        newBoardId: newBoardId
    });

    fetch(`/tasks/move?${params.toString()}`, {
        method: 'POST'

    })
    .then(response => {
        if (!response.ok) {
            throw new Error("Failed to move task");
        }
        return response.json();
    })
    .then(data => {
        console.log('Task moved successfully:', data);
    })
    .catch((error) => {
        console.error('Error:', error);
    });
}
