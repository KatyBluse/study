package com.study.studymarket.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @RequestMapping(value = "/sendMsg", method = {RequestMethod.GET, RequestMethod.POST})
    public String sendMsg(){
        return "test";
    }
}
