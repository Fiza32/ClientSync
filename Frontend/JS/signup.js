const url="http://localhost:8080";

function sign_up() {
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;

    const user = {
        email: email,
        password: password
    };

    fetch(`${url}/api/sunbase/auth/register`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(user)
    })
    .then(response => {
        if (response.ok) {
            return response.json();
        } else {
            throw new Error('Failed to register user');
        }
    })
    .then(data => {
        alert('Registration successful');
        window.location.href = 'login.html';
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Error during registration');
    });
}

function returnHome() {
    window.location.href = 'index.html';
}
