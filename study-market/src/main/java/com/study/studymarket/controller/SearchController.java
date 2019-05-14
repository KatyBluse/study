package com.study.studymarket.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/search")
public class SearchController {

    @PostMapping("findText")
    public void findText(String text) {

    }
}
