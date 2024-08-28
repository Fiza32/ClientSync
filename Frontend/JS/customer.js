const url="http://localhost:8080/api/sunbase";
const token = localStorage.getItem('jwtToken');

function saveCustomer(event) {
    event.preventDefault();

    const customer = {
        first_name: document.getElementById('first_name').value,
        last_name: document.getElementById('last_name').value,
        street: document.getElementById('street').value,
        address: document.getElementById('address').value,
        city: document.getElementById('city').value,
        state: document.getElementById('state').value,
        email: document.getElementById('email').value,
        phone: document.getElementById('phone').value
    };
	
	console.log('Customer to be saved:', customer); // Debugging

    fetch(`${url}/save_customer`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify(customer)
    })
    .then(response => {
        if (response.ok) {
            console.log(token);
            return response.json();
        } else {
            throw new Error('Failed to save customer');
        }
    })
    .then(data => {
        alert('Customer saved successfully');
        window.location.href = 'customerlist.html';
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Error during saving customer');
    });
}

document.querySelector('form').addEventListener('submit', saveCustomer);
