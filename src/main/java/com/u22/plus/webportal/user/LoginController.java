package com.u22.plus.webportal.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @Autowired
    private LoginServive loginServive;

    @GetMapping("/login")
    public String getLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String login(Model model, @RequestParam(name = "user_id") String userId,
            @RequestParam(name = "password") String password) {
        if (!loginServive.login(userId, password)) {
            model.addAttribute("errormessage", "ユーザIDまたはパスワードが違います。");
            return "student/login";
        }
        return "redirect:/";
    }
    @GetMapping("/logout")
    public String logout() {
        loginServive.logout();
        return "student/login";
    }
}
