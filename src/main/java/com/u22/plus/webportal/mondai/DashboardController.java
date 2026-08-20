package com.u22.plus.webportal.mondai;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.u22.plus.webportal.user.TeacherData;
import com.u22.plus.webportal.user.TeacherLoginService;

import jakarta.servlet.http.HttpSession;

/**
 * 講師向け分析ダッシュボードを担当するController。
 * 対応するThymeleafテンプレート: teach/bunseki.html
 *
 * ログイン中の講師が担当するコースに所属する生徒のみを表示する。
 * デイリー/総括の切り替えや円グラフの更新はJS側(dashboard.js)で行うため、
 * ここでは生徒一覧データ（daily/summary両方の集計込み）をまとめて渡す。
 */
@Controller
public class DashboardController {

  @Autowired
  private DashboardService dashboardService;

  @Autowired
  private TeacherLoginService loginServive;

  @Autowired
  private HttpSession session;

  @GetMapping("/dashboard")
  public String getDashboard(Model model, @RequestParam(defaultValue = "daily") String mode) {

    if (!loginServive.isLogin()) {
      return "teach/teachlogin";
    }

    TeacherData loginUser = (TeacherData) session.getAttribute("teacherData");

    List<DashboardStudentView> students = dashboardService.getDashboardStudents(loginUser.teacherId());

    model.addAttribute("mode", mode);
    model.addAttribute("students", students);

    return "teach/bunseki";
  }
}
