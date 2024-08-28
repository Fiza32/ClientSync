package com.sunbase;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class NotesAuthService {
/*
 * Call an external API (with the help of RestTemplate)
 * using Login Credentials.
 * 
 * It must return a token.
 */
	/*
	 * Private --> The variable URL can only be accessed within the Notes only. Other classes can't see or use it directly.
	 * 
	 * static --> The variable URL belongs to the class itself, not to any specific object created from that class. You can think of it as a shared variable that doesn't change for different instances (objects) of the class. Because it's static, you don't need to create an instance (an object) of the class to use this variable. It can be accessed directly using the class name.
	 * 
	 * final --> This means that the value of the variable URL cannot be changed once it has been set. It’s like saying, "This is the final value, and it will stay this way throughout the program."
	 * 
	 * String --> This is the data type of the variable URL. A String in Java is a sequence of characters, like a word or a sentence. It’s used to store text.
	 * 
	 * URL --> This is the name of the variable. It’s a label you can use to refer to the value.
	 * 
	 * But WHY___________???
	 * 'final' -->> By making it final, you ensure that the URL won't accidentally change somewhere else in your code. It's always the same. (Consistency)
	 * 
	 * 'static' -->> By making it static, you can use the same URL across different methods or instances without creating multiple copies. (Reusability)
	 * 
	 * 'private' -->> By making it private, you control access to this URL. Only the class that defines it can use it directly, which helps to protect it from unwanted changes. (Encapsulation)
	 */
	private static final String URL = "https://qa.sunbasedata.com/sunbase/portal/api/assignment_auth.jsp";
	
	
	/*
	 * authenticateAndGetToken -->> The purpose of this method is to send a request to a web server (an API) to log in with a username and password, and then get a 'token' in return.
	 * 
	 * public -->> This means the method can be accessed from outside the class it belongs to.
	 * String -->> This is the return type of the method. The method will return a String, which is the token in this case.
	 * authenticateAndGetToken -->> This is the name of the method.
	 * 
	 * HttpHeaders -->> This is a class used to set headers for your request. Headers are like label on a package that tell the server what to expect.
	 * `headers.setContentType(MediaType.APPLICATION_JSON)` -->> This line sets the type of the data you're sending to the server as JSON (JavaScript Object Notation), which is a common format for sending data over the web.
	 * 
	 * 
	 */
	public String authenticateAndGetToken() {
		/*
		 * RestTemplate -->> It is a class provided by the Spring that helps you make HTTP requests (like GET, POST, etc..) to a web server.
		 * new RestTemplate() -->> This creates a new object of RestTemplate, which you can use to send requests.
		 */
		RestTemplate restTemplate = new RestTemplate();
		
		/* Creating Request Headers
		 * 
		 * HttpHeaders -->> This is a class used to set headers for your request. Headers are like label on a package that tell the server what to expect.
		 * `headers.setContentType(MediaType.APPLICATION_JSON)` -->> This line sets the type of the data you're sending to the server as JSON (JavaScript Object Notation), which is a common format for sending data over the web.
		 */
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		/* Creating Request Body
		 * 
		 * A Collection that holds the key-value pairs, like a dictionary. Here the keys are "login_id" and "password", and their values are the actual login credentials.
		 * 
		 * 
		 */
		Map<String, String> body = new HashMap<>();
		body.put("login_id", "test@sunbasedata.com");
		body.put("password", "Test@123");
		
		/*
		 * HttpEntity -->> This class is used to combine the headers and the body of your request into one object ('reqEntity'), which you'll send to the server.
		 */
		// Combine the header and body
		HttpEntity<Map<String, String>> reqEntity = new HttpEntity<>(body, headers);
		
		/* Sending the Request and Getting the Response:
		 * 
		 * restTemplate.exchange(...) -->> This sends the request to the server.
		 * URL -> This is the web address where you're sending the request.
		 * HttpMethod.POST -> This specifies that you're making a POST request (sending data to the server).
		 * reqEntity -> This is the object that contains your request's headers and body.
		 * String.class -> This tells the method that you expect the server to return a response in the form of a `String`.
		 * responseEntity --> This stores the server's response, including the status and the body (which will contain the token)
		 * 
		 * 
		 */
		ResponseEntity<String> responseEntity = restTemplate.exchange(
				URL, 
				HttpMethod.POST, 
				reqEntity, 
				String.class
		);
		
		/* Extracting the Token:
		 * 
		 * `responseEntity.getBody()` -->> This gets the actual content of the server's response, which should contain the token.
		 * 
		 */
		String token = parseTokenFromResponse(responseEntity.getBody());
		return token;
	}

	private String parseTokenFromResponse(String body) {
		/*
		 * ObjectMapper: This is a class provided by the Jackson library, commonly used for converting between Java objects and JSON (JavaScript Object Notation).
		 * 
		 * new ObjectMapper(): This creates a new instance of the ObjectMapper class, which you'll use to work with JSON data.
		 */
		ObjectMapper objectMapper = new ObjectMapper();
		
		try {
			/*
			 * objectMapper.readTree(body): This line tells the ObjectMapper to parse the body string (which is expected to be in JSON format) and convert it into a tree structure of JsonNode objects.
			 * 
			 * JsonNode rootNode: The result of parsing the JSON is stored in rootNode, which represents the entire JSON data structure.
			 */
			JsonNode rootNode = objectMapper.readTree(body);
			return rootNode.path("access_token").asText();
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to parse authentication response", e);
		}
	}
	

	/*
	 * Sure! Let's take a closer look at the `HttpHeaders` class in Spring and how it works. 

	### Overview of `HttpHeaders`

		`HttpHeaders` is a Spring class that represents HTTP request and response headers. It provides a convenient way to work with headers in your HTTP requests or responses, allowing you to add, remove, and manipulate headers easily.

	### Why Do We Need `HttpHeaders`?

		In HTTP communication, headers are used to convey metadata about the request or response. This metadata can include things like:
		
		- **Content-Type**: The type of data being sent, like `application/json` or `text/html`.
		- **Authorization**: Credentials or tokens for authentication, like `Bearer <token>`.
		- **Accept**: The type of data the client expects in the response, like `application/json`.
		- **User-Agent**: Information about the client making the request.
		- **Cache-Control**: Instructions on how the response should be cached.
		
		Using the `HttpHeaders` class, you can easily manage these headers when building requests.

	### Creating an `HttpHeaders` Instance

		To create an `HttpHeaders` instance, you simply instantiate it like any other object:
		
		```java
		HttpHeaders headers = new HttpHeaders();
		```
		
		This creates an empty set of headers, which you can then populate with the values you need.

	### Common Methods in `HttpHeaders`

	Here are some commonly used methods provided by the `HttpHeaders` class:
		
		1. **Setting Content-Type**: 
		   - To specify the type of content you're sending:
		   ```java
		   headers.setContentType(MediaType.APPLICATION_JSON);
		   ```
		   This sets the `Content-Type` header to `application/json`, indicating that the request body is in JSON format.
		
		2. **Setting Authorization**: 
		   - To add an authorization token:
		   ```java
		   headers.setBearerAuth("your_token_here");
		   ```
		   This sets the `Authorization` header to `Bearer your_token_here`, which is commonly used for passing JWT tokens.
		
		3. **Adding Custom Headers**:
		   - To add any custom header:
		   ```java
		   headers.add("Custom-Header", "value");
		   ```
		   This adds a header called `Custom-Header` with the value `"value"`.
		
		4. **Setting Multiple Accept Types**:
		   - To specify the types of responses your client can accept:
		   ```java
		   headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML));
		   ```
		   This sets the `Accept` header to accept both JSON and XML responses.
		
		5. **Setting Cache-Control**:
		   - To control how the response is cached:
		   ```java
		   headers.setCacheControl(CacheControl.noCache().getHeaderValue());
		   ```
		   This adds a `Cache-Control` header with the value `no-cache`, instructing intermediaries not to cache the response.

	### Using `HttpHeaders` in a Request

		Once you've configured your headers, you typically use them as part of an `HttpEntity` to send them in a request. Here’s an example:
		
		```java
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth("your_token_here");
		
		HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, headers);
		```
		
		In this code:
		
		- `jsonBody` is the body of your request (e.g., a JSON string).
		- `headers` are the HTTP headers you've configured.
		- `HttpEntity<String>` combines both the body and the headers into a single entity that you can pass to methods like `restTemplate.exchange()`.

	### Internals of `HttpHeaders`

		Internally, `HttpHeaders` extends `MultiValueMap<String, String>`, which means it can store multiple values for a single header key. This is useful for headers like `Accept` where multiple values can be specified.
		
		Example:
		
		```java
		headers.add("Accept", "application/json");
		headers.add("Accept", "application/xml");
		```
		
		This means that the `Accept` header in the request will have two values: `application/json` and `application/xml`.

	### Summary

		- **Purpose**: `HttpHeaders` is used to manage HTTP headers in Spring applications.
		- **Functionality**: It allows you to set standard headers (like `Content-Type`), custom headers, and handle multiple values for a single header key.
		- **Usage**: Typically, `HttpHeaders` is used with `HttpEntity` to send headers along with a request body in HTTP requests made by `RestTemplate` or `WebClient`.
		
			Understanding `HttpHeaders` is essential for interacting with RESTful APIs in Java, as headers often contain critical information like content types, authorization tokens, and caching directives.
	 */
	
	
	
	/*
	 * The `restTemplate.exchange()` method in Spring's `RestTemplate` class is one of the most versatile methods for making HTTP requests. It allows you to interact with an external RESTful API and perform various types of HTTP operations (such as GET, POST, PUT, DELETE). 
	 * Let's dive deeper into how it works and what makes it powerful.

	### Method Signature
		Here's a typical signature of the `exchange()` method:

		```java
		public <T> ResponseEntity<T> exchange(
		        String url,
		        HttpMethod method,
		        @Nullable HttpEntity<?> requestEntity,
		        Class<T> responseType,
		        Object... uriVariables)
		        throws RestClientException
		```

	### Explanation of Parameters

		1. **`String url`**: 
		   - This is the endpoint of the API you're calling. It could be a simple URL or a more complex one with query parameters.
		   - Example: `"https://api.example.com/resource"`
		
		2. **`HttpMethod method`**:
		   - This parameter defines the HTTP method you want to use for the request. It could be `GET`, `POST`, `PUT`, `DELETE`, etc.
		   - Example: `HttpMethod.POST`
		
		3. **`HttpEntity<?> requestEntity`**:
		   - This is an optional parameter that contains the headers and body of your request.
		   - Example: You might use `HttpEntity` to pass in JSON data (in the body) and the content type (in the headers).
		
		4. **`Class<T> responseType`**:
		   - This defines the type of the response you expect from the API.
		   - Example: If you're expecting a JSON response that should be mapped to a Java class `MyResponse`, you would use `MyResponse.class`.
		
		5. **`Object... uriVariables`**:
		   - This is used for URL path parameters. If your URL is a template (e.g., `"https://api.example.com/resource/{id}"`), you can pass the values here.
		   - Example: If the URL is `"https://api.example.com/resource/{id}"`, you can pass the actual `id` value as a URI variable.

	### Return Type

		- **`ResponseEntity<T>`**:
		  - The method returns a `ResponseEntity` object that wraps the HTTP response.
		  - `ResponseEntity` contains:
		    - The HTTP status code (`200 OK`, `404 Not Found`, etc.)
		    - The response body (which is of type `T`)
		    - The response headers

	### How It Works in Your Example

		Let's revisit your code example:
		```java
		ResponseEntity<String> responseEntity = restTemplate.exchange(
		    URL, 
		    HttpMethod.POST, 
		    reqEntity, 
		    String.class
		);
		```

	#### What’s Happening Here:

		1. **`URL`**: 
		   - This is the URL of the API you're trying to authenticate against.
		
		2. **`HttpMethod.POST`**: 
		   - You're using the POST method because you want to send data (like login credentials) to the server.
		
		3. **`reqEntity`**: 
		   - This contains the request headers (like Content-Type) and the body (the JSON with the login credentials).
		
		4. **`String.class`**: 
		   - You're expecting a response in the form of a `String`. This is because the API will return the response (like a token) as a plain string.
		
		5. **Return Value (`responseEntity`)**: 
		   - The method returns a `ResponseEntity<String>`, which contains:
		     - The HTTP status code (e.g., 200 for success).
		     - The response body, which should contain the token or any other data returned by the server.

	### Why Use `exchange()`?

		- **Versatility**: You can use it for any type of HTTP method.
		- **Flexibility**: It allows you to control every part of the HTTP request, including headers, body, and URL variables.
		- **Typed Responses**: You can specify the exact type of the response you expect, which simplifies parsing.

	### Example Use Cases

		1. **GET Request**:
		   - To fetch data from an API:
		   ```java
		   ResponseEntity<MyData[]> response = restTemplate.exchange(
		       "https://api.example.com/data",
		       HttpMethod.GET,
		       null,
		       MyData[].class
		   );
		   ```

		2. **POST Request with a Body**:
		   - To send data to an API:
		   ```java
		   HttpHeaders headers = new HttpHeaders();
		   headers.setContentType(MediaType.APPLICATION_JSON);
		
		   MyRequestBody body = new MyRequestBody("value1", "value2");
		   HttpEntity<MyRequestBody> requestEntity = new HttpEntity<>(body, headers);
		
		   ResponseEntity<MyResponse> response = restTemplate.exchange(
		       "https://api.example.com/data",
		       HttpMethod.POST,
		       requestEntity,
		       MyResponse.class
		   );
		   ```

		3. **DELETE Request**:
		   - To delete a resource:
		   ```java
		   ResponseEntity<Void> response = restTemplate.exchange(
		       "https://api.example.com/resource/{id}",
		       HttpMethod.DELETE,
		       null,
		       Void.class,
		       id
		   );
		   ```

	### Summary

		The `exchange()` method is a powerful way to make HTTP requests in Java with full control over the request and response. 
		It’s used when you need more flexibility than simpler methods like `getForObject` or `postForEntity`.
	 */
	
	
	/*
	 * `ResponseEntity` is a key class in Spring used to represent the entire HTTP response, including the status code, headers, and body. It is particularly useful in RESTful web services, where you need to have fine-grained control over the HTTP response returned to the client.

	### Overview of `ResponseEntity`

		`ResponseEntity` is a generic class that can hold any type of object in its body, making it flexible for returning different kinds of data. The class is typically used in controller methods to define the response that should be sent back to the client.

### Key Components of `ResponseEntity`

1. **Status Code**: This is the HTTP status code (e.g., `200 OK`, `404 Not Found`, `500 Internal Server Error`) that indicates the outcome of the request.

2. **Headers**: These are the HTTP headers that can be included in the response, such as `Content-Type`, `Authorization`, and `Cache-Control`.

3. **Body**: This is the content of the response, which can be any object, such as a string, JSON, XML, or even a file.

### Creating a `ResponseEntity`

There are several ways to create a `ResponseEntity`:

1. **Using `ResponseEntity.ok()`**:
   - This is used when you want to return a successful `200 OK` response along with a body.
   ```java
   @GetMapping("/hello")
   public ResponseEntity<String> sayHello() {
       return ResponseEntity.ok("Hello, World!");
   }
   ```
   In this example, the `sayHello()` method returns a `200 OK` response with the body "Hello, World!".

2. **Using `ResponseEntity.status()`**:
   - This allows you to set a custom HTTP status code.
   ```java
   @GetMapping("/notfound")
   public ResponseEntity<String> notFound() {
       return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Resource not found");
   }
   ```
   Here, the method returns a `404 Not Found` status with a custom message.

3. **Using `ResponseEntity.badRequest()`**:
   - This is used for a `400 Bad Request` response.
   ```java
   @PostMapping("/submit")
   public ResponseEntity<String> submitData(@RequestBody MyData data) {
       if (data.isValid()) {
           return ResponseEntity.ok("Data submitted successfully");
       } else {
           return ResponseEntity.badRequest().body("Invalid data");
       }
   }
   ```
   This example checks if the submitted data is valid. If not, it returns a `400 Bad Request` status.

4. **Using `ResponseEntity.noContent()`**:
   - This is used for a `204 No Content` response, which indicates that the request was successful, but there’s no content to return.
   ```java
   @DeleteMapping("/delete/{id}")
   public ResponseEntity<Void> deleteResource(@PathVariable Long id) {
       myService.delete(id);
       return ResponseEntity.noContent().build();
   }
   ```
   The `deleteResource()` method returns a `204 No Content` status after successfully deleting a resource.

### Setting Headers in `ResponseEntity`

You can also add headers to the response using the `headers()` method:

```java
@GetMapping("/custom-headers")
public ResponseEntity<String> customHeaders() {
    HttpHeaders headers = new HttpHeaders();
    headers.add("Custom-Header", "CustomValue");

    return ResponseEntity.ok()
            .headers(headers)
            .body("Response with custom headers");
}
```

In this example, the response includes a custom header named `Custom-Header`.

### Using `ResponseEntity` with Generics

`ResponseEntity` is a generic class, meaning it can hold different types of objects in its body. For instance:

- **ResponseEntity<String>**: The body is a string.
- **ResponseEntity<List<User>>**: The body is a list of `User` objects.
- **ResponseEntity<Void>**: There’s no body, useful for `204 No Content` responses.

### Typical Use Cases

- **Error Handling**: Return detailed error messages with appropriate status codes.
- **Custom Headers**: Include additional metadata in responses via headers.
- **File Downloads**: Serve files (e.g., PDFs, images) by returning `ResponseEntity<byte[]>` with appropriate headers for content type and disposition.

### Example: A Detailed `ResponseEntity` Usage

Here's a more comprehensive example:

```java
@GetMapping("/user/{id}")
public ResponseEntity<User> getUser(@PathVariable Long id) {
    User user = userService.findById(id);
    if (user == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    HttpHeaders headers = new HttpHeaders();
    headers.add("X-Custom-Header", "User-Data");

    return ResponseEntity.ok()
            .headers(headers)
            .body(user);
}
```

In this example:

- If the user is found, the method returns a `200 OK` status with the user object and a custom header.
- If the user is not found, it returns a `404 Not Found` status with no body.

### Summary

- **`ResponseEntity`** is a powerful class in Spring for representing HTTP responses.
- It provides fine-grained control over the HTTP status, headers, and body returned to the client.
- It can be easily used in controller methods to create different types of responses based on the situation.

Understanding `ResponseEntity` is crucial for building robust and flexible RESTful APIs in Spring, as it allows you to manage the entire HTTP response in a consistent and meaningful way.
	 */
}
