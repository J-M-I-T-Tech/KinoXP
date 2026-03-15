const OMDB_API_KEY = '8e71e427';

const showingsCache = {};

function hideAll() {
    document.getElementById("createMovieForm").style.display = "none";
    document.getElementById("movies-container").style.display = "none";
    document.getElementById("active-filter").style.display = "none";
    document.getElementById("showingsAdminPanel").style.display = "none";
}

function showCreateMovie() {
    hideAll();
    document.getElementById("createMovieForm").style.display = "block";
}

function showAllMovies() {
    hideAll();
    document.getElementById("movies-container").style.display = "block";
    document.getElementById("active-filter").style.display = "block";
    loadMovies();
}

function showShowingsAdmin() {
    hideAll();
    document.getElementById("showingsAdminPanel").style.display = "block";
    loadMoviesForShowingsAdmin();
}

function bookMovie(movieId) {
    const user = localStorage.getItem('user');
    if (!user) {
        alert('Du skal logge ind for at booke en film!');
        window.location.href = 'login.html';
        return;
    }
    window.location.href = `booking.html?movieId=${movieId}`;
}

function checkUserLogin() {
    const user = localStorage.getItem('user');
    const loginLink = document.getElementById('loginLink');
    const registerLink = document.getElementById('registerLink');
    const logoutLink = document.getElementById('logoutLink');
    const userName = document.getElementById('userName');
    const myReservationsLink = document.getElementById('myReservationsLink');

    if (user) {
        const userData = JSON.parse(user);
        loginLink.style.display = 'none';
        registerLink.style.display = 'none';
        logoutLink.style.display = 'inline';
        userName.style.display = 'inline';
        userName.textContent = `Logget ind som: ${userData.name}`;

        if (userData.role === "CUSTOMER") {
            myReservationsLink.style.display = 'inline';
        }

        if (userData.role === "ADMIN") {
            document.getElementById("adminPanel").style.display = "block";
        }

        logoutLink.onclick = (e) => {
            e.preventDefault();
            localStorage.removeItem('user');
            alert('Du er nu logget ud.');
            window.location.href = 'index.html';
        };
    }
}

function formatAgeLimit(ageLimit) {
    const map = { 'ALLE': 'Alle', 'SYV_PLUS': '7+', 'ELLEVE_PLUS': '11+', 'FEMTEN_PLUS': '15+', 'ATTEN_PLUS': '18+' };
    return map[ageLimit] || ageLimit;
}

function formatLanguage(lang) {
    const map = { 'DANSK': 'Dansk', 'ENGELSK': 'Engelsk' };
    return map[lang] || lang;
}

function formatFormat(format) {
    const map = { 'FORMAT_2D': '2D', 'FORMAT_3D': '3D' };
    return map[format] || format;
}

function formatGenre(genre) {
    const map = { 'GYSER': 'Gyser', 'ROMANTISK': 'Romantik', 'ACTION': 'Action', 'SCIENCE_FICTION': 'Sci-Fi', 'KOMEDIE': 'Komedie', 'DRAMA': 'Drama' };
    return map[genre] || genre;
}

function addFilter(type, value) {
    const params = new URLSearchParams(window.location.search);
    params.set(type, value);
    window.location.href = `index.html?${params.toString()}`;
}

async function getPoster(title, year) {
    try {
        const res = await fetch(`https://www.omdbapi.com/?t=${encodeURIComponent(title)}&y=${year}&apikey=${OMDB_API_KEY}`);
        const data = await res.json();
        return data.Poster && data.Poster !== 'N/A' ? data.Poster : null;
    } catch (e) {
        return null;
    }
}

