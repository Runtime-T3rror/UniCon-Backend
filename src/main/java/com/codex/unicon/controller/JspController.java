package com.codex.unicon.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class JspController {
    @GetMapping("/set-password")
    public String getPassword() {
        return "set-password";
    }
}
