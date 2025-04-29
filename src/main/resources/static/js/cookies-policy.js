document.addEventListener('DOMContentLoaded', () => {

    function setCookie(name, value, days) {
        const expires = new Date(Date.now() + days * 864e5).toUTCString();
        document.cookie = name + '=' + encodeURIComponent(value) + '; expires=' + expires + '; path=/';
    }
    
    function getCookie(name) {
        return document.cookie.split('; ').reduce((r, v) => {
            const parts = v.split('=');
            return parts[0] === name ? decodeURIComponent(parts[1]) : r
        }, '');
    }
    
    window.addEventListener('load', () => {
        if (!getCookie('cookie-consent')) {
            document.getElementById('cookie-consent').style.display = 'flex';
        }
    });
    
    document.getElementById('accept-cookies').addEventListener('click', () => {
        setCookie('cookie-consent', 'accepted', 7);
        document.getElementById('cookie-consent').style.display = 'none';
    });
    
    document.getElementById('decline-cookies').addEventListener('click', () => {
        setCookie('cookie-consent', 'declined', 7);
        document.getElementById('cookie-consent').style.display = 'none';
    });
});