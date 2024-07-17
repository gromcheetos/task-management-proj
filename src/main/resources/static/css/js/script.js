document.addEventListener('DOMContentLoaded', (event) => {
    const addTaskButton = document.getElementById('add-task-btn');
    const addTaskModal = document.getElementsByClassName('addTaskModal');

    $(document).ready(function () {
        $('#addTaskModal').modal('hide');

        $('.add-task-btn').click(function () {
            $('#addTaskModal').modal('show');
        });
    });
});

        deleteButtons.forEach(button => {
        button.addEventListener('click', function () {
            const boardId = this.getAttribute('data-board-id');
            if (confirm("Are you sure you want to delete this board? All the tasks in the board will be deleted")) {

                })
            }
        });
    });