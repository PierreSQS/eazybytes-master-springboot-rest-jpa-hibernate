package com.eazybytes.eazyschool.controller;

import com.eazybytes.eazyschool.model.Course;
import com.eazybytes.eazyschool.model.EazyClass;
import com.eazybytes.eazyschool.model.Person;
import com.eazybytes.eazyschool.repository.CourseRepository;
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
    private final CourseRepository courseRepo;

    public AdminController(EazyClassRepository eazyClassRepo, PersonRepository personRepo, CourseRepository courseRepo) {
        this.eazyClassRepo = eazyClassRepo;
        this.personRepo = personRepo;
        this.courseRepo = courseRepo;
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
        String errorMessage;
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

    @GetMapping("deleteStudent")
    public String deleteStudent(@RequestParam("personId") int personID, HttpSession httpSession) {

        EazyClass eazyClass = (EazyClass) httpSession.getAttribute(EAZY_CLASS_ATTR);

        Optional<Person> personToDeleteOpt = personRepo.findById(personID);
        personToDeleteOpt.ifPresent(person -> {
            Set<Person> persons = eazyClass.getPersons();
            // Necessary for the test which doesn't the students e.g. Set<Person>
            if (persons == null) {
                persons = new HashSet<>();
            }
            persons.remove(person);
            person.setEazyClass(null);
            eazyClassRepo.save(eazyClass);
        });

        return "redirect:/admin/displayStudents/?classId="+eazyClass.getClassId();
    }

    @GetMapping("displayCourses")
    public String displayCourses(Model model) {
        List<Course> coursesForStudent = courseRepo.findAll();
        model.addAttribute("courses",coursesForStudent);
        model.addAttribute("course",new Course());
        return "courses_secure.html";
    }

    @PostMapping("addNewCourse")
    public ModelAndView addNewCourse(Course course) {
        ModelAndView modelAndView = new ModelAndView("redirect:/admin/displayCourses");
        courseRepo.save(course);
        return modelAndView;
    }

    @GetMapping("viewStudents")
    public String viewStudent(Model model, @RequestParam("id") Integer courseID, HttpSession httpSession, String error) {
        String errorMessage;
        Optional<Course> foundCourseOpt = courseRepo.findById(courseID);
        foundCourseOpt.ifPresent(course -> {
            model.addAttribute("course",course);
            model.addAttribute("person",new Person());
            httpSession.setAttribute("course",course);
        });

        if (error != null) {
            errorMessage = "Invalid email entered";
            model.addAttribute("errorMessage",errorMessage);
        }

        return "course_students.html";
    }

    @PostMapping("addStudentToCourse")
    ModelAndView addStudentToCourse(Person person, HttpSession httpSession) {
        Course course = (Course) httpSession.getAttribute("course");
        Person foundStudent = personRepo.findByEmail(person.getEmail());
        if (foundStudent == null) {
            return new ModelAndView("redirect:/admin/viewStudents/?id="+course.getCourseId()+"&error=true");
        }

        // link the course to the student
        course.getPersons().add(foundStudent);
        // link the student to the course
        foundStudent.getCourses().add(course);

        // save the student in the DB
        // since we have the cascade methode CASCADE.PERSIST
        // also the course will be saved
        personRepo.save(foundStudent);

        return new ModelAndView("redirect:/admin/viewStudents/?id="+course.getCourseId());
    }

    @GetMapping("deleteStudentFromCourse")
    public String deleteStudentFromCourse(@RequestParam Integer personId, HttpSession httpSession) {
        Course course = (Course) httpSession.getAttribute("course");

        Optional<Person> foundStudentOpt = personRepo.findById(personId);
        foundStudentOpt.ifPresent(person -> {
            person.getCourses().remove(course);
            course.getPersons().remove(person);
            personRepo.save(person);
        });

        return "redirect:/admin/viewStudents?id="+course.getCourseId();
    }

}
