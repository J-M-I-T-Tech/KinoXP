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

async function getPoster(title) {
    try {
        const res = await fetch(
            `https://www.omdbapi.com/?t=${encodeURIComponent(title)}&apikey=${OMDB_API_KEY}`
        );
        const data = await res.json();
        return data.Poster && data.Poster !== 'N/A'
            ? data.Poster
            : null;
    } catch {
        return null;
    }
}

function formatDateTime(dateTime) {
    if (!dateTime) return '–';

    const date = new Date(dateTime);

    return (
        date.toLocaleDateString('da-DK') +
        ' kl. ' +
        date.toLocaleTimeString('da-DK', {
            hour: '2-digit',
            minute: '2-digit'
        })
    );
}

function formatBookingStatus(status) {
    const map = {
        PENDING: '⏳ Afventer',
        CONFIRMED: '✅ Bekræftet',
        CANCELLED: '❌ Annulleret',
        EXPIRED: '⌛ Udløbet'
    };
    return map[status] || status;
}

function formatPaymentStatus(status) {
    const map = {
        AWAITING: '💳 Afventer betaling',
        PAID: '💳 Betalt',
        REFUNDED: '💳 Refunderet',
        CANCELLED: '💳 Annulleret'
    };
    return map[status] || status;
}

async function deleteReservation(reservationId) {
    if (!confirm('Er du sikker på, at du vil annullere denne reservation?'))
        return;

    const response = await fetch(`/kino/reservations/${reservationId}`, {
        method: 'DELETE',
        headers: getCsrfToken() ? { 'X-XSRF-TOKEN': getCsrfToken() } : {}
    });

    if (response.ok) {
        alert('Reservation annulleret.');
        loadReservations();
    } else {
        alert('Kunne ikke annullere reservationen.');
    }
}

function editReservation(reservationId, movieId) {
    if (
        !confirm(
            'Vil du ændre denne reservation? Den gamle slettes ved bekræftelse.'
        )
    )
        return;

    sessionStorage.setItem('editingReservationId', reservationId);
    window.location.href = `booking.html?movieId=${movieId}`;
}

function checkUserLogin() {
    const userRaw = localStorage.getItem('user');

    if (!userRaw) {
        alert('Du skal logge ind.');
        window.location.href = 'login.html';
        return null;
    }

    const user = JSON.parse(userRaw);

    document.getElementById(
        'userName'
    ).textContent = `Logget ind som: ${user.name}`;

    document
        .getElementById('logoutLink')
        .addEventListener('click', async (e) => {
            e.preventDefault();
            await performLogout();
        });

    return user;
}

async function loadReservations() {
    const user = checkUserLogin();
    if (!user) return;

    const container = document.getElementById('reservations-container');

    try {
        const response = await fetch(
            `/kino/reservations/customer/${encodeURIComponent(user.name)}`
        );

        const reservations = await response.json();

        if (!reservations || reservations.length === 0) {
            container.innerHTML = `
        <div style="text-align:center; color:#888; margin-top:60px;">
          <p>Du har ingen reservationer endnu.</p>
          <a href="index.html">🎬 Find en film</a>
        </div>`;
            return;
        }

        container.innerHTML = '';

        for (const res of reservations) {
            const posterUrl = await getPoster(res.movieTitle);

            const card = document.createElement('div');
            card.className = 'movie-card';

            card.innerHTML = `
        ${
                posterUrl
                    ? `<img src="${posterUrl}" style="width:70px; border-radius:5px; margin-right:15px;">`
                    : ''
            }
        <div style="flex:1;">
          <h2>${res.movieTitle ?? 'Film'}</h2>
          <p>📅 ${formatDateTime(res.showingStartTime)}</p>
          <p>🏛 Sal: ${res.theaterName ?? '–'}</p>
          <p><strong>Pris:</strong> ${res.totalPrice ?? '-'} kr.</p>
          <p>${formatBookingStatus(res.bookingStatus)}</p>
          <p>${formatPaymentStatus(res.paymentStatus)}</p>
        </div>
        <div style="display:flex; flex-direction:column; gap:8px;">
          <button class="btn" style="background:#555;"
            onclick="editReservation(${res.reservationId}, ${res.movieId})">
            Rediger
          </button>

          <button class="btn" style="background:#b71c1c;"
            onclick="deleteReservation(${res.reservationId})">
            Annullér
          </button>
        </div>
      `;

            container.appendChild(card);
        }

    } catch (error) {
        console.error('Fejl:', error);
        container.innerHTML =
            '<p>⚠️ Kunne ikke hente reservationer.</p>';
    }
}

loadReservations();