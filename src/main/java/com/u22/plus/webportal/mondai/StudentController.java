package com.u22.plus.webportal.mondai;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.u22.plus.webportal.user.StudentData;
import com.u22.plus.webportal.user.StudentRepository;
import com.u22.plus.webportal.user.TeacherLoginService;

/**
 * 講師側の生徒一覧・生徒詳細ページ（学習記録一覧）を担当するController。
 * 一覧URL: /student/list
 * 詳細URL: /student/{userId}
 *
 * ログイン中の講師のみ閲覧可能（TeacherLoginServiceでログイン状態を判定）。
 */
@Controller
public class StudentController {

  @Autowired
  private StudyRecordViewService studyRecordViewService;

  @Autowired
  private StudentRepository userRepository;

  @Autowired
  private TeacherLoginService loginServive;

  @GetMapping("/student/list")
  public String getStudentList(Model model) {

    if (!loginServive.isLogin()) {
      return "teach/teachlogin";
    }

    List<StudentData> students = userRepository.findAll();

    model.addAttribute("students", students);

    return "student/list";
  }

  @GetMapping("/student/{userId}")
  public String getStudentDetail(Model model, @PathVariable String userId) {

    if (!loginServive.isLogin()) {
      return "teach/teachlogin";
    }

    List<StudyRecord> studyRecords = studyRecordViewService.getStudyRecords(userId);

    model.addAttribute("targetUserId", userId);
    model.addAttribute("studyRecords", studyRecords);

    return "student/detail";
  }
}
