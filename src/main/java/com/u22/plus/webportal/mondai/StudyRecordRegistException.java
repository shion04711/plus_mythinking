package com.u22.plus.webportal.mondai;

/**
 * 学習記録登録時の業務エラー。
 * 例：数値変換エラー、存在しないquestionIdの指定 など
 */
public class StudyRecordRegistException extends RuntimeException {

  public StudyRecordRegistException(String message) {
    super(message);
  }
}
