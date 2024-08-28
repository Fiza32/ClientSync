package com.sunbase.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sunbase.models.Customer;
import com.sunbase.service.AuthService;
import com.sunbase.service.CustomerServiceImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sunbase")
public class CustomerController {
	
	private final CustomerServiceImpl customService;
	private final AuthService authService;


	@GetMapping("/all_customers")
	public ResponseEntity<Page<Customer>> getAllCustomers(
	    @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
	    @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
	    @RequestParam(value = "sortBy", defaultValue = "uuid", required = false) String sortBy,
	    @RequestParam(value = "sortDirection", defaultValue = "asc", required = false) String sortDirection) {

	    Page<Customer> page = customService.getAllCustomers(pageNumber, pageSize, sortBy, sortDirection);
	    return new ResponseEntity<>(page, HttpStatus.OK);
	}


	@PostMapping("/save_customer")
	public ResponseEntity<Customer> addCustomer(@Valid @RequestBody Customer customer) {
	    customService.addACustomer(customer);
	    return new ResponseEntity<>(customer, HttpStatus.CREATED);
	}
	
	
	@GetMapping("/{id}")
	public ResponseEntity<Customer> getACustomer(@PathVariable String id){
		Customer customer = customService.getCustomerById(id);
		
		return new ResponseEntity<>(customer, HttpStatus.OK);
	}


	@PutMapping("/editCustomer/{id}")
	public ResponseEntity<Void> updateCustomer(@PathVariable String id, @Valid @RequestBody Customer customer) {
	    customService.updateCustomer(id, customer);

	    return new ResponseEntity<>(HttpStatus.OK);
	}
	
	
	@DeleteMapping("/deleteCustomer/{id}")
	public ResponseEntity<Void> deleteCustomer(@PathVariable("id") String id) {
	    customService.deleteCustomerById(id); 
	    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	
	@GetMapping("/search_customers")
    public List<Customer> searchCustomers(
            @RequestParam(value = "field", required = false) String field,
            @RequestParam(value = "value", required = false) String value) {

        return customService.searchCustomers(field, value);
    }

	
	@PostMapping("/sync_customers")
    public ResponseEntity<String> syncCustomers() {
        try {
            String token = authService.authenticateAndGetToken();
            System.out.println("token: " + token);

            customService.syncCustomers(token);

            return ResponseEntity.ok("Customer synchronization completed successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to synchronize customers.");
        }
    }
	
}
