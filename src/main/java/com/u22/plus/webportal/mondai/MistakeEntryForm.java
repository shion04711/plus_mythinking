package com.u22.plus.webportal.mondai;

/**
 * 間違えた問題1件分のフォーム入力（input_logs に対応）。
 * HTML側の name="entries[n].xxx" にバインドされる。
 *
 * - honbun     : 問題文 (question_text)
 * - answer     : 正答 (correct_answer)
 * - miss       : 誤答 (incorrect_answer)
 * - reason     : ミス原因ID (reason_id、文字列で受け取り数値に変換する。例: "1"～"8")
 * - questionId : 現状未使用（フォームに入力欄が無いため）
 */
public class MistakeEntryForm {

  private String questionId;

  private String miss;

  private String answer;

  private String honbun;

  /** 間違えた原因ID（error_reason_m.reason_id の文字列表現。例: "1"） */
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
