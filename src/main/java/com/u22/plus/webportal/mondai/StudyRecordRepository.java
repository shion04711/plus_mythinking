package com.u22.plus.webportal.mondai;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

/**
 * 学習記録データの永続化を担当するRepository。
 * テーブル定義・SQL詳細はDB担当と調整の上、確定させること。
 * 間違えた問題(mistakes)は別テーブルへの複数件INSERTになる想定。
 */
@Repository
public class StudyRecordRepository {

  @Autowired
  private NamedParameterJdbcTemplate jdbc;

  // DBの1行を StudyRecord オブジェクトに変換
  private final RowMapper<StudyRecord> studyRecordRowMapper = (rs, rowNum) -> new StudyRecord(
      rs.getInt("session_id"),
      rs.getString("student_id"),
      rs.getObject("course_id", Integer.class),
      rs.getInt("study_minutes"),
      rs.getInt("print_count"),
      rs.getObject("created_at", LocalDateTime.class)
  );

  /**
   * session_id で学習記録を1件取得する。
   */
  public Optional<StudyRecord> findById(Integer sessionId) {

    final String SQL = "SELECT session_id, student_id, course_id, study_minutes, print_count, created_at "
        + "FROM study_sessions WHERE session_id = :sessionId";

    Map<String, Object> params = new HashMap<>();
    params.put("sessionId", sessionId);

    List<StudyRecord> list = jdbc.query(SQL, params, studyRecordRowMapper);

    if (list.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(list.get(0));
  }

  /**
   * 指定した生徒IDの学習記録を全件取得する（新しい順）。
   */
  public List<StudyRecord> findByStudentId(String studentId) {

    final String SQL = "SELECT session_id, student_id, course_id, study_minutes, print_count, created_at "
        + "FROM study_sessions WHERE student_id = :studentId ORDER BY created_at DESC";

    Map<String, Object> params = new HashMap<>();
    params.put("studentId", studentId);

    return jdbc.query(SQL, params, studyRecordRowMapper);
  }

  /**
   * 学習記録を1件保存する（自動採番された session_id を付与して返す）。
   */
  public StudyRecord save(StudyRecord record) {

    final String SQL_INSERT = "INSERT INTO study_sessions (student_id, course_id, study_minutes, print_count, created_at) "
        + "VALUES (:studentId, :courseId, :studyMinutes, :printCount, COALESCE(:createdAt, CURRENT_TIMESTAMP))";

    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue("studentId", record.studentId());
    params.addValue("courseId", record.courseId());
    params.addValue("studyMinutes", record.studyMinutes() != null ? record.studyMinutes() : 0);
    params.addValue("printCount", record.printCount() != null ? record.printCount() : 0);
    params.addValue("createdAt", record.createdAt());

    KeyHolder keyHolder = new GeneratedKeyHolder();

    jdbc.update(SQL_INSERT, params, keyHolder, new String[] { "session_id" });

    Integer generatedId = keyHolder.getKey().intValue();

    return new StudyRecord(
        generatedId,
        record.studentId(),
        record.courseId(),
        record.studyMinutes() != null ? record.studyMinutes() : 0,
        record.printCount() != null ? record.printCount() : 0,
        record.createdAt() != null ? record.createdAt() : LocalDateTime.now()
    );
  }
}
