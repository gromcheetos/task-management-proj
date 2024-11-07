function showProfileImageUpload(){
    const profileImage = document.getElementById('profileImage');
    const slideMenu = document.getElementById('slider-menu');
    const closeButton = document.getElementById('closeBtn');

    if (profileImage && slideMenu) {
        slideMenu.classList.add('active');
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
}


// Get references to elements
const menuBtn = document.getElementById('showSlideBtn');
const slideMenu = document.getElementById('slideMenu');
const closeBtn = document.getElementById('close');

// Open the slide-in menu
menuBtn.addEventListener('click', () => {
    slideMenu.classList.add('active');
});

// Close the slide-in menu
closeBtn.addEventListener('click', () => {
    slideMenu.classList.remove('active');
});

// Optionally, close menu when clicking outside (if desired)
document.addEventListener('click', (e) => {
    if (!slideMenu.contains(e.target) && !menuBtn.contains(e.target)) {
        slideMenu.classList.remove('active');
    }
});




