package com.example.podscaling;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PodController {

    @GetMapping("/test")
    public String getResponse() {
        return "test";
    }
}
