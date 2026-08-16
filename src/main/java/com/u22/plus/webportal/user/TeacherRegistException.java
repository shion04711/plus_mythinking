package com.u22.plus.webportal.user;

/**
 * 講師登録時の業務エラー。
 */
public class TeacherRegistException extends RuntimeException {

  public TeacherRegistException(String message) {
    super(message);
  }
}
