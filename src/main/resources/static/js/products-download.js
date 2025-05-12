document.addEventListener('DOMContentLoaded', () => {
    const download_button = document.getElementById('download-template');

    download_button.addEventListener('click', function () {
        console.log('Download button clicked');
        window.location.href = '/payment';
    });
});