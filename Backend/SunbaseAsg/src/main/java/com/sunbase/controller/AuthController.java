package com.sunbase.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sunbase.jwt.JwtHelper;
import com.sunbase.models.JwtRequest;
import com.sunbase.models.JwtResponse;
import com.sunbase.models.User;
import com.sunbase.service.UserServiceImpl;

import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sunbase/auth")
public class AuthController {
	private final UserServiceImpl userService;
	private final UserDetailsService userDetailsService;
	private final JwtHelper helper;
	

	@PostMapping("/register")
	public ResponseEntity<User> registerUser(@RequestBody User user){
		User createdUser = userService.addUsers(user);
		
		return new ResponseEntity<User>(createdUser, HttpStatus.CREATED);
	}
	
	@PostMapping("/login")
	public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request){
		userService.doAuthenticate(request.getEmail(), request.getPass());
		
		UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
		String token = helper.generateToken(userDetails);
		
		JwtResponse response = JwtResponse.builder()
				.jwtToken(token)
				.username(userDetails.getUsername()).build();
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PostMapping("/verify-token")
	public ResponseEntity<?> verifyToken(@RequestBody Map<String, String> tokenMap) {
	    String token = tokenMap.get("token");
	    try {
	        String username = helper.getUsernameFromToken(token);
	        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

	        boolean isValid = helper.validateToken(token, userDetails);
	        if (isValid) {
	            return ResponseEntity.ok().build();
	        } else {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	        }
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	    }
	}
}
