package com.u22.plus.webportal.mondai;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.u22.plus.webportal.user.LoginServive;
import com.u22.plus.webportal.user.UserData;
import com.u22.plus.webportal.user.UserRepository;

import jakarta.servlet.http.HttpSession;

/**
 * 講師側の生徒一覧・生徒詳細ページ（学習記録一覧）を担当するController。
 * 一覧URL: /student/list
 * 詳細URL: /student/{userId} （例: /student/taro@xxx.co.jp）
 *
 * 閲覧できるのはROLE_ADMIN・ROLE_TOPの権限を持つユーザーのみ。
 */
@Controller
public class StudentController {

  @Autowired
  private StudyRecordViewService studyRecordViewService;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private LoginServive loginServive;

  @Autowired
  private HttpSession session;

  @GetMapping("/student/list")
  public String getStudentList(Model model) {

    if (!loginServive.isLogin()) {
      return "login";
    }

    UserData loginUser = (UserData) session.getAttribute("userData");

    if (!isTeacherRole(loginUser.role())) {
      model.addAttribute("errorMessage", "この画面を閲覧する権限がありません。");
      return "login";
    }

    List<UserData> students = userRepository.findGeneralUsers();

    model.addAttribute("students", students);

    return "student/list";
  }

  @GetMapping("/student/{userId}")
  public String getStudentDetail(Model model, @PathVariable String userId) {

    if (!loginServive.isLogin()) {
      return "login";
    }

    UserData loginUser = (UserData) session.getAttribute("userData");

    if (!isTeacherRole(loginUser.role())) {
      model.addAttribute("errorMessage", "この画面を閲覧する権限がありません。");
      return "login";
    }

    List<StudyRecord> studyRecords = studyRecordViewService.getStudyRecords(userId);

    model.addAttribute("targetUserId", userId);
    model.addAttribute("studyRecords", studyRecords);

    return "student/detail";
  }

  /**
   * 講師権限（ROLE_ADMIN・ROLE_TOP）かどうかを判定する。
   */
  private boolean isTeacherRole(String role) {
    return "ROLE_ADMIN".equals(role) || "ROLE_TOP".equals(role);
  }
}
