document.addEventListener('DOMContentLoaded', function() {
    const profileImage = document.getElementById('profileImage');
    const slideMenu = document.getElementById('slideMenu');
    const closeButton = document.getElementById('closeBtn');

    if (profileImage && slideMenu) {
        profileImage.addEventListener('click', function() {
            console.log("Profile image clicked!"); // Debug line
            slideMenu.classList.add('active');  // Show the slide menu
            console.log("Slide menu should now be active."); // Debug line
        });
    }

    if (closeButton && slideMenu) {
        closeButton.addEventListener('click', function() {
            console.log("Close button clicked!"); // Debug line
            slideMenu.classList.remove('active');  // Hide the slide menu
            console.log("Slide menu should now be inactive."); // Debug line
        });
    }
});
