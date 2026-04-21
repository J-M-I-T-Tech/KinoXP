const OMDB_API_KEY = '8e71e427';

function getCsrfToken() {
    const match = document.cookie.match(/(?:^|;\s*)XSRF-TOKEN=([^;]+)/);
    return match ? decodeURIComponent(match[1]) : null;
}

async function performLogout() {
    try {
        await fetch('/kino/users/logout', {
            method: 'POST',
            credentials: 'same-origin',
            headers: getCsrfToken() ? { 'X-XSRF-TOKEN': getCsrfToken() } : {}
        });
    } catch (error) {
        console.error('Fejl ved logout:', error);
    } finally {
        localStorage.removeItem('user');
        window.location.href = 'index.html';
    }
}

function checkUserLogin() {
    const user = localStorage.getItem('user');

    const loginLink = document.getElementById('loginLink');
    const registerLink = document.getElementById('registerLink');
    const logoutLink = document.getElementById('logoutLink');
    const userName = document.getElementById('userName');

    if (user) {
        const userData = JSON.parse(user);

        loginLink.style.display = 'none';
        registerLink.style.display = 'none';
        logoutLink.style.display = 'inline';
        userName.style.display = 'inline';
        userName.textContent = `Logget ind som: ${userData.name}`;

        logoutLink.addEventListener('click', async (e) => {
            e.preventDefault();
            await performLogout();
        });
    }
}

function bookMovie(movieId, showingId) {
    const user = localStorage.getItem('user');

    if (!user) {
        alert('Du skal logge ind for at booke en film!');
        window.location.href = 'login.html';
        return;
    }

    if (!showingId) {
        window.location.href = `booking.html?movieId=${movieId}`;
        return;
    }

    window.location.href = `booking.html?movieId=${movieId}&showingId=${showingId}`;
}

function addFilter(type, value) {
    const params = new URLSearchParams();
    params.set(type, value);
    window.location.href = `index.html?${params.toString()}`;
}

function formatAgeLimit(ageLimit) {
    const map = {
        'ALL': 'Alle',
        'SEVEN_PLUS': '7+',
        'ELEVEN_PLUS': '11+',
        'FIFTEEN_PLUS': '15+',
        'EIGHTEEN_PLUS': '18+'
    };
    return map[ageLimit] || ageLimit;
}

function formatDateTime(dateTimeStr) {
    const date = new Date(dateTimeStr);

    return date.toLocaleDateString('da-DK', {
            weekday: 'long',
            day: 'numeric',
            month: 'long'
        }) + ' kl. ' +
        date.toLocaleTimeString('da-DK', {
            hour: '2-digit',
            minute: '2-digit'
        });
}

async function loadMovie() {
    const params = new URLSearchParams(window.location.search);
    const movieId = params.get('movieId');

    if (!movieId) {
        window.location.href = 'index.html';
        return;
    }

    try {
        const response = await fetch(`/kino/movies/${movieId}`);

        if (!response.ok) {
            document.getElementById('movie-detail').innerHTML =
                '<p>Film ikke fundet.</p>';
            return;
        }

        const movie = await response.json();

        // OMDb plakat
        let posterHtml = '';
        try {
            const omdbRes = await fetch(
                `https://www.omdbapi.com/?t=${encodeURIComponent(movie.title)}&y=${movie.releaseYear}&apikey=${OMDB_API_KEY}`
            );
            const omdbData = await omdbRes.json();

            if (omdbData.Poster && omdbData.Poster !== 'N/A') {
                posterHtml = `<img src="${omdbData.Poster}" class="movie-poster">`;
            }
        } catch (e) {}

        document.getElementById('movie-detail').innerHTML = `
            <div class="movie-header">
                ${posterHtml}
                <div>
                    <h1>${movie.title} (${movie.releaseYear})</h1>

                    <p>${movie.description || 'Ingen beskrivelse.'}</p>

                    <button class="btn"
                        onclick="bookMovie(${movie.movieId})">
                        🎟 Køb billetter
                    </button>
                </div>
            </div>
        `;
    } catch (error) {
        console.error('Fejl ved indlæsning:', error);
        document.getElementById('movie-detail').innerHTML =
            '<p>Der opstod en fejl.</p>';
    }
}

checkUserLogin();
loadMovie();