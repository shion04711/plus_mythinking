package com.u22.plus.webportal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.u22.plus.webportal.user.LoginServive;


@Controller
public class PortalController {

  @Autowired
  private LoginServive loginServive;

  @GetMapping("/")
  public String index() {
    if (!loginServive.isLogin()) {
      return "student/login";
    }
    return "student/index";
    }
}
