package com.eazybytes.eazyschool.controller;

import com.eazybytes.eazyschool.model.Course;
import com.eazybytes.eazyschool.model.EazyClass;
import com.eazybytes.eazyschool.model.Person;
import com.eazybytes.eazyschool.repository.CourseRepository;
import com.eazybytes.eazyschool.repository.EazyClassRepository;
import com.eazybytes.eazyschool.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {AdminController.class})
class AdminControllerTest {

    List<EazyClass> eazyClassesMock;

    List<Course> eazyCoursesMock;

    Set<Person> studentsMock;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    EazyClassRepository eazyClassRepoMock;

    @MockBean
    PersonRepository personRepoMock;

    @MockBean
    CourseRepository courseRepoMock;

    @BeforeEach
    void setUp() {
        // Setting the Mock Classes
        EazyClass class1 = new EazyClass();
        class1.setClassId(1);
        class1.setName("Music");

        EazyClass class2 = new EazyClass();
        class2.setClassId(2);
        class2.setName("Java");

        eazyClassesMock = List.of(class1,class2);

        // Setting the Mock Students
        Person studentMock1 = new Person();
        studentMock1.setPersonId(1);
        studentMock1.setName("Student1");
        studentMock1.setEmail("student1@gmail.com");


        Person studentMock2 = new Person();
        studentMock2.setPersonId(2);
        studentMock2.setName("Student2");
        studentMock2.setEmail("student2@gmail.com");

        studentsMock = Set.of(studentMock1,studentMock2);

        // Setting the Mock Courses
        Course course1 = new Course();
        course1.setCourseId(1);
        course1.setName("Music");
        course1.setFees("30€");

        Course course2 = new Course();
        course2.setCourseId(2);
        course2.setName("Yoga");
        course2.setFees("25€");

        eazyCoursesMock = List.of(course1, course2);

    }

