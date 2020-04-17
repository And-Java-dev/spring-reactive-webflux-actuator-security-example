package com.example.springreactivewebfluxactuatorsecurityexample.reactive.authresolver;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.security.Principal;

@RestController
public class AuthResolverController {
    @GetMapping("/custom/welcome")
    public Mono<String> sayWelcomeCustomer(Mono<Principal> principalMono){
        return principalMono
                .map(Principal::getName)
                .map(name -> String.format("Welcome to our site, %s ", name));
    }

    @GetMapping("/employee/welcome")
    public Mono<String> sayWelcomeEmployee(Mono<Principal> principalMono) {
        return principalMono
                .map(Principal::getName)
                .map(name -> String.format("Welcome to our company, %s ", name));
    }
}
