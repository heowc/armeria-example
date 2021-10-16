package com.example;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Callable;

@RestController
public class IndexController {

    @GetMapping("/sync")
    public String sync() {
        return "Hello, Armeria!";
    }

    @GetMapping("/async")
    public Callable<String> async() {
        return () -> "Hello, Armeria! (async)";
    }
}