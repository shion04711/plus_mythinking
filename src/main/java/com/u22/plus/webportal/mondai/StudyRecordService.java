package com.u22.plus.webportal.mondai;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 学習記録登録（勉強時間・プリント枚数・間違えた問題）の業務ロジックを担当するService。
 *
 * 出席状況(attendance)は study_sessions テーブルに対応するカラムが無いため保存しない。
 * 間違えた問題は Question（input_logs）として、学習記録とは別レコードで保存する。
 *
 * 主な処理:
 *  - フォーム入力値（文字列）の変換とチェック
 *  - 学習記録本体(study_sessions)の保存
 *  - 間違えた問題リストの Question(input_logs) への保存（0件でも可）
 */
@Transactional
@Service
public class StudyRecordService {

  @Autowired
  private StudyRecordRepository studyRecordRepository;

  @Autowired
  private QuestionRepository questionRepository;

  /**
   * 学習記録を登録する。
   *
   * @param studentId ログイン中の生徒ID
   * @param courseId  ログイン中の生徒が所属するコースID（student_m.course_id）
   * @param form      フォーム入力値
   */
  public void registStudyRecord(String studentId, Integer courseId, StudyRecordForm form) {

    Integer printCount = parsePrintCount(form.getMaisu());
    Integer studyMinutes = parseStudyMinutes(form.getTime());

    StudyRecord studyRecord = new StudyRecord(
        null,
        studentId,
        courseId,
        studyMinutes,
        printCount,
        LocalDateTime.now());

    studyRecordRepository.save(studyRecord);

    saveMistakes(studentId, courseId, form.getEntries());
  }

  private void saveMistakes(String studentId, Integer courseId, List<MistakeEntryForm> entries) {

    if (entries == null) {
      return;
    }

    for (MistakeEntryForm entryForm : entries) {
      questionRepository.save(toQuestion(studentId, courseId, entryForm));
    }
  }

  private Question toQuestion(String studentId, Integer courseId, MistakeEntryForm entryForm) {

    String honbun = entryForm.getHonbun();
    if (honbun == null || honbun.isBlank()) {
      throw new StudyRecordRegistException("間違えた問題の内容を入力してください。");
    }

    Integer reasonId = parseReasonId(entryForm.getReason());

    return new Question(
        null,
        studentId,
        courseId,
        reasonId,
        honbun,
        entryForm.getAnswer(),
        entryForm.getMiss(),
        LocalDateTime.now());
  }

  /**
   * 間違えた原因IDを文字列からIntegerへ変換する。
   * 現状フォームで未選択の場合は「ケアレスミス（その他）」扱いとする。
   */
  private Integer parseReasonId(String reason) {

    if (reason == null || reason.isBlank()) {
      return ErrorReason.CARELESS.getReasonId();
    }

    try {
      int reasonId = Integer.parseInt(reason.trim());
      // 存在するreason_idか検証する
      ErrorReason.fromId(reasonId);
      return reasonId;
    } catch (IllegalArgumentException e) {
      throw new StudyRecordRegistException("間違えた原因の値が不正です。");
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
}
