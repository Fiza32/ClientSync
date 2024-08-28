package com.sunbase.jwt;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.sunbase.exceptions.BadCredentialException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtHelper {

	public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;
	private String secretKey = generateSecretKey();
	
	//retrieve username from jwt token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }
    
    //retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }
    
    //Generic method to retrieve a specific claim from the JWT token.
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }
    
	//  Parses the JWT token and returns all claims.
	//for retrieveing any information from token we will need the secret key
	private Claims getAllClaimsFromToken(String token) {
	    return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
	}
	
	//check if the token has expired
	private Boolean isTokenExpired(String token) {
	    final Date expiration = getExpirationDateFromToken(token);
	    return expiration.before(new Date());
	}
	
	// Generates a JWT token for the specified UserDetails.
	public String generateToken(UserDetails userDetails) {
	    Map<String, Object> claims = new HashMap<>();
	    return doGenerateToken(claims, userDetails.getUsername());
	}
	
	// Builds the JWT token with specified claims, subject, and expiration.
	private String doGenerateToken(Map<String, Object> claims, String subject) {

        return Jwts.builder()
        		.setClaims(claims)
        		.setSubject(subject)
        		.setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    // Validates whether the JWT token is valid by comparing the username and checking for expiration.
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
	
	private String generateSecretKey() {
    	try {
            // Generate a secure random key of 64 bytes (512 bits)
            byte[] secretKey = new byte[64];
            SecureRandom secureRandom = SecureRandom.getInstanceStrong();
            secureRandom.nextBytes(secretKey);

            // Encode the key as a Base64 string
            return Base64.getEncoder().encodeToString(secretKey);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating secret key", e);
        }
    }
	
	
}
