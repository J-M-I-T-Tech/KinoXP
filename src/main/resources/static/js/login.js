document.addEventListener("DOMContentLoaded", () => {

    const form = document.getElementById("loginForm");
    const getCsrfToken = () => {
        const match = document.cookie.match(/(?:^|;\s*)XSRF-TOKEN=([^;]+)/);
        return match ? decodeURIComponent(match[1]) : null;
    };

    form.addEventListener("submit", async (e) => {
        e.preventDefault();

        const name = document.getElementById("name").value;
        const password = document.getElementById("password").value;
        const errorDiv = document.getElementById("errorMessage");

        try {
            const response = await fetch('/kino/users/login', {
                method: 'POST',
                credentials: 'same-origin',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                    ...(getCsrfToken() ? { 'X-XSRF-TOKEN': getCsrfToken() } : {})
                },
                body: new URLSearchParams({
                    username: name,
                    password: password
                })
            });

            if (response.ok) {
                const meResponse = await fetch('/kino/users/me', {
                    credentials: 'same-origin',
                    headers: {
                        'Accept': 'application/json'
                    }
                });
                const contentType = meResponse.headers.get('content-type') || '';

                if (!meResponse.ok || !contentType.includes('application/json')) {
                    errorDiv.textContent = 'Forkert navn eller adgangskode';
                    return;
                }

                const me = await meResponse.json();
                localStorage.setItem('user', JSON.stringify(me));
                window.location.href = '/html/index.html';
            } else {
                errorDiv.textContent = 'Forkert navn eller adgangskode';
            }

        } catch (error) {
            errorDiv.textContent = 'Der opstod en fejl. Prøv igen senere.';
            console.error("Login fejl:", error);
        }
    });

});