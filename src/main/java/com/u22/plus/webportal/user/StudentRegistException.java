package com.u22.plus.webportal.user;

/**
 * 生徒登録時の業務エラー。
 */
public class StudentRegistException extends RuntimeException {

  public StudentRegistException(String message) {
    super(message);
  }
}
