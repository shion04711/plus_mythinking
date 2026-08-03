package com.u22.plus.webportal.mondai;

/**
 * 問題登録フォームの入力値。
 * 先生側の問題登録画面から受け取る。
 */
public record QuestionForm(

  String questionId,

  String location,

  Long fieldId
) {
}
