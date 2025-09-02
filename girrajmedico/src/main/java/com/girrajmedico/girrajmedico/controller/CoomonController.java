package com.girrajmedico.girrajmedico.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.girrajmedico.girrajmedico.compiler.CodeExecutionService;
import com.girrajmedico.girrajmedico.model.dao.User;
import com.girrajmedico.girrajmedico.model.request.RegistrationRequest;
import com.girrajmedico.girrajmedico.service.UserService;


@RestController
@RequestMapping("/api")
public class CoomonController {
	
	 @Autowired
	    private UserService userService;
	 
	 @Autowired
	 private  CodeExecutionService codeExecutionService;

	    
	    @PostMapping("/execute")
	    public ResponseEntity<String> executeCode(@RequestBody String javaCode) {
	        // It's crucial to ensure the provided code contains a public class with a main method
	        // for this simple executor to work directly.
	        // Example expected input:
	        // "public class MyClass { public static void main(String[] args) { System.out.println(\"Hello from API!\"); } }"
	        String result = codeExecutionService.executeJavaCode(javaCode);
	        return ResponseEntity.ok(result);
	    }
	 
	 
	 

	 @PostMapping("/register")
	    public ResponseEntity<String> register(@RequestBody RegistrationRequest request) {
	        boolean registered = userService.registerUser(request);
	        if (registered) {
	            return ResponseEntity.ok("User registered successfully");
	        } else {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already exists");
	        }
	    }
	    @PostMapping("/login")
	    public String login() {
	        return "login succesfull"; // Return the name of your login HTML file
	    }
	    @PostMapping("/docregister")
	    public ResponseEntity<String> docregister(@RequestBody RegistrationRequest request) {
	        boolean registered = userService.registerDoctor(request);
	        if (registered) {
	            return ResponseEntity.ok("Doctor registered successfully");
	        } else {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Doctor already exists");
	        }
	    }

		

		
		@GetMapping("/loggeduser")
	    public ResponseEntity<User> getLoginUser() {
	    	if(userService.getLoggedInUser()==null) {
	    		ResponseEntity.status(HttpStatus.BAD_REQUEST).body("not login yet");
	    	}
	    	 return ResponseEntity.ok(userService.getLoggedInUser());
	        }
}
