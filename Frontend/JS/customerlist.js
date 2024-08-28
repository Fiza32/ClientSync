const url="http://localhost:8080/api/sunbase";
const token = localStorage.getItem('jwtToken');

// Toggle dropdown menu visibility
document.querySelector('.btn-dropdown').addEventListener('click', function() {
    var menu = document.getElementById('searchFieldMenu');
    menu.classList.toggle('show');
});

// Hide dropdown menu when clicking outside
window.addEventListener('click', function(event) {
    var dropdownButton = document.querySelector('.btn-dropdown');
    var menu = document.getElementById('searchFieldMenu');
    
    if (!dropdownButton.contains(event.target) && !menu.contains(event.target)) {
        if (menu.classList.contains('show')) {
            menu.classList.remove('show');
        }
    }
});

document.addEventListener('DOMContentLoaded', function () {
    var dropdownButton = document.querySelector('.btn-dropdown');
    var dropdownItems = document.querySelectorAll('.dropdown-menu .dropdown-item');
    
    dropdownItems.forEach(function (item) {
        item.addEventListener('click', function () {
            dropdownButton.childNodes[0].textContent = this.textContent;
            
            var selectedField = this.getAttribute('data-field');
            console.log('Selected field:', selectedField);

            var menu = document.getElementById('searchFieldMenu');
            menu.classList.remove('show');
        });
    });
});

function redirectToAddCustomer() {
    window.location.href = 'customers.html';
}


