package com.u22.plus.webportal.mondai;

import java.time.LocalDate;

/**
 * コースデータ。
 * 講師が作成する、生徒が所属する学習コース（期間付き）。
 */
public record Course(

  Integer courseId,

  String courseName,

  LocalDate startDate,

  LocalDate endDate,

  String teacherId
) {
}
