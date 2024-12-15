package com.proglang.fap.demo.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CatchAllController {

    @RequestMapping("/**")
    public String handleNotFound() {
        return "404 - Endpoint not found";
    }
}
