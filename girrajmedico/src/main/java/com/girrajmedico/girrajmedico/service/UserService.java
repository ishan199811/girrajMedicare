package com.girrajmedico.girrajmedico.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.girrajmedico.girrajmedico.model.dao.Role;
import com.girrajmedico.girrajmedico.model.dao.User;
import com.girrajmedico.girrajmedico.model.request.RegistrationRequest;
import com.girrajmedico.girrajmedico.repository.RoleRepository;
import com.girrajmedico.girrajmedico.repository.UserRepository;






@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository; // Inject RoleRepository

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public boolean registerUser(RegistrationRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            return false; // Username already exists
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(encodedPassword);
        user.setEnabled(true);
        user.setEmail(request.getEmail());
        user.setMobileNumber(request.getMobileNumber());
        
        Set<Role> roles = new HashSet<>();
        Role defaultRole = roleRepository.findByName("USER"); // Fetch Role by name from database

        if (defaultRole == null) {
            // Handle case where "USER" role doesn't exist.
            // You might want to create the role here, or return an error.
            defaultRole = new Role();
            defaultRole.setName("USER");
            roleRepository.save(defaultRole); //create a new role.
        }

        roles.add(defaultRole);
        user.setRoles(roles);

        userRepository.save(user);
        return true;
    }
    
    
    public boolean registerDoctor(RegistrationRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            return false; // Username already exists
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(encodedPassword);
        user.setEnabled(true);
        user.setEmail(request.getEmail());
        user.setMobileNumber(request.getMobileNumber());
        Set<Role> roles = new HashSet<>();
        Role defaultRole = roleRepository.findByName("DOCTOR"); // Fetch Role by name from database

        if (defaultRole == null) {
            // Handle case where "USER" role doesn't exist.
            // You might want to create the role here, or return an error.
            defaultRole = new Role();
            defaultRole.setName("DOCTOR");
            roleRepository.save(defaultRole); //create a new role.
        }

        roles.add(defaultRole);
        user.setRoles(roles);

        userRepository.save(user);
        return true;
    }
    
    public boolean updateUser(Long id, RegistrationRequest request) {
        Optional<User> existingUserOptional = userRepository.findById(id);
        if (existingUserOptional.isEmpty()) {
            return false; // User not found
        }

        User existingUser = existingUserOptional.get();

        if (request.getUsername() != null && !request.getUsername().equals(existingUser.getUsername()) && userRepository.existsByUsername(request.getUsername())) {
            return false; // New username already exists
        }

        if (request.getUsername() != null) {
            existingUser.setUsername(request.getUsername());
        }
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(request.getPassword());
            existingUser.setPassword(encodedPassword);
        }
        if (request.getEmail() != null) {
            existingUser.setEmail(request.getEmail());
        }
      
            existingUser.setMobileNumber(request.getMobileNumber());
  userRepository.save(existingUser);
        return true;
    }
    
    public User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
           User user=userRepository.getUserByUsername(authentication.getName());
        	return user;
        }
        return null; // Return null if no user is logged in
    }
	public List<User> getAllUser() {
		// TODO Auto-generated method stub
	List<User> user=	userRepository.findAll().stream()
			.filter(e->e.getRoles().stream().anyMatch(role->role.getName().equals("USER"))).collect(Collectors.toList());
		return user;
	}
	
}