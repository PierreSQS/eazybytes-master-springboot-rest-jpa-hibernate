package com.eazybytes.eazyschool.controller;

import com.eazybytes.eazyschool.model.EazyClass;
import com.eazybytes.eazyschool.repository.EazyClassRepository;
import com.eazybytes.eazyschool.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("admin")
public class AdminController {

    private final EazyClassRepository eazyClassRepo;
    private final PersonRepository personRepo;

    public AdminController(EazyClassRepository eazyClassRepo, PersonRepository personRepo) {
        this.eazyClassRepo = eazyClassRepo;
        this.personRepo = personRepo;
    }

    @GetMapping("displayClasses")
    public String displayClasses(Model model) {
        List<EazyClass> eazyClasses = eazyClassRepo.findAll();
        model.addAttribute("eazyClass",new EazyClass());
        model.addAttribute("eazyClasses",eazyClasses);
        return "classes.html";
    }

    @PostMapping("addNewClass")
    public String createClass(@ModelAttribute EazyClass eazyClass) {
        eazyClassRepo.save(eazyClass);
        return "redirect:/admin/displayClasses";
    }
}
