package com.u22.plus.webportal.mondai;

import java.time.LocalDateTime;

/**
 * 1回分の学習記録（出席状況・勉強時間・プリント枚数・間違えた問題リスト）。
 */
public record StudyRecord(

  Integer sessionId,

  String studentId,

  Integer courseId,

  /** 勉強時間（分） */
  Integer studyMinutes,

  /** プリント枚数 */
  Integer printCount,

  LocalDateTime createdAt
) {
}
