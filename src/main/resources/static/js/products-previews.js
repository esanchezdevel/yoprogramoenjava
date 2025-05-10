document.addEventListener('DOMContentLoaded', () => {
    const modal = document.getElementById('video-modal');
    const modalVideo = document.getElementById('modal-video');
    const videoSource = document.getElementById('modal-video-source');
    const closeButton = document.querySelector('.close-button');

    // Botón que abre el modal
    document.querySelectorAll('[data-video-src]').forEach(button => {
        button.addEventListener('click', () => {
            const src = button.getAttribute('data-video-src');
            videoSource.src = src;
            modalVideo.load();
            modal.style.display = 'block';
            modalVideo.play();
        });
    });

    // Botón de cerrar
    closeButton.addEventListener('click', () => {
        modal.style.display = 'none';
        modalVideo.pause();
        modalVideo.currentTime = 0;
    });

    // Cerrar al hacer clic fuera del modal
    window.addEventListener('click', event => {
        if (event.target === modal) {
            modal.style.display = 'none';
            modalVideo.pause();
            modalVideo.currentTime = 0;
        }
    });
});
