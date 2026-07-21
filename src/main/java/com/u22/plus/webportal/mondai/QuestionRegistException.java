package com.u22.plus.webportal.mondai;

/**
 * 問題登録時の業務エラー。
 * 例：ID重複、存在しない分野の指定 など
 */
public class QuestionRegistException extends RuntimeException {

  public QuestionRegistException(String message) {
    super(message);
  }
}
