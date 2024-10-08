const API_URL="http://localhost:8080/api/sunbase";

document.addEventListener('DOMContentLoaded', () => {
    const urlParams = new URLSearchParams(window.location.search);
    const uuid = urlParams.get('id');
    console.log("UUID on load:", uuid); // Debugging
  
    if (uuid) {
      getCustomer(uuid);
    } else {
      alert('No customer ID provided');
      window.location.href = 'customerlist.html';
    }
  
    document.getElementById('editCustomerForm').addEventListener('submit', (event) => {
      event.preventDefault();
      updateCustomer();
    });
  });
  
  async function getCustomer(uuid) {
    const token = localStorage.getItem('jwtToken');
    if (!token) {
      window.location.href = 'index.html';
      return;
    }
  
    try {
      const response = await fetch(`${API_URL}/${uuid}`, {
        method: 'GET',
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });
  
      if (response.ok) {
        const customer = await response.json();
        populateCustomerForm(customer);
      } else {
        alert('Failed to fetch customer details!');
      }
    } catch (error) {
      console.error('Error:', error);
    }
  }
  
  function populateCustomerForm(customer) {
    document.getElementById('first_name').value = customer.first_name;
    document.getElementById('last_name').value = customer.last_name;
    document.getElementById('street').value = customer.street;
    document.getElementById('address').value = customer.address;
    document.getElementById('city').value = customer.city;
    document.getElementById('state').value = customer.state;
    document.getElementById('email').value = customer.email;
    document.getElementById('phone').value = customer.phone;
  }
  
  async function updateCustomer() {
    const urlParams = new URLSearchParams(window.location.search);
    const uuid = urlParams.get('id');
    console.log("UUID for update:", uuid); // Debugging
    const token = localStorage.getItem('jwtToken');
  
    if (!uuid || !token) {
      alert('Missing customer ID or token');
      return;
    }
  
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
  
    try {
      const response = await fetch(`${API_URL}/editCustomer/${uuid}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify(customer)
      });
  
      if (response.ok) {
        alert('Customer updated successfully');
        window.location.href = 'customerlist.html';
      } else {
        alert('Failed to update customer');
      }
    } catch (error) {
      console.error('Error:', error);
    }
  }
  