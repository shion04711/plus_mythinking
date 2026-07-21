package com.u22.plus.webportal.mondai;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 1回分の学習記録（出席状況・勉強時間・プリント枚数・間違えた問題リスト）。
 */
public record StudyRecord(

  String userId,

  AttendanceStatus attendance,

  /** プリント枚数 */
  Integer printCount,

  /** 勉強時間（分） */
  Integer studyMinutes,

  /** 間違えた問題のリスト（0件可） */
  List<MistakeEntry> mistakes,

  LocalDateTime createdAt
) {
}
