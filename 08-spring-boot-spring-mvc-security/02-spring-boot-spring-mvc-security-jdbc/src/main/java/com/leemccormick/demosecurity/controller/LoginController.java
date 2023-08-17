package com.leemccormick.demosecurity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    @GetMapping("/showMyLoginPage")
    public String showMyLoginPage() {
        // return "plain-login"; --> We use fancy-login for new style
        return "fancy-login";
    }

    // Add Request Mapping for /access-denied
    @GetMapping("/access-denied")
    public String showAccessAenied() {
        return "access-denied";
    }
}
