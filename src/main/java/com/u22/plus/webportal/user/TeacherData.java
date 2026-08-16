package com.u22.plus.webportal.user;

/**
 * ログイン中の講師情報。
 * teacher_m テーブルに対応する。
 */
public record TeacherData(

  /** 教員ID（teacher_m の主キー、ログインIDとして使用） */
  String teacherId,

  String password,

  String teacherName
) {
}
