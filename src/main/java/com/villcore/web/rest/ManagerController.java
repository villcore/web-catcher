package com.villcore.web.rest;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@EnableAutoConfiguration
public class ManagerController {
    @RequestMapping("/manage/hello")
    public String hello() {
        return "Hello World! " + new Date().toString();
    }
}