    @Test
    @WithMockUser(username = "Mock Admin",roles = {"ADMIN"})
    void displayClasses() throws Exception {
        // Given
        given(eazyClassRepoMock.findAll()).willReturn(eazyClassesMock);

        mockMvc.perform(get("/admin/displayClasses"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("eazyClass"))
                .andExpect(view().name("classes.html"))
                .andExpect(content().string(containsString("EazySchool Class Details")))
                .andExpect(content().string(containsString("Music")))
                .andExpect(content().string(containsString("Java")))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "Mock Admin",roles = {"ADMIN"})
    void createClass() throws Exception {
        EazyClass aClass = new EazyClass();
        aClass.setName("A Class");

        MultiValueMap<String,String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("name",aClass.getName());

        mockMvc.perform(post("/admin/addNewClass").with(csrf()).params(multiValueMap))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/admin/displayClasses"))
                .andDo(print());

        verify(eazyClassRepoMock).save(any());
    }

    @Test
    @WithMockUser(username = "Mock Admin", roles = {"ADMIN"})
    void deleteClassWithStudents() throws Exception {

        EazyClass eazyClassMock = eazyClassesMock.get(0);
        eazyClassMock.setPersons(studentsMock);

        given(eazyClassRepoMock.findById(anyInt())).willReturn(Optional.of(eazyClassMock));

        mockMvc.perform(get("/admin/deleteClass?id={id}",1))
                .andExpect(status().is3xxRedirection())
                .andExpect(content().string(not(containsString("Look like you're lost"))))
                .andExpect(view().name("redirect:/admin/displayClasses"))
                .andDo(print());

        verify(personRepoMock,times(2)).save(any());
        verify(eazyClassRepoMock).deleteById(1);
    }

    @Test
    @WithMockUser(username = "Mock Admin", roles = {"ADMIN"})
    void deleteClassWithoutStudents() throws Exception {

        EazyClass eazyClassMock = eazyClassesMock.get(0);
        // Looks like JPA is setting the Set<> of persons
        // in the real Application to an empty Set for example new HashSet()
        eazyClassMock.setPersons(new HashSet<>());

        given(eazyClassRepoMock.findById(anyInt())).willReturn(Optional.of(eazyClassMock));

        mockMvc.perform(get("/admin/deleteClass?id={id}",1))
                .andExpect(status().is3xxRedirection())
                .andExpect(content().string(not(containsString("Look like you're lost"))))
                .andExpect(view().name("redirect:/admin/displayClasses"))
                .andDo(print());

        verify(personRepoMock,times(0)).save(any());
        verify(eazyClassRepoMock).deleteById(1);
    }

    @Test
    void displayStudents() throws Exception {
        given(eazyClassRepoMock.findById(anyInt())).willReturn(Optional.of(eazyClassesMock.get(0)));
        mockMvc.perform(get("/admin/displayStudents?classId={id}",1).with(user("admin@eazyschool.com").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(content().string(not(containsString("oops..."))))
                .andDo(print());
    }

    @Test
    void addStudentsWithoutEmailEntered() throws Exception {
        mockMvc.perform(post("/admin/addStudent")
                        .with(user("Mock Admin").roles("ADMIN"))
                        .with(csrf())
                        .sessionAttr("eazyClass",eazyClassesMock.get(1)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/displayStudents?classId=2&error=true"))
                .andExpect(content().string(not(containsString("oops..."))))
                .andDo(print());
    }

    @Test
    void addStudentsEmailEntered() throws Exception {
        // Given
        Person student = studentsMock.stream().toList().get(0);

        EazyClass classMock = eazyClassesMock.get(0);

        MultiValueMap<String,String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("email", student.getEmail());

        given(personRepoMock.findByEmail(any())).willReturn(student);

        mockMvc.perform(post("/admin/addStudent")
                        .with(user("Mock Admin").roles("ADMIN"))
                        .with(csrf())
                        .sessionAttr("eazyClass",classMock)
                        .params(multiValueMap)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/displayStudents?classId=1"))
                .andExpect(content().string(not(containsString("oops..."))))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "Mock Admin",roles = {"ADMIN"})
    void deleteStudent() throws Exception {
        // Given
        List<Person> studentsMock = this.studentsMock.stream().toList();
        given(personRepoMock.findById(anyInt())).willReturn(Optional.of(studentsMock.get(0)));

        EazyClass classMock = eazyClassesMock.get(0);

        // When, Then
        mockMvc.perform(get("/admin/deleteStudent/?personId={id}",1)
                        .with(csrf())
                        .sessionAttr("eazyClass",classMock))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/displayStudents?classId=1"))
                .andDo(print());

        verify(eazyClassRepoMock).save(any());
    }

    @Test
    void displayCourses() throws Exception {
        // Given
        given(courseRepoMock.findAll(Sort.by("name").descending())).willReturn(eazyCoursesMock);

        // When and Then
        mockMvc.perform(get("/admin/displayCourses")
                        .with(user("Mock Admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("course"))
                .andExpect(model().attributeExists("courses"))
                .andExpect(view().name("courses_secure.html"))
                .andExpect(content().string(containsString("EazySchool Course Details<")))
                .andExpect(content().string(containsString("<td>Music</td>")))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "Mock Admin",roles = {"ADMIN"})
    void addNewCourse() throws Exception {

        // Given
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        Course course = eazyCoursesMock.get(0);
        multiValueMap.add("name", course.getName());
        multiValueMap.add("fees",course.getFees());

        // When Then
        mockMvc.perform((post("/admin/addNewCourse").with(csrf())).params(multiValueMap))
                .andExpect(status().is3xxRedirection())
                .andDo(print());

        verify(courseRepoMock).save(any());

    }

    @Test
    @WithMockUser(username = "Mock Admin",roles = {"ADMIN"})
    void viewStudent() throws Exception {
        // Given
        given(courseRepoMock.findById(anyInt())).willReturn(Optional.ofNullable(eazyCoursesMock.get(0)));
        mockMvc.perform(get("/admin/viewStudents").param("courseId","1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("course"))
                .andExpect(view().name("course_students.html"))
                .andExpect(content().string(not(containsString("oops..."))))
                .andDo(print());
    }

    @Test
    void addStudentToCourseNoEmailEntered() throws Exception {
        mockMvc.perform(post("/admin/addStudentToCourse")
                        .with(user("Mock ADMIN").roles("ADMIN"))
                        .with(csrf())
                        .sessionAttr("course",eazyCoursesMock.get(0)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/viewStudents?courseId=1&error=true"))
                .andDo(print());
    }

    @Test
    void addStudentToCourseWithEmailEntered() throws Exception {
        List<Person> students = studentsMock.stream().toList();
        Person foundStudent = students.get(0);

        Course sessionAttrCourse = eazyCoursesMock.get(0);

        given(personRepoMock.findByEmail(anyString())).willReturn(foundStudent);

        mockMvc.perform(post("/admin/addStudentToCourse")
                        .with(user("Mock ADMIN").roles("ADMIN"))
                        .with(csrf())
                        .sessionAttr("course", sessionAttrCourse)
                        .param("email", foundStudent.getEmail())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/viewStudents?courseId="+sessionAttrCourse.getCourseId()))
                .andDo(print());

        verify(personRepoMock).save(foundStudent);
    }

    @Test
    @WithMockUser(username = "Mock Admin",roles = {"ADMIN"})
    void deleteStudentFromCourse() throws Exception {
        // Given
        Person studentToDelete = studentsMock.stream().toList().get(0);
        Integer personId = studentToDelete.getPersonId();
        given(personRepoMock.findById(personId)).willReturn(Optional.of(studentToDelete));

        // When, Then
        mockMvc.perform(get("/admin/deleteStudentFromCourse?personId={id}", personId)
                        .with(csrf())
                        .sessionAttr("course",eazyCoursesMock.get(0)))
                .andExpect(status().is3xxRedirection())
                .andDo(print());

        verify(personRepoMock).save(any());
    }
}