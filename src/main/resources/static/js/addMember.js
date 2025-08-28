(function waitForJQuery() {
    if (typeof jQuery !== 'undefined') {

        $(document).ready(function() {
            //const avatars = document.querySelectorAll('.avatar');
            const projectId = document.getElementById('projectId').value;
            fetch(`/project/show/members?projectId=${projectId}`)
                .then(res => res.json())
                .then(members => renderMembers(members));
            /*avatars.forEach(avatar => {
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
            });*/

            $("#searchBox").on("input", function () {
                const query = $(this).val();

                if (query.length >= 2) {
                    $.ajax({
                        url: "/search/users",
                        method: "GET",
                        data: {keyword: query},
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
                    const userRole = $("#roleSelect").val();
                    const jobId = $("#positionSelect").val();
                    const params = new URLSearchParams({
                        userId: userId,
                        userRole: userRole,
                        projectId: projectId,
                        jobId: jobId
                    });
                    fetch(`/project/add/member?${params.toString()}`,
                        {
                            method: 'POST',
                            headers: {
                                'Content-Type': 'application/json',
                            }
                        })
                        .then(projectId => {
                            if (!projectId.ok) {
                                throw new Error("Failed to add member");
                            }
                            return projectId.json();
                        })
                        .then(() => {
                            return fetch(`/project/show/members?projectId=${projectId}`);
                        })
                        .then(response => {
                            if (!response.ok) {
                                throw new Error("Failed to fetch updated member list");
                            }
                            return response.json();
                        })
                        .then(members => renderMembers(members))
                        .catch((error) => {
                            console.error('Error updating members:', error);
                        });

                });
            });
        } else {
            setTimeout(waitForJQuery, 100);
        }
            function renderMembers(memberDtos) {
                $('#userList').empty();

                memberDtos.forEach(user => {
                    const userInitials = user.name
                        .split(' ')
                        .map(word => word.charAt(0))
                        .join('')
                        .toUpperCase()
                        .substring(0, 2);

                    $('#userList').append(`
                                    <div class="member">
                                        <span class="avatar">${userInitials}</span>
                                        <div class="info">
                                            <div class="top-row">
                                                <div class="left">
                                                    <p class="name" id="username">${user.username}</p>
                                                    <p class="role">${user.roles}</p>                                           
                                                </div>
                                                <span class="position">${user.jobTitle ?? ''}</span>
                                            </div>
                                        </div>
                                    </div>
                                `);
                });

                $('#topAvatars').empty();

                memberDtos.forEach(user => {
                    const userInitials = user.name
                        .split(' ')
                        .map(word => word.charAt(0))
                        .join('')
                        .toUpperCase()
                        .substring(0, 2);

                    $('#topAvatars').append(`
                                    <div class="member-avatar">
                                        <span class="avatar">${userInitials}</span>
                                    </div>
                                `);
                });
                $('#addMemberModal').modal('hide');
                $('#searchBox').val('');

            }
    })();