document.addEventListener('DOMContentLoaded', function() {
    let pageNumber = 0;
    const pageSize = 5;
    const sortBy = 'uuid';
    const sortDirection = 'asc';
    const token = localStorage.getItem('jwtToken');

    function fetchCustomers() {
        fetch(`${url}/all_customers?pageNumber=${pageNumber}&pageSize=${pageSize}&sortBy=${sortBy}&sortDirection=${sortDirection}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            }
        })
        .then(response => response.json())
        .then(data => {
            const tableBody = document.getElementById('tableBody');
            tableBody.innerHTML = '';

            data.content.forEach(customer => {
                const row = document.createElement('tr');

                row.innerHTML = `
                    <td>${customer.first_name}</td>
                    <td>${customer.last_name}</td>
                    <td>${customer.street}</td>
                    <td>${customer.address}</td>
                    <td>${customer.city}</td>
                    <td>${customer.state}</td>
                    <td>${customer.email}</td>
                    <td>${customer.phone}</td>
                    <td>
                        <!-- Edit Customer Button -->
                        <button class="btn btn-outline-primary btn-icon" onclick="editCustomer('${customer.uuid}')">
                            <i class="fa-solid fa-pen-to-square"></i>
                        </button>

                        <!-- Delete Customer Button -->
                        <button class="btn btn-outline-danger btn-icon" onclick="deleteCustomer('${customer.uuid}')">
                            <i class="fa-solid fa-trash"></i>
                        </button>
                    </td>
                `;

                tableBody.appendChild(row);
            });

            updatePagination(data.currentPage, data.totalPages);
        })
        .catch(error => console.error('Error fetching customers:', error));
    }

    function updatePagination(currentPage, totalPages) {
        const pageNumbers = document.getElementById('pageNumbers');
        pageNumbers.innerHTML = '';

        for (let i = 0; i < totalPages; i++) {
            const pageNumberElement = document.createElement('button');
            pageNumberElement.classList.add('pagination-button');
            pageNumberElement.innerText = i + 1;
            pageNumberElement.addEventListener('click', () => {
                pageNumber = i;
                fetchCustomers();
            });

            if (i === currentPage) {
                pageNumberElement.disabled = true;
            }

            pageNumbers.appendChild(pageNumberElement);
        }
    }

    // Fetch initial data
    fetchCustomers();
});

function editCustomer(uuid) {
    // Redirect to the edit page with the UUID as a URL parameter
    window.location.href = `editcustomer.html?id=${uuid}`;
}




function deleteCustomer(uuid) {
    const token = localStorage.getItem('jwtToken');
    fetch(`${url}/deleteCustomer/${uuid}`, {
        method: 'DELETE',
        headers: {
            'Authorization': `Bearer ${token}`
        }
    })
    .then(response => {
        if (response.ok) {
            alert('Customer deleted successfully');
            window.location.href = `customerlist.html`;
        } else {
            console.error('Error deleting customer:', response.statusText);
        }
    })
    .catch(error => console.error('Error:', error));
}

document.addEventListener('DOMContentLoaded', function() {
            document.getElementById('syncButton').addEventListener('click', function(event) {
                event.preventDefault();
                const token = localStorage.getItem('jwtToken');

                fetch(`${url}/sync_customers`, {
                    method: 'POST',
                    headers: {
                        'Authorization': `Bearer ${token}`
                    }
                })
                .then(response => response.text())
                .then(data => {
                    alert(data);
                    window.location.reload();
                })
                .catch(error => console.error('Error:', error));
            });
});

let selectedField = 'firstName';

document.querySelectorAll('#searchFieldMenu .dropdown-item').forEach(item => {
    item.addEventListener('click', function(event) {
        event.preventDefault();
        selectedField = this.getAttribute('data-field');
        console.log('Selected field:', selectedField);

        document.querySelector('.btn-dropdown').innerHTML = `Search By: ${this.innerText} <i id="dropdownButton" class="fas fa-chevron-down dropdown-icon"></i>`;
    });
});


document.getElementById('searchButton').addEventListener('click', function() {
    const searchValue = document.getElementById('searchValue').value;
    const token = localStorage.getItem('jwtToken');
    console.log('Search value:', searchValue); // Debugging

    fetch(`${url}/search_customers?field=${selectedField}&value=${encodeURIComponent(searchValue)}`, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        }
    })
    .then(response => response.json())
    .then(data => {
        const tableBody = document.getElementById('tableBody');
        tableBody.innerHTML = '';

        data.forEach(customer => {
            const row = document.createElement('tr');

            row.innerHTML = `
                <td>${customer.first_name}</td>
                <td>${customer.last_name}</td>
                <td>${customer.street}</td>
                <td>${customer.address}</td>
                <td>${customer.city}</td>
                <td>${customer.state}</td>
                <td>${customer.email}</td>
                <td>${customer.phone}</td>
                <td>
                    <button class="btn btn-outline-primary btn-icon" onclick="editCustomer('${customer.uuid}')">
                        <i class="fa-solid fa-pen-to-square"></i>
                    </button>

                    <!-- Delete Customer Button -->
                    <button class="btn btn-outline-danger btn-icon" onclick="deleteCustomer('${customer.uuid}')">
                        <i class="fa-solid fa-trash"></i>
                    </button>
                </td>
            `;

            tableBody.appendChild(row);
        });
    })
    .catch(error => console.error('Error fetching customers:', error));
});




document.addEventListener('DOMContentLoaded', function () {
    const rowsPerPage = 5;
    let currentPage = 1;
    const tableBody = document.getElementById('tableBody');
    const pageNumbersContainer = document.getElementById('pageNumbers');
    
    // Example data; replace with actual data
    const data = [
        
    ];

    function renderTable(data, page) {
        tableBody.innerHTML = '';
        const start = (page - 1) * rowsPerPage;
        const end = start + rowsPerPage;
        const paginatedData = data.slice(start, end);
        
        paginatedData.forEach(item => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${item.firstName}</td>
                <td>${item.lastName}</td>
                <td>${item.street}</td>
                <td>${item.address}</td>
                <td>${item.city}</td>
                <td>${item.state}</td>
                <td>${item.email}</td>
                <td>${item.phone}</td>
                <td>
                    <button class="btn btn-outline-primary btn-icon" onclick="editCustomer('${customer.uuid}')">
                        <i class="fa-solid fa-pen-to-square"></i>
                    </button>

                    <!-- Delete Customer Button -->
                    <button class="btn btn-outline-danger btn-icon" onclick="deleteCustomer('${customer.uuid}')">
                        <i class="fa-solid fa-trash"></i>
                    </button>
                </td>
            `;
            tableBody.appendChild(row);
        });
    }

    function renderPagination(data) {
        pageNumbersContainer.innerHTML = '';
        const totalPages = Math.ceil(data.length / rowsPerPage);
        
        for (let i = 1; i <= totalPages; i++) {
            const pageNumber = document.createElement('span');
            pageNumber.textContent = i;
            pageNumber.className = 'page-number';
            if (i === currentPage) {
                pageNumber.classList.add('active');
            }
            pageNumber.addEventListener('click', function () {
                currentPage = i;
                renderTable(data, currentPage);
                renderPagination(data);
            });
            pageNumbersContainer.appendChild(pageNumber);
        }
    }

    function handlePagination() {
        document.getElementById('prevPage').addEventListener('click', function () {
            if (currentPage > 1) {
                currentPage--;
                renderTable(data, currentPage);
                renderPagination(data);
            }
        });
        document.getElementById('nextPage').addEventListener('click', function () {
            const totalPages = Math.ceil(data.length / rowsPerPage);
            if (currentPage < totalPages) {
                currentPage++;
                renderTable(data, currentPage);
                renderPagination(data);
            }
        });
    }

    renderTable(data, currentPage);
    renderPagination(data);
    handlePagination();
});


document.getElementById('logoutButton').addEventListener('click', function() {
    localStorage.removeItem('jwtToken');
    window.location.href = 'index.html';
});