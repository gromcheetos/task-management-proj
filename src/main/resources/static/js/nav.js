document.addEventListener('DOMContentLoaded', function() {
    const profileImage = document.getElementById('profileImage');
    const slideMenu = document.getElementById('slideMenu');
    const closeButton = document.getElementById('closeBtn');

    if (profileImage && slideMenu) {
        profileImage.addEventListener('click', function() {
            console.log("Profile image clicked!");
            slideMenu.classList.add('active');
        });
    } else {
        console.error("Profile image or slide menu not found.");
    }

    if (closeButton && slideMenu) {
        closeButton.addEventListener('click', function() {
            console.log("Close button clicked!");
            slideMenu.classList.remove('active');
        });
    } else {
        console.error("Close button or slide menu not found.");
    }
});
