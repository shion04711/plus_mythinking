package com.u22.plus.webportal.mondai;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.u22.plus.webportal.user.LoginServive;
import com.u22.plus.webportal.user.UserData;

import jakarta.servlet.http.HttpSession;

/**
 * コース作成（講師側）の画面遷移を担当するController。
 * 対応するThymeleafテンプレート: courseadd.html
 *
 * 作成できるのはROLE_ADMIN・ROLE_TOPの権限を持つユーザーのみ。
 */
@Controller
public class CourseController {

  @Autowired
  private CourseService courseService;

  @Autowired
  private LoginServive loginServive;

  @Autowired
  private HttpSession session;

  @GetMapping("/courseadd")
  public String getCourseAdd() {
    return "courseadd";
  }

  @PostMapping("/courseadd")
  public String postCourseAdd(Model model, @ModelAttribute CourseForm form) {

    if (!loginServive.isLogin()) {
      return "login";
    }

    UserData loginUser = (UserData) session.getAttribute("userData");

    if (!isTeacherRole(loginUser.role())) {
      model.addAttribute("errormessage", "この操作を行う権限がありません。");
      return "login";
    }

    try {
      courseService.registCourse(form);
    } catch (CourseRegistException e) {
      model.addAttribute("errormessage", e.getMessage());
      return "courseadd";
    }

    return "index";
  }

  /**
   * 講師権限（ROLE_ADMIN・ROLE_TOP）かどうかを判定する。
   */
  private boolean isTeacherRole(String role) {
    return "ROLE_ADMIN".equals(role) || "ROLE_TOP".equals(role);
  }
}
