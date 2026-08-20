package com.u22.plus.webportal.mondai;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.u22.plus.webportal.user.StudentLoginService;
import com.u22.plus.webportal.user.StudentData;

import jakarta.servlet.http.HttpSession;

@Controller
public class StudyRecordController {

    @Autowired
    private StudyRecordService studyRecordService;

    @Autowired
    private StudentLoginService loginServive;

    @Autowired
    private HttpSession session;

    @GetMapping("/mondai")
    public String GetMondai() {
        return "mondai/input";
    }

    @PostMapping("/mondai")
    public String PostMondai(Model model, @ModelAttribute StudyRecordForm form) {

        if (!loginServive.isLogin()) {
            return "student/login";
        }

        StudentData studentData = (StudentData) session.getAttribute("studentData");

        try {
            studyRecordService.registStudyRecord(studentData.studentId(), studentData.courseId(), form);
        } catch (StudyRecordRegistException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "mondai/input";
        }

        return "student/index";
    }
}
