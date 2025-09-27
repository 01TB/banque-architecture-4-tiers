package com.front.banque_front.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/admin")
public class Dashboard {

    @GetMapping
    public String dashboard() {
        return "admin/home";
    }

    @GetMapping("/login")
    public String login() {
        return "admin/login";
    }

    @GetMapping("/register")
    public String register() {
        return "admin/register";
    }

    @GetMapping("/forgot-password")
    public String forgotPassword() {
        return "admin/forgot-password";
    }

    @GetMapping("/error-404")
    public String error404() {
        return "admin/error-404";
    }

    @GetMapping("/blank")
    public String blank() {
        return "admin/blank";
    }

    @GetMapping("/buttons")
    public String buttons() {
        return "admin/buttons";
    }

    @GetMapping("/cards")
    public String cards() {
        return "admin/cards";
    }

    @GetMapping("/charts")
    public String charts() {
        return "admin/charts";
    }

    @GetMapping("/tables")
    public String tables() {
        return "admin/tables";
    }

    @GetMapping("/utilities-animation")
    public String utilitiesAnimation() {
        return "admin/utilities-animation";
    }

    @GetMapping("/utilities-border")
    public String utilitiesBorder() {
        return "admin/utilities-border";
    }

    @GetMapping("/utilities-color")
    public String utilitiesColor() {
        return "admin/utilities-color";
    }

    @GetMapping("/utilities-other")
    public String utilitiesOther() {
        return "admin/utilities-other";
    }
}
