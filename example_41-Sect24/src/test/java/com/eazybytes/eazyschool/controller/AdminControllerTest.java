package com.eazybytes.eazyschool.controller;

import com.eazybytes.eazyschool.model.EazyClass;
import com.eazybytes.eazyschool.model.Person;
import com.eazybytes.eazyschool.repository.EazyClassRepository;
import com.eazybytes.eazyschool.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {AdminController.class})
class AdminControllerTest {

    List<EazyClass> eazyClassesMock;

    Set<Person> students;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    EazyClassRepository eazyClassRepoMock;

    @MockBean
    PersonRepository personRepoMock;

    @BeforeEach
    void setUp() {
        EazyClass class1 = new EazyClass();
        class1.setClassId(1);
        class1.setName("Music");

        EazyClass class2 = new EazyClass();
        class2.setClassId(2);
        class2.setName("Java");

        eazyClassesMock = List.of(class1,class2);

        Person student1 = new Person();
        student1.setName("Student1");

        Person student2 = new Person();
        student2.setName("Student2");

        students = Set.of(student1,student2);

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
        eazyClassMock.setPersons(students);

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
        Person student = students.stream().toList().get(0);
        student.setPersonId(1);
        student.setEmail("student1@gmail.com");

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
        List<Person> studentsMock = students.stream().toList();
        given(personRepoMock.findById(anyInt())).willReturn(Optional.of(studentsMock.get(0)));

        EazyClass classMock = eazyClassesMock.get(0);

        // When, Then
        mockMvc.perform(get("/admin/deleteStudent/?personId={id}",1)
                        .with(csrf())
                        .sessionAttr("eazyClass",classMock))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/displayStudents/?classId=1"))
                .andDo(print());

        verify(personRepoMock).deleteById(anyInt());
        verify(eazyClassRepoMock).save(any());
    }
}