async function loadMovies() {
    const userStr = localStorage.getItem("user");
    const user = userStr ? JSON.parse(userStr) : null;
    const isAdmin = user && user.role === "ADMIN";

    try {
        const params = new URLSearchParams(window.location.search);
        const ageFilter = params.get('ageLimit');
        const genreFilter = params.get('genre');
        const langFilter = params.get('language');
        const formatFilter = params.get('format');

        const response = await fetch('/kino/movies');
        let movies = await response.json();

        if (ageFilter) movies = movies.filter(m => m.ageLimit === ageFilter);
        if (genreFilter) movies = movies.filter(m => m.genre === genreFilter);
        if (langFilter) movies = movies.filter(m => m.language === langFilter);
        if (formatFilter) movies = movies.filter(m => m.format === formatFilter);

        const activeFilters = [];
        if (ageFilter) activeFilters.push(`Aldersgrænse: <strong>${formatAgeLimit(ageFilter)}</strong>`);
        if (genreFilter) activeFilters.push(`Genre: <strong>${formatGenre(genreFilter)}</strong>`);
        if (langFilter) activeFilters.push(`Sprog: <strong>${formatLanguage(langFilter)}</strong>`);
        if (formatFilter) activeFilters.push(`Format: <strong>${formatFormat(formatFilter)}</strong>`);

        const filterDiv = document.getElementById('active-filter');
        if (activeFilters.length > 0) {
            filterDiv.innerHTML = `<p style="color:#888; margin-bottom:10px;">Filtreret på: ${activeFilters.join(' | ')} &nbsp;<a href="index.html" style="color:#d32f2f; font-size:13px;">Fjern alle filtre</a></p>`;
        } else {
            filterDiv.innerHTML = '';
        }

        const container = document.getElementById('movies-container');
        container.innerHTML = '';

        for (const movie of movies) {
            const desc = movie.description || movie.Description || '';
            const posterUrl = await getPoster(movie.title, movie.releaseYear);
            const card = document.createElement('div');
            card.className = 'movie-card';
            card.innerHTML = `
                ${posterUrl ? `<img src="${posterUrl}" alt="${movie.title}" style="width:70px; border-radius:5px; margin-right:15px; flex-shrink:0;">` : ''}
                <div class="movie-info" style="flex:1;">
                    <h2>${movie.title} (${movie.releaseYear})</h2>
                    <p>
                        <span class="badge" onclick="addFilter('genre', '${movie.genre}')" style="cursor:pointer;">${formatGenre(movie.genre)}</span>
                        <span class="badge" onclick="addFilter('language', '${movie.language}')" style="cursor:pointer;">${formatLanguage(movie.language)}</span>
                        <span class="badge" onclick="addFilter('format', '${movie.format}')" style="cursor:pointer;">${formatFormat(movie.format)}</span>
                    </p>
                    <p>
                        <span class="badge age" onclick="addFilter('ageLimit', '${movie.ageLimit}')" style="cursor:pointer;">${formatAgeLimit(movie.ageLimit)}</span>
                        | ${movie.durationInMinutes} min
                    </p>
                </div>
                <div style="display:flex; gap:10px; flex-shrink:0;">
                    <button class="btn btn-secondary" onclick="window.location.href='movie.html?movieId=${movie.movieId}'">Læs mere</button>
                    <button class="btn" onclick="bookMovie(${movie.movieId})">Bestil nu</button>
                    ${isAdmin ? `
                        <button class="btn" style="background:#555;" onclick="openEditMovie(${movie.movieId})">Rediger</button>
                        <button class="btn btn-danger" onclick="deleteMovie(${movie.movieId})">Slet</button>
                    ` : ''}
                </div>
            `;
            container.appendChild(card);
        }
    } catch (error) {
        console.error('Error loading movies:', error);
    }
}

async function loadMoviesForShowingsAdmin() {
    try {
        const response = await fetch('/kino/movies');
        const movies = await response.json();

        const select = document.getElementById('showingMovieSelect');
        select.innerHTML = '<option value="">-- Vælg en film --</option>';
        movies.forEach(movie => {
            const option = document.createElement('option');
            option.value = movie.movieId;
            option.textContent = movie.title;
            select.appendChild(option);
        });

        const modalSelect = document.getElementById('modalMovieId');
        modalSelect.innerHTML = '';
        movies.forEach(movie => {
            const option = document.createElement('option');
            option.value = movie.movieId;
            option.textContent = movie.title;
            modalSelect.appendChild(option);
        });
    } catch (error) {
        console.error('Fejl ved hentning af film:', error);
    }
}

async function loadTheatersForModal() {
    try {
        const response = await fetch('/kino/theaters');
        const theaters = await response.json();
        const select = document.getElementById('modalTheaterId');
        select.innerHTML = '';
        theaters.forEach(t => {
            const option = document.createElement('option');
            option.value = t.theaterId;
            option.textContent = t.theaterName;
            select.appendChild(option);
        });
    } catch (error) {
        console.error('Fejl ved hentning af sale:', error);
    }
}

