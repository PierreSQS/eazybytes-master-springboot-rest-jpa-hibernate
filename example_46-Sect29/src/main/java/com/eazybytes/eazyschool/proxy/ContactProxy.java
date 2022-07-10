package com.eazybytes.eazyschool.proxy;

import com.eazybytes.eazyschool.config.ProjectConfiguration;
import com.eazybytes.eazyschool.model.Contact;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "contact", url = "http://localhost:8080/api/contact",
    configuration = ProjectConfiguration.class)
public interface ContactProxy {

    @GetMapping("/getMessagesByStatus")
    @Headers(value = "Content-Type: application/json")
    List<Contact> getMessagesByStatus(@RequestParam String status);

}
