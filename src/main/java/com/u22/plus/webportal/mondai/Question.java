package com.u22.plus.webportal.mondai;

import java.time.LocalDateTime;

/**
 * 問題データ。
 * 先生側が事前に登録する問題。生徒側は入力時にこのquestionIdを使って回答を紐づける。
 */
public record Question(

  Integer logId,          // log_id (/自動採番)

  String studentId,       

  Integer courseId,       

  Integer reasonId,       // ミス原因ID

  String questionText,    // 問題文

  String correctAnswer,   // 正答

  String incorrectAnswer, // 誤答

  LocalDateTime createdAt
) {
}
