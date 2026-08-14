package com.u22.plus.webportal.user;

/**
 * ログイン中の生徒情報。
 * student_m テーブルに対応する。
 */
public record StudentData(

  /** 学籍番号（student_m の主キー、ログインIDとして使用） */
  String studentId,

  String password,

  String studentName,

  Integer courseId,

  String className,

  Integer studentNumber
) {
}
