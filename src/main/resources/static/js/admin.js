function hideAdminForms(){
    document.getElementById("createMovieForm").style.display = "none";
}

function showCreateMovie() {
    hideAdminForms();
    document.getElementById("createMovieForm").style.display = "block";


    async function createMovie() {

        const adminUserId = localStorage.getItem("userId");

        const movie = {
            title: document.getElementById("title").value,
            category: document.getElementById("category").value,
            ageLimit: parseInt(document.getElementById("ageLimit").value),
            duration: parseInt(document.getElementById("duration").value),
            filmType: document.getElementById("filmType").value
        };

        const response = await fetch(`/kino/movies?adminUserId=${adminUserId}`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(movie)
        });

        if (response.ok) {
            alert("Movie created");
            loadMovies();
        }
    }
}