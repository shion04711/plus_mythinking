package com.u22.plus.webportal.mondai;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MondaiController {

    @GetMapping("/mondai")
    public String GetMondai() {
        return "mondai/input";
    }

    @PostMapping("/mondai")
    public String PostMondai() {
        return "index";
    }
}
