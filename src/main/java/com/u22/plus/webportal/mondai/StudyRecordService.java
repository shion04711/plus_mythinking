package com.u22.plus.webportal.mondai;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 学習記録登録（出席・勉強時間・プリント枚数・間違えた問題）の業務ロジックを担当するService。
 *
 * 主な処理:
 *  - フォーム入力値（文字列）の変換とチェック
 *  - 間違えた問題リストの変換（0件でも可）
 *  - questionIdが入力されている場合のみ、問題登録機能とのID存在チェック
 */
@Transactional
@Service
public class StudyRecordService {

  @Autowired
  private StudyRecordRepository studyRecordRepository;

  @Autowired
  private QuestionRepository questionRepository;

  public void registStudyRecord(String userId, StudyRecordForm form) {

    AttendanceStatus attendance = parseAttendance(form.getAtten());
    Integer printCount = parsePrintCount(form.getMaisu());
    Integer studyMinutes = parseStudyMinutes(form.getTime());
    List<MistakeEntry> mistakes = toMistakeEntries(form.getEntries());

    StudyRecord studyRecord = new StudyRecord(
        null,
        userId,
        attendance,
        printCount,
        studyMinutes,
        mistakes,
        LocalDateTime.now());

    Long recordId = studyRecordRepository.saveRecord(studyRecord);

    for (MistakeEntry mistake : mistakes) {
      studyRecordRepository.saveMistake(recordId, mistake);
    }
  }

  private AttendanceStatus parseAttendance(String atten) {

    if (atten == null || atten.isBlank()) {
      throw new StudyRecordRegistException("出席状況を選択してください。");
    }

    try {
      return AttendanceStatus.valueOf(atten.trim().toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new StudyRecordRegistException("出席状況の値が不正です。");
    }
  }

  private Integer parsePrintCount(String maisu) {

    if (maisu == null || maisu.isBlank()) {
      throw new StudyRecordRegistException("プリント枚数が入力されていません。");
    }

    try {
      int value = Integer.parseInt(maisu.trim());
      if (value < 0) {
        throw new StudyRecordRegistException("プリント枚数は0以上で入力してください。");
      }
      return value;
    } catch (NumberFormatException e) {
      throw new StudyRecordRegistException("プリント枚数は数値で入力してください。");
    }
  }

  private Integer parseStudyMinutes(String time) {

    if (time == null || time.isBlank()) {
      throw new StudyRecordRegistException("勉強時間が入力されていません。");
    }

    // custom-time-picker.js は "HH:mm" 形式のhidden inputを送信する
    String[] parts = time.trim().split(":");
    if (parts.length != 2) {
      throw new StudyRecordRegistException("勉強時間の形式が不正です。");
    }

    try {
      int hour = Integer.parseInt(parts[0]);
      int minute = Integer.parseInt(parts[1]);
      return hour * 60 + minute;
    } catch (NumberFormatException e) {
      throw new StudyRecordRegistException("勉強時間の形式が不正です。");
    }
  }

  private List<MistakeEntry> toMistakeEntries(List<MistakeEntryForm> entries) {

    List<MistakeEntry> mistakes = new ArrayList<>();

    if (entries == null) {
      return mistakes;
    }

    for (MistakeEntryForm entryForm : entries) {
      mistakes.add(toMistakeEntry(entryForm));
    }

    return mistakes;
  }

  private MistakeEntry toMistakeEntry(MistakeEntryForm entryForm) {

    String honbun = entryForm.getHonbun();
    if (honbun == null || honbun.isBlank()) {
      throw new StudyRecordRegistException("間違えた問題の内容を入力してください。");
    }

    String questionId = entryForm.getQuestionId();

    // questionIdが入力されている場合のみ、問題登録機能とのID紐付けを検証する。
    // 現状フォームに入力欄が無いため未入力（null/空文字）が正常系。
    if (questionId != null && !questionId.isBlank()) {
      if (!questionRepository.existsById(questionId.trim())) {
        throw new StudyRecordRegistException("指定された問題ID「" + questionId + "」は登録されていません。");
      }
    }

    return new MistakeEntry(
        questionId != null && !questionId.isBlank() ? questionId.trim() : null,
        entryForm.getMiss(),
        entryForm.getAnswer(),
        honbun);
  }
}
