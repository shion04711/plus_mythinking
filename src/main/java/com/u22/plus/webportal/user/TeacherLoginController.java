package com.u22.plus.webportal.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 講師側のログイン・ログアウト・新規登録の画面遷移を担当するController。
 * 対応するThymeleafテンプレート: teach/teachlogin.html, teach/teachadd.html
 */
@Controller
public class TeacherLoginController {

  @Autowired
  private TeacherLoginService teacherLoginService;

  @GetMapping("/teachlogin")
  public String getLogin() {
    return "teach/teachlogin";
  }

  @PostMapping("/teachlogin")
  public String login(Model model,
      @RequestParam(name = "user_id") String userId,
      @RequestParam(name = "password") String password) {

    if (!teacherLoginService.login(userId, password)) {
      model.addAttribute("errormessage", "ユーザIDまたはパスワードが違います。");
      return "teach/teachlogin";
    }

    return "redirect:/";
  }

  @GetMapping("/teachlogout")
  public String logout() {
    teacherLoginService.logout();
    return "teach/teachlogin";
  }

  @GetMapping("/teachadd")
  public String getTeacherAdd() {
    return "teach/teachadd";
  }

  @PostMapping("/teachadd")
  public String postTeacherAdd(Model model,
      @RequestParam(name = "user_id") String userId,
      @RequestParam(name = "password") String password,
      @RequestParam(name = "password2") String password2,
      @RequestParam(name = "hyoji") String hyoji) {

    try {
      teacherLoginService.register(userId, password, password2, hyoji);
    } catch (TeacherRegistException e) {
      model.addAttribute("errormessage", e.getMessage());
      return "teach/teachadd";
    }

    return "teach/teachlogin";
  }
}
