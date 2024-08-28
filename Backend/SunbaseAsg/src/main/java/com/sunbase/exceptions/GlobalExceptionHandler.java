package com.sunbase.exceptions;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(BadCredentialException.class)
	public ResponseEntity<MyErrorDetails> badCredentialExceptionHandler(Exception e, WebRequest req){
		MyErrorDetails myError= new MyErrorDetails();
		myError.setTimeStamp(LocalDateTime.now());
		myError.setMessage(e.getMessage());
		myError.setDetails(req.getDescription(false));
		
		return new ResponseEntity<>(myError, HttpStatus.BAD_REQUEST);
	}
	
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<MyErrorDetails> gloabalHandler(Exception e, WebRequest req){
		MyErrorDetails myError= new MyErrorDetails();
		myError.setTimeStamp(LocalDateTime.now());
		myError.setMessage(e.getMessage());
		myError.setDetails(req.getDescription(false));
		
		return new ResponseEntity<>(myError, HttpStatus.BAD_REQUEST);
	}
}
