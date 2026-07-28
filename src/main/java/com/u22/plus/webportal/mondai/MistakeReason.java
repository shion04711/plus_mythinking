package com.u22.plus.webportal.mondai;

/**
 * 間違えた原因の分類。
 * デイリーレポート画面の円グラフ集計に使用する。
 * HTML側のselect要素等のvalue属性に対応させること。
 */
public enum MistakeReason {
  CARELESS,      // ケアレスミス
  UNDERSTANDING, // 理解不足
  TIME_SHORTAGE, // 時間不足
  OTHER          // その他
}
