package com.u22.plus.webportal.mondai;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 問題登録（先生側）の業務ロジックを担当するService。
 *
 * 主なチェック内容:
 *  - questionId の重複チェック（先生が手入力するID運用のため）
 *  - fieldId（分野）が分野マスタに存在するかのチェック
 */
@Transactional
@Service
public class QuestionService {

  @Autowired
  private QuestionRepository questionRepository;

  public Question registQuestion(QuestionForm form) {

    validate(form);

    Question question = new Question(
        null,
        form.questionId().trim(),
        form.location().trim(),
        form.fieldId(),
        LocalDateTime.now());

    return questionRepository.save(question);
  }

  private void validate(QuestionForm form) {

    String questionId = form.questionId();

    if (questionId == null || questionId.isBlank()) {
      throw new QuestionRegistException("問題IDが入力されていません。");
    }

    if (questionRepository.existsById(questionId.trim())) {
      throw new QuestionRegistException("問題ID「" + questionId + "」は既に登録されています。別のIDを指定してください。");
    }

    if (form.fieldId() == null) {
      throw new QuestionRegistException("分野が指定されていません。");
    }

    if (!questionRepository.existsFieldId(form.fieldId())) {
      throw new QuestionRegistException("指定された分野（ID: " + form.fieldId() + "）は存在しません。");
    }

    if (form.location() == null || form.location().isBlank()) {
      throw new QuestionRegistException("問題の場所（リンク）が入力されていません。");
    }
  }
}
