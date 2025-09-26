package com.girrajmedico.girrajmedico.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.girrajmedico.girrajmedico.model.dao.User;
import com.girrajmedico.girrajmedico.model.request.RegistrationRequest;
import com.girrajmedico.girrajmedico.service.UserService;

@CrossOrigin(origins = "*") // Allow all origins// Allow requests from React
@RestController
@RequestMapping("/user")
public class UserController {
	
	 @Autowired
	    private UserService userService;

	
	@GetMapping("/loggeduser")
    public ResponseEntity<User> getLoginUser() {
    	if(userService.getLoggedInUser()==null) {
    		ResponseEntity.status(HttpStatus.BAD_REQUEST).body("not login yet");
    	}
    	 return ResponseEntity.ok(userService.getLoggedInUser());
        }
	
	@PutMapping("/updateUser/{id}") // Correct mapping with path variable
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody RegistrationRequest user) {

        boolean b = userService.updateUser(id, user);
        if (b) {
            return ResponseEntity.ok("Update Complete");
        }
        return ResponseEntity.ok("Update Err");
    }
}

