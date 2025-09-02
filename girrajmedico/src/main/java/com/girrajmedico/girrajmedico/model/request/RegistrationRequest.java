package com.girrajmedico.girrajmedico.model.request;

//RegistrationRequest.java (DTO for registration)
public class RegistrationRequest {
 private String username;
 private String password;
 private boolean enabled;
 private String email;
public boolean isEnabled() {
	return enabled;
}

public void setEnabled(boolean enabled) {
	this.enabled = enabled;
}

public String getEmail() {
	return email;
}

public void setEmail(String email) {
	this.email = email;
}

public long getMobileNumber() {
	return mobileNumber;
}

public void setMobileNumber(long mobileNumber) {
	this.mobileNumber = mobileNumber;
}

private long mobileNumber ; 
 // Constructors, getters, setters...

 public RegistrationRequest(){}

 public RegistrationRequest(String username, String password){
     this.username = username;
     this.password = password;
 }

 public String getUsername() {
     return username;
 }

 public void setUsername(String username) {
     this.username = username;
 }

 public String getPassword() {
     return password;
 }

 public void setPassword(String password) {
     this.password = password;
 }
}
