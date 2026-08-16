package com.u22.plus.webportal.mondai;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.u22.plus.webportal.user.StudentLoginService;
import com.u22.plus.webportal.user.StudentData;

import jakarta.servlet.http.HttpSession;

/**
 * 生徒自身が自分の学習記録を振り返るレポート画面のController。
 * 対応するThymeleafテンプレート: report/daily.html, report/soukatsu.html
 */
@Controller
public class ReportController {

  @Autowired
  private ReportService reportService;

  @Autowired
  private StudentLoginService loginServive;

  @Autowired
  private HttpSession session;

  @GetMapping("/report/daily")
  public String getDailyReport(Model model) {

    if (!loginServive.isLogin()) {
      return "login";
    }

    StudentData loginUser = (StudentData) session.getAttribute("userData");

    DailyReportView report = reportService.getDailyReport(loginUser.studentId());

    model.addAttribute("dailytime", report.dailyTime());
    model.addAttribute("dailyprint", report.dailyPrint());
    model.addAttribute("alltime", report.allTime());
    model.addAttribute("allmaisu", report.allMaisu());
    model.addAttribute("reasonCounts", report.reasonCounts());
    model.addAttribute("sikentext", report.comparisonText());
    model.addAttribute("nextAdviceText", report.nextAdviceText());

    return "report/daily";
  }

  @GetMapping("/report/soukatsu")
  public String getSummaryReport(Model model) {

    if (!loginServive.isLogin()) {
      return "login";
    }

    StudentData loginUser = (StudentData) session.getAttribute("userData");

    SummaryReportView report = reportService.getSummaryReport(loginUser.studentId());

    model.addAttribute("alltime", report.allTime());
    model.addAttribute("allmaisu", report.allMaisu());
    model.addAttribute("sikentext", report.examText());

    return "report/soukatsu";
  }
}
