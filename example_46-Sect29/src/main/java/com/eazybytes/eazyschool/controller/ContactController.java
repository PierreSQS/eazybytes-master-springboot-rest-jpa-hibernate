package com.eazybytes.eazyschool.controller;

import com.eazybytes.eazyschool.model.Contact;
import com.eazybytes.eazyschool.model.Response;
import com.eazybytes.eazyschool.proxy.ContactProxy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
public class ContactController {

    private final ContactProxy contactProxy;
    private final RestTemplate restTemplate;
    private final WebClient webClient;

    public ContactController(ContactProxy contactProxy, RestTemplate restTemplate, WebClient webClient) {
        this.contactProxy = contactProxy;
        this.restTemplate = restTemplate;
        this.webClient = webClient;
    }

    @GetMapping("/getMessages")
    public List<Contact> getMessages(@RequestParam String status) {
        return contactProxy.getMessagesByStatus(status);
    }

    @PostMapping("saveMsg")
    public ResponseEntity<Response> saveMsg(@RequestBody Contact contact) {
        String url = "http://localhost:8080/api/contact/saveMsg";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("invocationFrom","RestTemplate");

        HttpEntity<Contact> httpEntity = new HttpEntity<>(contact, httpHeaders);
        return restTemplate.exchange(url, HttpMethod.POST,httpEntity,Response.class);
    }

    @PostMapping("saveMessage")
    public Mono<Response> saveMessage(@RequestBody Contact contact) {
        String url = "http://localhost:8080/api/contact/saveMsg";

        return webClient.post().uri(url)
                .header("invocationFrom","WebClient")
                .body(Mono.just(contact),Contact.class)
                .retrieve()
                .bodyToMono(Response.class);
    }

}
