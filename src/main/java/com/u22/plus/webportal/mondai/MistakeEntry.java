package com.u22.plus.webportal.mondai;

/**
 * 間違えた問題1件分の保存用データ。
 * questionIdは問題登録機能(Question)とのID紐付け用。現状フォームに入力欄が無いためnullになり得る。
 */
public record MistakeEntry(

  String questionId,

  String miss,

  String answer,

  String honbun,

  /** 間違えた原因（ケアレスミス・理解不足・時間不足・その他） */
  MistakeReason reason
) {
}
