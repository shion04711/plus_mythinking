package com.u22.plus.webportal.mondai;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * 学習記録データの永続化を担当するRepository。
 * テーブル定義・SQL詳細はDB担当と調整の上、確定させること。
 * 間違えた問題(mistakes)は別テーブルへの複数件INSERTになる想定。
 */
@Repository
public class StudyRecordRepository {

  @Autowired
  private NamedParameterJdbcTemplate jdbc;

  /**
   * 学習記録本体（出席・プリント枚数・勉強時間）を保存する。
   * 戻り値は生成された学習記録ID（mistakesの紐付けに使用）を想定。
   */
  public Long saveRecord(StudyRecord studyRecord) {

    final String SQL_INSERT = "INSERT INTO study_record (user_id, attendance, print_count, study_minutes, created_at) "
        + "VALUES (:userId, :attendance, :printCount, :studyMinutes, :createdAt)";

    Map<String, Object> params = new HashMap<>();
    params.put("userId", studyRecord.userId());
    params.put("attendance", studyRecord.attendance().name());
    params.put("printCount", studyRecord.printCount());
    params.put("studyMinutes", studyRecord.studyMinutes());
    params.put("createdAt", studyRecord.createdAt());

    jdbc.update(SQL_INSERT, params);

    // TODO: 採番方式が決まり次第、生成されたIDを取得して返すよう修正する
    return null;
  }

  /**
   * 間違えた問題を1件保存する。
   */
  public void saveMistake(Long recordId, MistakeEntry mistake) {

    final String SQL_INSERT = "INSERT INTO mistake_entry (record_id, question_id, miss, answer, honbun, reason) "
        + "VALUES (:recordId, :questionId, :miss, :answer, :honbun, :reason)";

    Map<String, Object> params = new HashMap<>();
    params.put("recordId", recordId);
    params.put("questionId", mistake.questionId());
    params.put("miss", mistake.miss());
    params.put("answer", mistake.answer());
    params.put("honbun", mistake.honbun());
    params.put("reason", mistake.reason() != null ? mistake.reason().name() : null);

    jdbc.update(SQL_INSERT, params);
  }

  /**
   * 指定した生徒(userId)の学習記録を全件取得する（新しい順）。
   * 各学習記録に紐づく間違えた問題も合わせて取得する。
   *
   * 講師側の詳細ページ（生徒1人の記録一覧）で使用する。
   */
  public List<StudyRecord> findByUserId(String userId) {

    final String SQL_RECORDS = "SELECT record_id, user_id, attendance, print_count, study_minutes, created_at "
        + "FROM study_record WHERE user_id = :userId ORDER BY created_at DESC";

    Map<String, Object> params = new HashMap<>();
    params.put("userId", userId);

    List<Map<String, Object>> recordRows = jdbc.queryForList(SQL_RECORDS, params);

    List<StudyRecord> studyRecords = new ArrayList<>();

    for (Map<String, Object> row : recordRows) {

      Long recordId = ((Number) row.get("record_id")).longValue();

      List<MistakeEntry> mistakes = findMistakesByRecordId(recordId);

      StudyRecord studyRecord = new StudyRecord(
          recordId,
          (String) row.get("user_id"),
          AttendanceStatus.valueOf((String) row.get("attendance")),
          ((Number) row.get("print_count")).intValue(),
          ((Number) row.get("study_minutes")).intValue(),
          mistakes,
          (LocalDateTime) row.get("created_at"));

      studyRecords.add(studyRecord);
    }

    return studyRecords;
  }

  /**
   * 指定した学習記録(recordId)に紐づく間違えた問題を全件取得する。
   */
  private List<MistakeEntry> findMistakesByRecordId(Long recordId) {

    final String SQL_MISTAKES = "SELECT question_id, miss, answer, honbun, reason "
        + "FROM mistake_entry WHERE record_id = :recordId";

    Map<String, Object> params = new HashMap<>();
    params.put("recordId", recordId);

    List<Map<String, Object>> mistakeRows = jdbc.queryForList(SQL_MISTAKES, params);

    List<MistakeEntry> mistakes = new ArrayList<>();

    for (Map<String, Object> row : mistakeRows) {

      String reasonStr = (String) row.get("reason");
      MistakeReason reason = reasonStr != null ? MistakeReason.valueOf(reasonStr) : null;

      mistakes.add(new MistakeEntry(
          (String) row.get("question_id"),
          (String) row.get("miss"),
          (String) row.get("answer"),
          (String) row.get("honbun"),
          reason));
    }

    return mistakes;
  }
}
