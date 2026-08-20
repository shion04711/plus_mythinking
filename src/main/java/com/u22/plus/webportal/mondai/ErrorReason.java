package com.u22.plus.webportal.mondai;

/**
 * 間違えた原因の分類。error_reason_m テーブルに対応する。
 * reason_id は DB側の定義と一致させること。
 */
public enum ErrorReason {

  UNDERSTANDING(1, "理解不足（分かってなかった）"),
  KNOWLEDGE(2, "知識不足（知らなかった）"),
  TIME_SHORTAGE(3, "時間切れ"),
  ASSUMPTION(4, "思い込み"),
  MISREADING(5, "読み間違い"),
  WRITING_MISTAKE(6, "書き間違い"),
  CALCULATION_MISTAKE(7, "計算ミス"),
  CARELESS(8, "ケアレスミス（その他）");

  private final int reasonId;
  private final String reasonName;

  ErrorReason(int reasonId, String reasonName) {
    this.reasonId = reasonId;
    this.reasonName = reasonName;
  }

  public int getReasonId() {
    return reasonId;
  }

  public String getReasonName() {
    return reasonName;
  }

  /**
   * reason_id からenumを取得する。
   */
  public static ErrorReason fromId(int reasonId) {
    for (ErrorReason reason : values()) {
      if (reason.reasonId == reasonId) {
        return reason;
      }
    }
    throw new IllegalArgumentException("不正な reason_id です: " + reasonId);
  }
}
