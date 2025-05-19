document.addEventListener('DOMContentLoaded', () => {
    const modal = document.getElementById('video-modal');
    const modalVideo = document.getElementById('modal-video');
    const videoSource = document.getElementById('modal-video-source');
    const closeButton = document.querySelector('.close-button');

    // button that opens the preview popup
    document.querySelectorAll('[data-video-src]').forEach(button => {
        button.addEventListener('click', () => {
            const src = button.getAttribute('data-video-src');
            videoSource.src = src;
            modalVideo.load();
            modal.style.display = 'flex';
            modalVideo.play();
        });
    });

    // Close button
    closeButton.addEventListener('click', () => {
        modal.style.display = 'none';
        modalVideo.pause();
        modalVideo.currentTime = 0;
    });

    // Close when click out of the popup
    window.addEventListener('click', event => {
        if (event.target === modal) {
            modal.style.display = 'none';
            modalVideo.pause();
            modalVideo.currentTime = 0;
        }
    });
});
