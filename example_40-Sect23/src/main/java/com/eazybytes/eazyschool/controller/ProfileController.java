package com.eazybytes.eazyschool.controller;

import com.eazybytes.eazyschool.model.Address;
import com.eazybytes.eazyschool.model.Person;
import com.eazybytes.eazyschool.model.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class ProfileController {

    @GetMapping("/displayProfile")
    public String displayProfilePage(Model model, HttpSession httpSession) {
        Person loggedUser = (Person) httpSession.getAttribute("loggedUser");
        Profile profile = new Profile();
        profile.setName(loggedUser.getName());
        profile.setEmail(loggedUser.getEmail());
        profile.setMobileNumber(loggedUser.getMobileNumber());
        Address address = loggedUser.getAddress();
        if (loggedUser.getPersonId() > 0 && address != null){
            profile.setAddress1(address.getAddress1());
            profile.setCity(address.getCity());
            profile.setState(address.getState());
            profile.setZipCode(address.getZipCode());
        }
        model.addAttribute("profile",profile);
        return "profile.html";
    }

}
