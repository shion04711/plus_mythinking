package com.u22.plus.webportal.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 生徒側のログイン・ログアウト・新規登録の画面遷移を担当するController。
 * 対応するThymeleafテンプレート: student/login.html, student/studentadd.html
 */
@Controller
public class StudentLoginController {

  @Autowired
  private StudentLoginService studentLoginService;

  @GetMapping("/studentlogin")
  public String getLogin() {
    return "student/login";
  }

  @PostMapping("/studentlogin")
  public String login(Model model,
      @RequestParam(name = "user_id") String userId,
      @RequestParam(name = "password") String password) {

    if (!studentLoginService.login(userId, password)) {
      model.addAttribute("errormessage", "ユーザIDまたはパスワードが違います。");
      return "student/login";
    }

    return "student/index";
  }

  @GetMapping("/studentlogout")
  public String logout() {
    studentLoginService.logout();
    return "student/login";
  }

  @GetMapping("/studentadd")
  public String getStudentAdd() {
    return "student/studentadd";
  }

  @PostMapping("/studentadd")
  public String postStudentAdd(Model model,
      @RequestParam(name = "course") String course,
      @RequestParam(name = "user_id") String userId,
      @RequestParam(name = "password") String password,
      @RequestParam(name = "password2") String password2,
      @RequestParam(name = "hyoji") String hyoji) {

    try {
      Integer courseId = parseCourseId(course);
      studentLoginService.register(userId, password, password2, hyoji, courseId);
    } catch (StudentRegistException e) {
      model.addAttribute("errormessage", e.getMessage());
      return "student/studentadd";
    } catch (NumberFormatException e) {
      model.addAttribute("errormessage", "コースIDの形式が不正です。");
      return "student/studentadd";
    }

    return "student/login";
  }

  private Integer parseCourseId(String course) {
    if (course == null || course.isBlank()) {
      return null;
    }
    return Integer.parseInt(course.trim());
  }
}
