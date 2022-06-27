package com.eazybytes.eazyschool.controller;

import com.eazybytes.eazyschool.model.EazyClass;
import com.eazybytes.eazyschool.model.Person;
import com.eazybytes.eazyschool.repository.EazyClassRepository;
import com.eazybytes.eazyschool.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

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

    @GetMapping("deleteClass")
    public String deleteClass(@RequestParam("id") int classId){
        Optional<EazyClass> classByIdOpt = eazyClassRepo.findById(classId);

        // This is an elegant alternative!
        classByIdOpt.ifPresent(eazyClass -> eazyClass.getPersons().forEach(person -> {
            person.setEazyClass(null); // Delete all Person, e.g. Student in the class
            personRepo.save(person);
        }));

        // Then delete the class
        log.info("########## deleteClass: deleting the class... #################");
        eazyClassRepo.deleteById(classId);

        return "redirect:/admin/displayClasses";
    }

    @GetMapping("displayStudents")
    public ModelAndView displayStudents(@RequestParam int classId) {
        ModelAndView modelAndView = new ModelAndView("students.html");
        Optional<EazyClass> classByIDOpt = eazyClassRepo.findById(classId);
        classByIDOpt.ifPresent(eazyClass -> modelAndView.addObject("eazyClass", eazyClass));
        modelAndView.addObject("person",new Person());
        return modelAndView;
    }

}
