package com.u22.plus.webportal.mondai;

/**
 * コース登録時の業務エラー。
 * 例：ID重複、日付の前後関係が不正 など
 */
public class CourseRegistException extends RuntimeException {

  public CourseRegistException(String message) {
    super(message);
  }
}
