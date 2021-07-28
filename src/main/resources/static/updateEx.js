function submitUpdate() {
    fetch("http://localhost:8080/api/")
        .then(res => res.json())
        .then(data => renderUsers(data))
}

const form = document.querySelector('form');
document.getElementById("new_user").addEventListener('mousemove', submitUpdate);