package com.u22.plus.webportal.mondai;

import java.time.LocalDateTime;

/**
 * 問題データ。
 * 先生側が事前に登録する問題。生徒側は入力時にこのquestionIdを使って回答を紐づける。
 */
public record Question(

  String questionId,

  String location,

  Long fieldId,

  LocalDateTime createdAt
) {
}
