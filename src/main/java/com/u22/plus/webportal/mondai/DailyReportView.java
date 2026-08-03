package com.u22.plus.webportal.mondai;

import java.util.Map;

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

  /** 間違えた原因ごとの件数（円グラフ用） */
  Map<MistakeReason, Long> reasonCounts,

  /** 今までのデータとの比較コメント（現時点では固定文） */
  String comparisonText,

  /** 次回気を付けることのコメント（現時点では固定文） */
  String nextAdviceText
) {
}
