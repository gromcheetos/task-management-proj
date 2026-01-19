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
                    window.location.href = '/';
                }
        }).catch((error) => {
            console.error('Error updating user:', error);
        });
    })

    $("#uploadImageBtn").on("click", function () {
        $("#profileImageInput").click();
    });

    $("#profileImageInput").on("change", function () {
        const file = this.files[0];
        if (!file) return;

        if (file.size > 5 * 1024 * 1024) {
            alert("File must be smaller than 5 MB");
            return;
        }

        const reader = new FileReader();
        reader.onload = function (e) {
            $("#profileImageCard").attr("src", e.target.result).show();
            $("#profileImageNav").attr("src", e.target.result);
            $("#userAvatar").hide();
        };
        reader.readAsDataURL(file);

        const formData = new FormData();
        formData.append("userId", $("#userId").val());
        formData.append("file", file);

        fetch("/users/uploadImg", { method: "POST", body: formData })
            .then(async res => {
                if (res.ok) {
                    alert("Profile photo updated!");
                    return;
                }
                const text = await res.text();
                console.error("Upload failed:", res.status, text);
                alert("Upload failed: " + res.status);
            })
            .catch(err => console.error("Upload error:", err));
    });
})