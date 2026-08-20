package com.u22.plus.webportal.mondai;

import java.util.List;

/**
 * デイリーレポート画面に表示するデータ。
 * HTML側の th:text="${dailytime}" 等に対応する各値を持つ。
 */
public record DailyReportView(

  /** 当日の勉強時間（分） */
  Integer dailyTime,

  /** 当日のプリント枚数 */
  Integer dailyPrint,

  /** 累計勉強時間（分） */
  Integer allTime,

  /** 累計プリント枚数 */
  Integer allMaisu,

  /**
   * 間違えた原因ごとの件数（累計、円グラフ用）。
   * error_reason_m の reason_id 昇順（1, 2, 3...）に対応した件数だけの配列。
   * 例: [3, 0, 1, 0, 0, 0, 0, 5] なら reason_id=1が3件, reason_id=8が5件。
   */
  List<Long> reasonCounts,

  /** 間違えた原因ごとの件数（当日分）。形式は reasonCounts と同じ。 */
  List<Long> dailyReasonCounts,

  /** 今までのデータとの比較コメント（現時点では固定文） */
  String comparisonText,

  /** 次回気を付けることのコメント（現時点では固定文） */
  String nextAdviceText
) {
}
