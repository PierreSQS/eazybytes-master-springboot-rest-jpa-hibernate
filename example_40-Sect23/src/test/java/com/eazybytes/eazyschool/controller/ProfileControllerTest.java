package com.eazybytes.eazyschool.controller;

import com.eazybytes.eazyschool.constants.EazySchoolConstants;
import com.eazybytes.eazyschool.model.Address;
import com.eazybytes.eazyschool.model.Person;
import com.eazybytes.eazyschool.model.Roles;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProfileController.class)
class ProfileControllerTest {

    // Person to update
    private Person personToUpdate;

    // Session Map for the Request
    private Map<String,Object> stringPersonMap = new HashMap<>();

    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    void setUp() {

        // Role to set for a Person
        Roles roles = new Roles();
        roles.setRoleName(EazySchoolConstants.STUDENT_ROLE);

        // Address to set for a Person
        Address address = new Address();
        address.setAddress1("Rue des Manguiers");
        address.setCity("Douala");
        address.setState("Wouri");
        address.setZipCode("12345");

        personToUpdate = new Person();
        personToUpdate.setPersonId(1);
        personToUpdate.setName("Pierrot");
        personToUpdate.setEmail("pierrot@gmail.com");
        personToUpdate.setConfirmEmail("pierrot@gmail.com");
        personToUpdate.setMobileNumber("1234567890");
        personToUpdate.setPwd("galerien");
        personToUpdate.setConfirmPwd("galerien");
        personToUpdate.setRoles(roles);
        personToUpdate.setAddress(address);

        stringPersonMap = new HashMap<>();

        Person personMock = new Person();
        personMock.setPersonId(1);
        personMock.setName("Mock User");
        personMock.setMobileNumber("1111111111");

        // Sets the Session Attribute for the Request
        stringPersonMap.put("loggedUser",personMock);
    }
    @Test
    void displayProfilePage() throws Exception {

        mockMvc.perform(get("/displayProfile").sessionAttrs(stringPersonMap)
                        .with(user("Mock User")))
                .andExpect(status().isOk())
                .andExpect(model().attributeDoesNotExist("errormsg"))
                .andExpect(view().name("profile.html"))
                .andExpect(content().string(containsString("My Profile")))
                .andExpect(content().string(not((containsString("oops...")))))
                .andDo(print());
    }

    @Test
    void updateProfilePage() throws Exception {
        MultiValueMap<String,String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("name", personToUpdate.getName());
        multiValueMap.add("mobileNumber", personToUpdate.getMobileNumber());
        multiValueMap.add("email", personToUpdate.getEmail());
        multiValueMap.add("confirmEmail", personToUpdate.getConfirmEmail());
        multiValueMap.add("pwd", personToUpdate.getPwd());
        multiValueMap.add("confirmPwd", personToUpdate.getConfirmPwd());
        multiValueMap.add("address1",personToUpdate.getAddress().getAddress1());
        multiValueMap.add("city",personToUpdate.getAddress().getCity());
        multiValueMap.add("state",personToUpdate.getAddress().getState());
        multiValueMap.add("zipCode",personToUpdate.getAddress().getZipCode());

        mockMvc.perform(post("/updateProfile").sessionAttrs(stringPersonMap)
                        .with(csrf())
                        .with(user("Mock User"))
                        .params(multiValueMap))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().attributeDoesNotExist("errormsg"))
                .andExpect(view().name("redirect:/displayProfile"))
                .andDo(print());
    }

}