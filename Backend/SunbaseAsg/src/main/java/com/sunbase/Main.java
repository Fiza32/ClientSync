package com.sunbase;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {

	public static void main(String[] args) {
		String jsonString = "{ \"name\": \"Md Faizan Raza\", \"marks\": \"343\" }";
		
		String name = parseNameFromJson(jsonString);
		
		System.out.println("Marks is: " + name);
	}

	
	private static String parseNameFromJson(String jsonString) {
		ObjectMapper objectMapper = new ObjectMapper();
		
		try {
			JsonNode rootNode = objectMapper.readTree(jsonString);
			return rootNode.path("marks").asText();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new RuntimeException("Failed to parse authentication response", e);
		}
	}
}
