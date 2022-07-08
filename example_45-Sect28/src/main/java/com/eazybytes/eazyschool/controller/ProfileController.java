package com.eazybytes.eazyschool.controller;

import com.eazybytes.eazyschool.model.Address;
import com.eazybytes.eazyschool.model.Person;
import com.eazybytes.eazyschool.model.Profile;
import com.eazybytes.eazyschool.repository.PersonRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class ProfileController {

    public static final String LOGGED_USER = "loggedUser";
    private final PersonRepository personRepo;

    public ProfileController(PersonRepository personRepo) {
        this.personRepo = personRepo;
    }

    @GetMapping("/displayProfile")
    public String displayProfilePage(Model model, HttpSession httpSession) {
        Person loggedUser = (Person) httpSession.getAttribute(LOGGED_USER);
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

    @PostMapping("/updateProfile")
    public String updateProfilePage(@Valid @ModelAttribute Profile profile, Errors errors, HttpSession httpSession){
        if (errors.hasErrors()) {
            return "profile.html";
        }

        Person loggedUser = (Person) httpSession.getAttribute(LOGGED_USER);
        loggedUser.setName(profile.getName());
        loggedUser.setEmail(profile.getEmail());
        loggedUser.setMobileNumber(profile.getMobileNumber());
        Address address = loggedUser.getAddress();
        if (address == null) {
            address = new Address();
        }

        // update the Address
        address.setAddress1(profile.getAddress1());
        address.setAddress2(profile.getAddress2());
        address.setCity(profile.getCity());
        address.setState(profile.getState());
        address.setZipCode(profile.getZipCode());

        // reassign the updated address
        loggedUser.setAddress(address);

        // update the loggedInUser's Address
        personRepo.save(loggedUser);

        // set the session Attribute to keep the data on Form
        httpSession.setAttribute(LOGGED_USER,loggedUser);

        return "redirect:/displayProfile";
    }

}
