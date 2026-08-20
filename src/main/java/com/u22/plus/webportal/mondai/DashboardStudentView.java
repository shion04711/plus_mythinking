package com.u22.plus.webportal.mondai;

import java.util.List;

/**
 * 講師向け分析ダッシュボード(teach/bunseki.html)の生徒一覧テーブル1行分のデータ。
 *
 * dashboard.js が読む data-* 属性に対応させるため、
 * デイリー(daily)・総括(summary)の両方の集計値を持つ。
 */
public record DashboardStudentView(

  String id,

  String name,

  String classNum,

  /** 当日の間違えた問題数 */
  Long dailyMissCount,

  /** 当日の間違えた原因の内訳（error_reason_m の reason_id 昇順、件数だけの配列） */
  List<Long> dailyReasonCounts,

  /** 当日分の備考（現時点では固定文） */
  String dailyInfo,

  /** 累計の間違えた問題数 */
  Long summaryMissCount,

  /** 累計の間違えた原因の内訳（同上） */
  List<Long> summaryReasonCounts,

  /** 累計分の備考（現時点では固定文） */
  String summaryInfo
) {
}
