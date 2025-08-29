$(function(){
    var userId = $("#userId").val();

    $("#changeUserInfo").on("click", function(){
        var username = $("#inputUsername").val();
        var userFullName = $("#inputFirstName").val() + " " + $("#inputLastName").val();
        var userEmail = $("#inputEmailAddress").val();
        var password = $("#password").val();
        var projectId = $("#projectId").val();

        const params = new URLSearchParams({
            userId: userId,
            username: username,
            name: userFullName,
            userEmail: userEmail,
            password: password,
            projectId: projectId
        });
        fetch(`/users/update?${params.toString()}`,
        {
            method: 'POST',

        }).then(res => {
                if (res.ok) {
                    alert("Your information changed successfully!");
                    location.reload();
                }
        }).catch((error) => {
            console.error('Error updating user:', error);
        });

    })
})