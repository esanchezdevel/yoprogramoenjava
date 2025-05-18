document.addEventListener('DOMContentLoaded', () => {
    const download_button = document.getElementById('download-template');
    const product_id = download_button.getAttribute('product-id');

    download_button.addEventListener('click', function () {
        window.location.href = '/payment/' + product_id;
    });
});