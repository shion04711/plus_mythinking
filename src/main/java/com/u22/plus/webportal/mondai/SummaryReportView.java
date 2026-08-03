package com.u22.plus.webportal.mondai;

/**
 * 総括レポート画面に表示するデータ。
 */
public record SummaryReportView(

  /** 総勉強時間（分） */
  Integer allTime,

  /** 総プリント枚数 */
  Integer allMaisu,

  /** 試験本番に向けてのコメント（現時点では固定文） */
  String examText
) {
}
