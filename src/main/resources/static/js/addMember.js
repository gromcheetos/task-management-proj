(function waitForJQuery() {
    if (typeof jQuery !== 'undefined') {

        $(document).ready(function() {
            const avatars = document.querySelectorAll('.avatar');

            avatars.forEach(avatar => {
                const username = avatar.dataset.username;
                if (username) {
                   const initials = username
                        .split(' ')
                        .map(word => word.charAt(0))
                        .join('')
                        .toUpperCase()
                        .substring(0, 2);

                    avatar.textContent = initials;
                }
            });

            $("#searchBox").on("input", function () {
                const query = $(this).val();

                if (query.length >= 2) {
                    $.ajax({
                        url: "/search/users",
                        method: "GET",
                        data: { keyword: query },
                        success: function (data) {
                            let html = "";
                            data.forEach(item => {
                                html += `<div class="suggestion-item" data-user-id="${item.userId}">${item.username}</div>`;
                            });
                            $("#suggestions").html(html).show();
                        }
                    });
                } else {
                    $("#suggestions").hide();
                }
                $(document).on("click", ".suggestion-item", function () {
                    $("#searchBox").val($(this).text());
                    $("#searchBox").attr('data-user-id', $(this).data('user-id'));
                    $("#suggestions").hide();
                });
            });
            document.getElementById('invite').addEventListener('click',
                function (event) {
                    event.preventDefault();
                    const userId = $("#searchBox").attr('data-user-id');
                    const projectId = document.getElementById('projectId').value;
                    const userName = $("#searchBox").val();
                    const userRole = $("#roleSelect").val();
                    const params = new URLSearchParams({
                        userId: userId,
                        userRole: userRole,
                        projectId: projectId
                    });
                    fetch(`/project/add/member?${params.toString()}`,
                        {
                            method: 'POST',
                            headers: {
                                'Content-Type': 'application/json',
                            }
                        })
                        .then(teamProject => {
                            if (!teamProject.ok) {
                                throw new Error("Failed to add member");
                            }
                            return teamProject.json();
                        })
                        .then(data => {
                         //   alert('Sent invitation to ' + userName + ' ');

                            const userName = $("#searchBox").val();
                            const userRole = $("#roleSelect").val();

                            const userInitials = userName
                            .split(' ')
                            .map(word => word.charAt(0))
                            .join('')
                            .toUpperCase()
                            .substring(0, 2);

                            alert('Invited successfully');
                            $('#userList').append('<div class="member">' +
                                '<span class="avatar" data-username=' + initials + '></span>' +
                                '<div class="info">' +
                                '<p class="name" id="username">' + userName + '</p>' +
                                '<p class="role">'+ userRole +'</p>' +
                                '</div>' +
                        '</div>');
                            $('#addMemberModal').modal('hide');
                            $('#searchBox')[0].reset();

                        })
                        .catch((error) => {
                            console.error('Error updating members:', error);
                        });
            });
        });

        } else {
            setTimeout(waitForJQuery, 100);
        }
    })();
