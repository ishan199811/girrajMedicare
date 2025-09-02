package com.girrajmedico.girrajmedico;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageCallingController {
	
	
	@GetMapping("/")
	public String getNavbar() {
        return "index"; // Returns the navbar.html template
    }
	
	@GetMapping("/productlist")
	public String getpropertpage() {
        return "fragment/property"; // Returns the navbar.html template
    }
	@GetMapping("/addproperty")
	public String getaddProperty() {
        return "addproperty"; // Returns the navbar.html template
    }
	
	@GetMapping("/ad")
	public String getadminpage() {
        return "sidebar"; // Returns the navbar.html template
    }
	
	@GetMapping("/registration")
	public String getRegisternpage() {
        return "fragment/registration"; // Returns the navbar.html template
    }
	
	@GetMapping("/userdetail")
	public String getuseDetail() {
        return "userdetail"; // Returns the navbar.html template
    }
	
	@GetMapping("/contact")
	public String getcontactpage() {
        return "contact"; // Returns the navbar.html template
    }
	@GetMapping("/service")
	public String getservicepage() {
        return "service"; // Returns the navbar.html template
    }
	
	
	

}
