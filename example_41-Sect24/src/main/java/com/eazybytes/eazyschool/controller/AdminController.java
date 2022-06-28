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

import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Controller
@RequestMapping("admin")
public class AdminController {

    public static final String EAZY_CLASS_ATTR = "eazyClass";

    private final EazyClassRepository eazyClassRepo;
    private final PersonRepository personRepo;

    public AdminController(EazyClassRepository eazyClassRepo, PersonRepository personRepo) {
        this.eazyClassRepo = eazyClassRepo;
        this.personRepo = personRepo;
    }

    @GetMapping("displayClasses")
    public String displayClasses(Model model) {
        List<EazyClass> eazyClasses = eazyClassRepo.findAll();
        model.addAttribute(EAZY_CLASS_ATTR,new EazyClass());
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
    public ModelAndView displayStudents(@RequestParam int classId, HttpSession httpSession,
                                        @RequestParam(required = false) String error) {
        String errorMessage = null;
        ModelAndView modelAndView = new ModelAndView("students.html");
        Optional<EazyClass> classByIDOpt = eazyClassRepo.findById(classId);
        classByIDOpt.ifPresent(eazyClass -> {
            modelAndView.addObject(EAZY_CLASS_ATTR, eazyClass);
            httpSession.setAttribute(EAZY_CLASS_ATTR, eazyClass);
        });
        modelAndView.addObject("person",new Person());

        if (error != null) {
            errorMessage = "Invalid email entered";
            modelAndView.addObject("errorMessage",errorMessage);

        }

        return modelAndView;
    }

    @PostMapping("addStudent")
    public String addStudents(Person student, HttpSession httpSession) {
        EazyClass eazyClass = (EazyClass) httpSession.getAttribute(EAZY_CLASS_ATTR);
        Person foundStudent = personRepo.findByEmail(student.getEmail());
        if (foundStudent == null) {
            return "redirect:/admin/displayStudents?classId="+eazyClass.getClassId()+"&error=true";
        }

        // add the class to the student
        foundStudent.setEazyClass(eazyClass);

        // add the student to the class
        Set<Person> students = eazyClass.getPersons();
        if (students == null) {
            students = new HashSet<>(); // necessary for the test, otherwise NPE will be thrown!!
        }

        students.add(foundStudent);

        // and save the class, thus automatically
        // the student because the cascade "Persist"
        eazyClassRepo.save(eazyClass);

        return "redirect:/admin/displayStudents?classId="+eazyClass.getClassId();
    }

}
