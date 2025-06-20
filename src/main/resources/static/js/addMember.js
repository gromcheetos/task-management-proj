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
                                html += `<div class="suggestion-item">${item}</div>`;
                            });
                            $("#suggestions").html(html).show();
                        }
                    });
                } else {
                    $("#suggestions").hide();
                }
                $(document).on("click", ".suggestion-item", function () {
                    $("#searchBox").val($(this).text());
                    $("#suggestions").hide();
                });
            });
            document.getElementById('invite').addEventListener('click',
                function (event) {
                    event.preventDefault();
                    const username = document.getElementById('searchBox').value;
                    const params = new URLSearchParams({
                        teamMembers: username
                    });
                    fetch(`/add/member?${params.toString()}`,
                        {
                            method: 'POST',
                            headers: {
                                'Content-Type': 'application/json',
                            }
                        }
                    )
            });
        });

        } else {
            setTimeout(waitForJQuery, 100);
        }
    })();


