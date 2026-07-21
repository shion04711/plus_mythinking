package com.u22.plus.webportal.mondai;

import java.util.HashMap;
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

    final String SQL_INSERT = "INSERT INTO mistake_entry (record_id, question_id, miss, answer, honbun) "
        + "VALUES (:recordId, :questionId, :miss, :answer, :honbun)";

    Map<String, Object> params = new HashMap<>();
    params.put("recordId", recordId);
    params.put("questionId", mistake.questionId());
    params.put("miss", mistake.miss());
    params.put("answer", mistake.answer());
    params.put("honbun", mistake.honbun());

    jdbc.update(SQL_INSERT, params);
  }
}
