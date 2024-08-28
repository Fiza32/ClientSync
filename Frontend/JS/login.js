const url="http://localhost:8080";

document.getElementById('sign-in-button').addEventListener('click', function() {
    const email = document.getElementById('email').value;
    const pass = document.getElementById('password').value;
    
    fetch(`${url}/api/sunbase/auth/login`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ email, pass })
    })
    .then(response => response.json())
    .then(data => {
        console.log(data);
        if (data.jwtToken) {
            localStorage.setItem('jwtToken', data.jwtToken);
            alert("Login Successful");

            window.location.href = 'customerlist.html';
        } else {
            alert('Invalid credentials');
        }
    })
    .catch(error => {
        alert('Invalid credentials');
        console.error('Error:', error);
    });
});

function returnHome() {
     window.location.href = 'index.html';
}