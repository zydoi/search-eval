package com.litb.search.eval.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController {

    @ModelAttribute("page")
    public String module() {
        return "home";
    }
	
	@RequestMapping(value = {"/", "home"}, method = RequestMethod.GET)
	public String index(Principal principal) {
        return principal != null ? "home/homeSignedIn" : "home/homeNotSignedIn";
	}
}
