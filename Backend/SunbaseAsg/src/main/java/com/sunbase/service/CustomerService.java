package com.sunbase.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.sunbase.models.Customer;

public interface CustomerService {
	void addACustomer(Customer customer);
	Customer getCustomerById(String id);
	
    void updateCustomer(String id, Customer customer);
    
	public List<Customer> searchCustomers(String field, String value);
	void deleteCustomerById(String id);
	Page<Customer> getAllCustomers(int pageNum, int pageSize, String sortBy, String sortDirection);

	public void syncCustomers(String token);
	
	
}

