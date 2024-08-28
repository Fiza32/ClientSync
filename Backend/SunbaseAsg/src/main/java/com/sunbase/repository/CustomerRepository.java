package com.sunbase.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sunbase.models.Customer;


@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
	List<Customer> findByFirstName(String firstName);

    List<Customer> findByCity(String city);

    List<Customer> findByEmailContainingIgnoreCase(String email);

    List<Customer> findByPhoneContainingIgnoreCase(String phone);

    Optional<Customer> findByUuid(String uuid);
}
