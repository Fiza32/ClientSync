package com.sunbase.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sunbase.models.User;

public interface UserRepository extends JpaRepository<User, String> {
	Optional<User> findByEmail(String email);
}
