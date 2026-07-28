package com.u22.plus.webportal.mondai;

/**
 * 間違えた問題1件分のフォーム入力。
 * HTML側の name="entries[n].xxx" にバインドされる。
 *
 * questionId・reasonは現状のフォームに入力欄が無いため任意項目。
 * 将来フォームに入力欄が追加された際、そのまま利用できる。
 */
public class MistakeEntryForm {

  private String questionId;

  private String miss;

  private String answer;

  private String honbun;

  /** 間違えた原因（文字列。値は MistakeReason の名前と一致させる。例: "CARELESS"） */
  private String reason;

  public String getQuestionId() {
    return questionId;
  }

  public void setQuestionId(String questionId) {
    this.questionId = questionId;
  }

  public String getMiss() {
    return miss;
  }

  public void setMiss(String miss) {
    this.miss = miss;
  }

  public String getAnswer() {
    return answer;
  }

  public void setAnswer(String answer) {
    this.answer = answer;
  }

  public String getHonbun() {
    return honbun;
  }

  public void setHonbun(String honbun) {
    this.honbun = honbun;
  }

  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }
}
