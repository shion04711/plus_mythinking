package com.u22.plus.webportal.mondai;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.RowMapper;

/**
 * 問題データの永続化を担当するRepository。
 * テーブル定義・SQL詳細はDB担当と調整の上、確定させること。
 * ここではロジック側（Service）が必要とする最小限のメソッドのみを用意している。
 */
@Repository
public class QuestionRepository {

  @Autowired
  private NamedParameterJdbcTemplate jdbc;

  // RowMapper（DBの1行を Question オブジェクトに変換）
  private final RowMapper<Question> questionRowMapper = (rs, rowNum) -> new Question(
      rs.getInt("log_id"),
      rs.getString("student_id"),
      rs.getObject("course_id", Integer.class),
      rs.getObject("reason_id", Integer.class),
      rs.getString("question_text"),
      rs.getString("correct_answer"),
      rs.getString("incorrect_answer"),
      rs.getObject("created_at", LocalDateTime.class)
  );

  /**
   * logId で入力ログを1件取得する。
   */
  public Optional<Question> findById(Integer logId) {

    final String SQL = "SELECT log_id, student_id, course_id, reason_id, question_text, correct_answer, incorrect_answer, created_at "
        + "FROM input_logs WHERE log_id = :logId";

    Map<String, Object> params = new HashMap<>();
    params.put("logId", logId);

    List<Question> list = jdbc.query(SQL, params, questionRowMapper);

    if (list.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(list.get(0));
  }

  /**
   * 生徒IDに紐づく入力ログ一覧を取得する（新しい順）。
   */
  public List<Question> findByStudentId(String studentId) {

    final String SQL = "SELECT log_id, student_id, course_id, reason_id, question_text, correct_answer, incorrect_answer, created_at "
        + "FROM input_logs WHERE student_id = :studentId ORDER BY created_at DESC";

    Map<String, Object> params = new HashMap<>();
    params.put("studentId", studentId);

    return jdbc.query(SQL, params, questionRowMapper);
  }

  /**
   * 入力ログを1件保存する（自動採番された log_id をセットして返す）。
   */
  public Question save(Question question) {

    final String SQL_INSERT = "INSERT INTO input_logs (student_id, course_id, reason_id, question_text, correct_answer, incorrect_answer, created_at) "
        + "VALUES (:studentId, :courseId, :reasonId, :questionText, :correctAnswer, :incorrectAnswer, COALESCE(:createdAt, CURRENT_TIMESTAMP))";

    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue("studentId", question.studentId());
    params.addValue("courseId", question.courseId());
    params.addValue("reasonId", question.reasonId());
    params.addValue("questionText", question.questionText());
    params.addValue("correctAnswer", question.correctAnswer());
    params.addValue("incorrectAnswer", question.incorrectAnswer());
    params.addValue("createdAt", question.createdAt());

    KeyHolder keyHolder = new GeneratedKeyHolder();

    jdbc.update(SQL_INSERT, params, keyHolder, new String[] { "log_id" });

    Integer generatedId = keyHolder.getKey().intValue();

    return new Question(
        generatedId,
        question.studentId(),
        question.courseId(),
        question.reasonId(),
        question.questionText(),
        question.correctAnswer(),
        question.incorrectAnswer(),
        question.createdAt() != null ? question.createdAt() : LocalDateTime.now()
    );
  }
}
