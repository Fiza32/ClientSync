package com.sunbase.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sunbase.exceptions.BadCredentialException;
import com.sunbase.models.User;
import com.sunbase.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    
    private final AuthenticationManager manager;
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    
    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    /**
     * Adds a new user to the repository with an encoded password.
     * 
     * @param user the user to be added
     * @return the saved user
     */
    @Override
    public User addUsers(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    /**
     * Authenticates a user with the given email and password.
     * 
     * @param email the email of the user
     * @param password the password of the user
     * @throws BadCredentialException if the authentication fails
     */
    public void doAuthenticate(String email, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
        
        try {
            manager.authenticate(authenticationToken);
        } catch (Exception e) {
            logger.error("Authentication failed for user: {}", email, e);
            throw new BadCredentialException("Invalid Username or Password!");
        }
    }
}
