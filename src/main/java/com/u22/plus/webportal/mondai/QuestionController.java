package com.u22.plus.webportal.mondai;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 問題登録（先生側）の画面遷移を担当するController。
 * 対応するThymeleafテンプレート: mondai/question-input.html（未作成の場合はHTML担当と調整）
 */
@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/mondai/question")
    public String getQuestionInput() {
        return "mondai/question-input";
    }

    @PostMapping("/mondai/question")
    public String postQuestionInput(Model model, @ModelAttribute QuestionForm form) {

        try {
            questionService.registQuestion(form);
        } catch (QuestionRegistException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "mondai/question-input";
        }

        return "index";
    }
}
