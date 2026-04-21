const urlParams = new URLSearchParams(window.location.search);
const movieId = urlParams.get('movieId');
let showingId = urlParams.get('showingId');

if (!movieId) {
    window.location.href = 'index.html';
}

let selectedSeats = [];
let theaterId = null;
let movieData = null;
let currentShowing = null;

function getCsrfToken() {
    const match = document.cookie.match(/(?:^|;\s*)XSRF-TOKEN=([^;]+)/);
    return match ? decodeURIComponent(match[1]) : null;
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

async function loadBookingData() {
    try {
        const movieResp = await fetch(`/kino/movies/${movieId}`);
        movieData = await movieResp.json();

        document.getElementById('booking-title').textContent =
            `Reserver billetter: ${movieData.title}`;

        const showingsResp = await fetch(`/kino/showings/movie/${movieId}`);
        const showings = await showingsResp.json();

        if (!showingId) {
            renderShowingSelection(showings);
            return;
        }

        currentShowing = showings.find(s => s.showingId == showingId);

        if (!currentShowing) {
            renderShowingSelection(showings);
            return;
        }

        await initializeBooking(currentShowing);

    } catch (error) {
        console.error(error);
    }
}

function renderShowingSelection(showings) {
    document.getElementById('showing-selection').style.display = 'block';
    document.getElementById('booking-content').style.display = 'none';

    const list = document.getElementById('showings-list');
    list.innerHTML = '';

    showings.forEach(s => {
        const btn = document.createElement('button');
        btn.className = 'btn';
        btn.textContent = formatDateTime(s.startTime);

        btn.onclick = () => {
            showingId = s.showingId;
            initializeBooking(s);
        };

        list.appendChild(btn);
    });
}

async function initializeBooking(showing) {
    currentShowing = showing;
    theaterId = showing.theaterId;

    document.getElementById('booking-content').style.display = 'block';

    const seatsResp = await fetch(`/kino/seats/theater/${theaterId}`);
    const allSeats = await seatsResp.json();

    const reservedResp = await fetch(
        `/kino/reservations/showing/${showing.showingId}/reserved-seats`
    );
    const reservedSeatIds = await reservedResp.json();

    renderSeatGrid(allSeats, reservedSeatIds);
}

function renderSeatGrid(allSeats, reservedSeatIds) {
    const grid = document.getElementById('seat-grid');
    grid.innerHTML = '';

    // Grupper sæder efter række
    const rows = {};
    allSeats.forEach(seat => {
        if (!rows[seat.rowNumber]) {
            rows[seat.rowNumber] = [];
        }
        rows[seat.rowNumber].push(seat);
    });

    // Sorter rækkerne (højeste rækkenummer øverst, da det er tættest på lærredet i nogle biografer, 
    // eller laveste øverst - vi vælger laveste øverst her)
    const sortedRowNumbers = Object.keys(rows).sort((a, b) => a - b);

    sortedRowNumbers.forEach(rowNum => {
        const rowDiv = document.createElement('div');
        rowDiv.className = 'seat-row';
        
        // Valgfrit: Tilføj rækkenummer i starten
        const rowLabel = document.createElement('div');
        rowLabel.style.width = '30px';
        rowLabel.style.fontSize = '0.8em';
        rowLabel.style.color = '#777';
        rowLabel.textContent = `R${rowNum}`;
        rowDiv.appendChild(rowLabel);

        // Sorter sæder i rækken efter nummer
        rows[rowNum].sort((a, b) => a.seatNumber - b.seatNumber);

        rows[rowNum].forEach(seat => {
            const seatDiv = document.createElement('div');
            const isReserved = reservedSeatIds.includes(seat.seatId);
            
            seatDiv.className = 'seat';
            if (isReserved) {
                seatDiv.classList.add('reserved');
                seatDiv.title = `Sæde ${seat.seatNumber}, Række ${seat.rowNumber} (Optaget)`;
            } else {
                seatDiv.title = `Sæde ${seat.seatNumber}, Række ${seat.rowNumber}`;
                seatDiv.addEventListener('click', () => toggleSeat(seat, seatDiv));
            }
            
            // Vis sædenummer inde i sædet
            seatDiv.textContent = seat.seatNumber;

            rowDiv.appendChild(seatDiv);
        });

        grid.appendChild(rowDiv);
    });
}

function toggleSeat(seat, element) {
    const index = selectedSeats.findIndex(s =>
        s.seatId === seat.seatId
    );

    if (index > -1) {
        selectedSeats.splice(index, 1);
        element.classList.remove('selected');
    } else {
        selectedSeats.push(seat);
        element.classList.add('selected');
    }
    
    updateBookingSummary();
}

function updateBookingSummary() {
    const selectedSeatsList = document.getElementById('selected-seats-list');
    const ticketCount = document.getElementById('ticket-count');
    const totalPrice = document.getElementById('total-price');
    const confirmBtn = document.getElementById('confirm-booking-btn');
    
    if (selectedSeats.length === 0) {
        selectedSeatsList.textContent = '-';
        ticketCount.textContent = '0';
        totalPrice.textContent = '0';
        confirmBtn.disabled = true;
    } else {
        // Sorter sæder efter række og nummer for pæn visning
        const sortedSeats = [...selectedSeats].sort((a, b) => {
            if (a.rowNumber !== b.rowNumber) {
                return a.rowNumber - b.rowNumber;
            }
            return a.seatNumber - b.seatNumber;
        });
        
        const seatLabels = sortedSeats.map(seat => 
            `R${seat.rowNumber}S${seat.seatNumber}`
        ).join(', ');
        
        selectedSeatsList.textContent = seatLabels;
        ticketCount.textContent = selectedSeats.length;
        
        // Beregn total pris (antag 100 kr pr billet - juster efter behov)
        const pricePerTicket = 100;
        const total = selectedSeats.length * pricePerTicket;
        totalPrice.textContent = total;
        
        // Aktiver bekræft knappen hvis der er valgt sæder
        confirmBtn.disabled = false;
    }
}

loadBookingData();

// Tilføj event listener til bekræft knappen
document.addEventListener('DOMContentLoaded', function() {
    const confirmBtn = document.getElementById('confirm-booking-btn');
    if (confirmBtn) {
        confirmBtn.addEventListener('click', confirmBooking);
    }
});

async function confirmBooking() {
    if (selectedSeats.length === 0) {
        alert('Vælg venligst mindst ét sæde');
        return;
    }
    
    if (!currentShowing) {
        alert('Ingen forestilling valgt');
        return;
    }
    
    // Tjek om bruger er logget ind
    const userStr = localStorage.getItem('user');
    if (!userStr) {
        alert('Du skal være logget ind for at lave en reservation');
        return;
    }
    
    const user = JSON.parse(userStr);
    
    try {
        const seatIds = selectedSeats.map(seat => seat.seatId);
        
        const response = await fetch('/kino/reservations', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                ...(getCsrfToken() ? { 'X-XSRF-TOKEN': getCsrfToken() } : {})
            },
            body: JSON.stringify({
                showingId: currentShowing.showingId,
                customerName: user.name,
                userId: user.userId,
                seatIds: seatIds
            })
        });
        
        if (response.ok) {
            const reservation = await response.json();
            alert(`Reservation bekræftet! Reservation ID: ${reservation.reservationId}`);
            window.location.href = '/html/my-reservations.html';
        } else {
            const errorText = await response.text();
            alert('Fejl ved reservation: ' + errorText);
        }
    } catch (error) {
        console.error('Fejl ved reservation:', error);
        alert('Der opstod en fejl ved reservation. Prøv venligst igen.');
    }
}