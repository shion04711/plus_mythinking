package com.u22.plus.webportal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.u22.plus.webportal.user.StudentLoginService;
import com.u22.plus.webportal.user.TeacherLoginService;


@Controller
public class PortalController {

  @Autowired
  private StudentLoginService studentLoginService;

  @Autowired
  private TeacherLoginService teacherLoginService;

  @GetMapping("/")
  public String index() {

    if (teacherLoginService.isLogin()) {
      return "teach/teachindex";
    }

    if (!studentLoginService.isLogin()) {
      return "student/login";
    }

    return "student/index";
  }
}