async function loadShowingsForMovie() {
    const movieId = document.getElementById('showingMovieSelect').value;

    document.getElementById('showingsTableContainer').style.display = 'none';
    document.getElementById('noShowings').style.display = 'none';

    if (!movieId) return;

    try {
        const response = await fetch(`/kino/showings/movie/${movieId}/all`);
        const showings = await response.json();

        const tbody = document.getElementById('showingsTableBody');
        tbody.innerHTML = '';

        if (showings.length === 0) {
            document.getElementById('noShowings').style.display = 'block';
            return;
        }

        showings.forEach(showing => {
            showingsCache[showing.showingId] = showing;

            const statusLabel = {
                'UPCOMING': 'Kommende',
                'RUNNING': 'I gang',
                'COMPLETED': 'Afsluttet',
                'CANCELLED': 'Aflyst'
            }[showing.showingStatus] || showing.showingStatus;

            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${showing.movieTitle}</td>
                <td>${showing.theaterName}</td>
                <td>${formatDateTime(showing.startTime)}</td>
                <td>${formatDateTime(showing.endTime)}</td>
                <td><span class="status-badge">${statusLabel}</span></td>
                <td style="display:flex; gap:6px;">
                    <button class="btn" style="padding:4px 10px; font-size:0.85em;" onclick="openEditShowingModal(${showing.showingId})">Rediger</button>
                    <button class="btn btn-danger" style="padding:4px 10px; font-size:0.85em;" onclick="deleteShowing(${showing.showingId})">Slet</button>
                </td>
            `;
            tbody.appendChild(row);
        });

        document.getElementById('showingsTableContainer').style.display = 'block';
    } catch (error) {
        console.error('Fejl ved hentning af visninger:', error);
        document.getElementById('noShowings').style.display = 'block';
    }
}

function formatDateTime(dateTime) {
    const date = new Date(dateTime);
    return date.toLocaleDateString('da-DK') + ' ' + date.toLocaleTimeString('da-DK', { hour: '2-digit', minute: '2-digit' });
}

function toDateTimeLocal(dateTimeStr) {
    const d = new Date(dateTimeStr);
    const pad = n => String(n).padStart(2, '0');
    return `${d.getFullYear()}-${pad(d.getMonth()+1)}-${pad(d.getDate())}T${pad(d.getHours())}:${pad(d.getMinutes())}`;
}

async function openCreateShowingModal() {
    document.getElementById('modalTitle').textContent = 'Opret visning';
    document.getElementById('editShowingId').value = '';
    document.getElementById('modalStartTime').value = '';
    document.getElementById('modalEndTime').value = '';
    document.getElementById('modalStatus').value = 'UPCOMING';

    const movieId = document.getElementById('showingMovieSelect').value;
    await loadTheatersForModal();

    if (movieId) {
        document.getElementById('modalMovieId').value = movieId;
    }

    document.getElementById('showingModal').classList.add('active');
}

async function openEditShowingModal(showingId) {
    const showing = showingsCache[showingId];
    if (!showing) {
        alert('Visning ikke fundet – prøv at genindlæse listen.');
        return;
    }

    document.getElementById('modalTitle').textContent = 'Rediger visning';
    document.getElementById('editShowingId').value = showing.showingId;

    await loadTheatersForModal();

    document.getElementById('modalMovieId').value = showing.movieId;
    document.getElementById('modalTheaterId').value = showing.theaterId;
    document.getElementById('modalStartTime').value = toDateTimeLocal(showing.startTime);
    document.getElementById('modalEndTime').value = toDateTimeLocal(showing.endTime);
    document.getElementById('modalStatus').value = showing.showingStatus;

    document.getElementById('showingModal').classList.add('active');
}

function closeModal() {
    document.getElementById('showingModal').classList.remove('active');
}

async function saveShowing() {
    const user = JSON.parse(localStorage.getItem("user"));
    const editId = document.getElementById('editShowingId').value;

    const body = {
        movieId: parseInt(document.getElementById('modalMovieId').value),
        theaterId: parseInt(document.getElementById('modalTheaterId').value),
        startTime: document.getElementById('modalStartTime').value,
        endTime: document.getElementById('modalEndTime').value,
        showingStatus: document.getElementById('modalStatus').value
    };

    const isEdit = !!editId;
    const url = isEdit
        ? `/kino/showings/${editId}?userId=${user.userId}`
        : `/kino/showings?userId=${user.userId}`;

    try {
        const response = await fetch(url, {
            method: isEdit ? 'PUT' : 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(body)
        });

        if (response.ok) {
            closeModal();
            loadShowingsForMovie();
        } else {
            const err = await response.text();
            alert('Fejl: ' + err);
        }
    } catch (error) {
        console.error('Fejl ved gem af visning:', error);
    }
}

async function deleteShowing(showingId) {
    if (!confirm('Er du sikker på at du vil slette denne visning?')) return;

    const user = JSON.parse(localStorage.getItem("user"));

    try {
        const response = await fetch(`/kino/showings/${showingId}?userId=${user.userId}`, {
            method: 'DELETE'
        });

        if (response.ok) {
            loadShowingsForMovie();
        } else {
            alert('Fejl ved sletning af visning.');
        }
    } catch (error) {
        console.error('Fejl ved sletning:', error);
    }
}

async function createMovie() {
    const user = JSON.parse(localStorage.getItem("user"));
    const adminUserId = user.userId;

    const movie = {
        title: document.getElementById("title").value,
        description: document.getElementById("beskrivelse").value,
        genre: document.getElementById("genre").value,
        ageLimit: document.getElementById("ageLimit").value,
        language: document.getElementById("language").value,
        format: document.getElementById("format").value,
        durationInMinutes: parseInt(document.getElementById("duration").value),
        releaseYear: parseInt(document.getElementById("releaseYear").value)
    };

    try {
        const response = await fetch(`/kino/movies?userId=${adminUserId}`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(movie)
        });

        if (response.ok) {
            alert("Film oprettet!");
            document.querySelector("#createMovieForm form").reset();
            showAllMovies();
        } else {
            const errorText = await response.text();
            alert("Fejl ved oprettelse af film: " + errorText);
        }
    } catch (error) {
        console.error("Fejl ved oprettelse af film:", error);
    }
}

async function deleteMovie(movieId) {
    const user = JSON.parse(localStorage.getItem("user"));

    const response = await fetch(`/kino/movies/${movieId}?userId=${user.userId}`, {
        method: "DELETE"
    });

    if (response.ok) {
        alert("Film slettet");
        showAllMovies();
    } else {
        const errorText = await response.text();
        alert("Kun admin kan slette film: " + errorText);
    }
}

async function openEditMovie(movieId) {

    const response = await fetch(`/kino/movies/${movieId}`);
    const movie = await response.json();

    showCreateMovie();

    document.getElementById("title").value = movie.title;
    document.getElementById("beskrivelse").value = movie.description;
    document.getElementById("genre").value = movie.genre;
    document.getElementById("ageLimit").value = movie.ageLimit;
    document.getElementById("language").value = movie.language;
    document.getElementById("format").value = movie.format;
    document.getElementById("duration").value = movie.durationInMinutes;
    document.getElementById("releaseYear").value = movie.releaseYear;

    const form = document.querySelector("#createMovieForm form");

    form.onsubmit = async function(e){
        e.preventDefault();

        const user = JSON.parse(localStorage.getItem("user"));

        const updatedMovie = {
            title: document.getElementById("title").value,
            description: document.getElementById("beskrivelse").value,
            genre: document.getElementById("genre").value,
            ageLimit: document.getElementById("ageLimit").value,
            language: document.getElementById("language").value,
            format: document.getElementById("format").value,
            durationInMinutes: parseInt(document.getElementById("duration").value),
            releaseYear: parseInt(document.getElementById("releaseYear").value)
        };

        const response = await fetch(`/kino/movies/${movieId}?userId=${user.userId}`,{
            method:"PUT",
            headers:{
                "Content-Type":"application/json"
            },
            body:JSON.stringify(updatedMovie)
        });

        if(response.ok){
            alert("Film opdateret!");
            showAllMovies();
        }else{
            alert("Fejl ved redigering");
        }
    }
}

checkUserLogin();
loadMovies();
