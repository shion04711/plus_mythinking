package com.u22.plus.webportal.mondai;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.u22.plus.webportal.user.TeacherLoginService;
import com.u22.plus.webportal.user.TeacherData;

import jakarta.servlet.http.HttpSession;

/**
 * コース作成（講師側）の画面遷移を担当するController。
 * 対応するThymeleafテンプレート: teach/courseadd.html
 *
 * ログイン中の講師のみ作成可能（TeacherLoginServiceでログイン状態を判定）。
 */
@Controller
public class CourseController {

  @Autowired
  private CourseService courseService;

  @Autowired
  private TeacherLoginService loginServive;

  @Autowired
  private HttpSession session;

  @GetMapping("/courseadd")
  public String getCourseAdd() {
    return "teach/courseadd";
  }

  @PostMapping("/courseadd")
  public String postCourseAdd(Model model, @ModelAttribute CourseForm form) {

    if (!loginServive.isLogin()) {
      return "teach/teachlogin";
    }

    TeacherData loginUser = (TeacherData) session.getAttribute("teacherData");

    try {
      courseService.registCourse(form, loginUser.teacherId());
    } catch (CourseRegistException e) {
      model.addAttribute("errormessage", e.getMessage());
      return "teach/courseadd";
    }

    return "teach/teachindex";
  }
}
