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

