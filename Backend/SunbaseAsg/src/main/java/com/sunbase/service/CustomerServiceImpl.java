package com.sunbase.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.sunbase.models.Customer;
import com.sunbase.repository.CustomerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepo;

    @Override
    public void addACustomer(Customer customer) {
        customer.setUuid(UUID.randomUUID().toString());

        customerRepo.save(customer);
        System.out.println("Saved new customer with UUID: " + customer.getUuid()); // Debugging
    }

    
    @Override
    public Customer getCustomerById(String id) {
        return customerRepo.findById(id).orElse(null);
    }

    /**
     * Retrieves all customers with pagination and sorting.
     * 
     * @param pageNumber   the page number to retrieve
     * @param pageSize     the number of customers per page
     * @param sortBy       the field to sort by
     * @param sortDirection the direction of sorting (ASC/DESC)
     * @return a paginated list of customers
     */
    public Page<Customer> getAllCustomers(int pageNumber, int pageSize, String sortBy, String sortDirection) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, 
                Sort.by(Sort.Direction.fromString(sortDirection), sortBy));
        return customerRepo.findAll(pageable);
    }


    public void deleteCustomerById(String id) {
        customerRepo.deleteById(id);
    }

    /**
     * Searches for customers based on a specific field and value.
     * 
     * @param field the field to search by (e.g., firstName, city, email, phone)
     * @param value the value to search for
     * @return a list of customers matching the search criteria
     */
    @Override
    public List<Customer> searchCustomers(String field, String value) {
        switch (field) {
            case "firstName":
                return customerRepo.findByFirstName(value);
            case "city":
                return customerRepo.findByCity(value);
            case "email":
                return customerRepo.findByEmailContainingIgnoreCase(value);
            case "phone":
                return customerRepo.findByPhoneContainingIgnoreCase(value);
            default:
                return customerRepo.findAll();
        }
    }

    /**
     * Synchronizes customers by fetching data from an external API and updating
     * the local repository.
     * 
     * @param token the authentication token for the API
     */
    @Override
    public void syncCustomers(String token) {
        String url = "https://qa.sunbasedata.com/sunbase/portal/api/assignment.jsp?cmd=get_customer_list";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        ResponseEntity<Customer[]> response = restTemplate.exchange(
        		url, 
        		HttpMethod.GET, 
        		entity, 
        		Customer[].class
        		);

        if (response.getStatusCode() == HttpStatus.OK) {
            Customer[] customers = response.getBody();
            if (customers != null) {
                for (Customer customer : customers) {
                    System.out.println("Customer fetched from API: " + customer);
                    System.out.println("First Name ->> " + customer.getFirstName());
                    saveOrUpdateCustomer(customer);
                }
            }
        } else {
            throw new RuntimeException("Failed to fetch customers");
        }
    }

    
    public void saveOrUpdateCustomer(Customer customer) {
        Optional<Customer> existingCustomerOpt = customerRepo.findByUuid(customer.getUuid());
        if (existingCustomerOpt.isPresent()) {
            Customer existingCustomer = existingCustomerOpt.get();
            existingCustomer.setFirstName(customer.getFirstName());
            existingCustomer.setLastName(customer.getLastName());
            existingCustomer.setStreet(customer.getStreet());
            existingCustomer.setAddress(customer.getAddress());
            existingCustomer.setCity(customer.getCity());
            existingCustomer.setState(customer.getState());
            existingCustomer.setEmail(customer.getEmail());
            existingCustomer.setPhone(customer.getPhone());
            customerRepo.save(existingCustomer);
        } else {
            customerRepo.save(customer);
        }
    }

    
	@Override
	public void updateCustomer(String id, Customer customer) {
        Customer existingCustomer = customerRepo.findByUuid(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        existingCustomer.setFirstName(customer.getFirstName());
        existingCustomer.setLastName(customer.getLastName());
        existingCustomer.setStreet(customer.getStreet());
        existingCustomer.setAddress(customer.getAddress());
        existingCustomer.setCity(customer.getCity());
        existingCustomer.setState(customer.getState());
        existingCustomer.setEmail(customer.getEmail());
        existingCustomer.setPhone(customer.getPhone());

        customerRepo.save(existingCustomer);
	}
}
