$(document).ready(function () {
    // Hide the modal when the document is ready
    $('#addTaskModal').modal('hide');

    // Show the modal when the "Add tasks" button is clicked
    $('.add-task-btn').click(function () {
        $('#addTaskModal').modal('show');
    });


});
