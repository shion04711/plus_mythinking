package com.u22.plus.webportal.mondai;

import java.util.ArrayList;
import java.util.List;

/**
 * 学習記録フォーム全体の入力値。
 * HTMLの単一フォーム（出席・勉強時間・プリント枚数・間違えた問題リスト）に対応する。
 */
public class StudyRecordForm {

  private String atten;

  /** プリント枚数（フォーム上は文字列） */
  private String maisu;

  /** 勉強時間（custom-time-picker.jsが hidden input に "HH:mm" 形式で詰める） */
  private String time;

  private List<MistakeEntryForm> entries = new ArrayList<>();

  public String getAtten() {
    return atten;
  }

  public void setAtten(String atten) {
    this.atten = atten;
  }

  public String getMaisu() {
    return maisu;
  }

  public void setMaisu(String maisu) {
    this.maisu = maisu;
  }

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }

  public List<MistakeEntryForm> getEntries() {
    return entries;
  }

  public void setEntries(List<MistakeEntryForm> entries) {
    this.entries = entries;
  }
}
