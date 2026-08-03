package com.u22.plus.webportal.mondai;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * 問題データの永続化を担当するRepository。
 * テーブル定義・SQL詳細はDB担当と調整の上、確定させること。
 * ここではロジック側（Service）が必要とする最小限のメソッドのみを用意している。
 */
@Repository
public class QuestionRepository {

  @Autowired
  private NamedParameterJdbcTemplate jdbc;

  /**
   * questionId が既に登録されているか確認する。
   */
  public boolean existsById(String questionId) {

    final String SQL = "SELECT COUNT(*) FROM question_m WHERE question_id = :questionId";

    Map<String, Object> params = new HashMap<>();
    params.put("questionId", questionId);

    Integer count = jdbc.queryForObject(SQL, params, Integer.class);
    return count != null && count > 0;
  }

  /**
   * fieldId（分野）が分野マスタに存在するか確認する。
   * 分野マスタのテーブル名・カラム名はDB担当の実装に合わせて修正すること。
   */
  public boolean existsFieldId(Long fieldId) {

    final String SQL = "SELECT COUNT(*) FROM field_m WHERE field_id = :fieldId";

    Map<String, Object> params = new HashMap<>();
    params.put("fieldId", fieldId);

    Integer count = jdbc.queryForObject(SQL, params, Integer.class);
    return count != null && count > 0;
  }

  /**
   * 問題を1件保存する。
   */
  public Question save(Question question) {

    final String SQL_INSERT = "INSERT INTO question_m (question_id, location, field_id, created_at) "
        + "VALUES (:questionId, :location, :fieldId, :createdAt)";

    Map<String, Object> params = new HashMap<>();
    params.put("questionId", question.questionId());
    params.put("location", question.location());
    params.put("fieldId", question.fieldId());
    params.put("createdAt", question.createdAt());

    jdbc.update(SQL_INSERT, params);

    return question;
  }

  /**
   * questionId で問題を1件取得する。
   */
  public Optional<Question> findById(String questionId) {

    final String SQL = "SELECT question_id, location, field_id, created_at FROM question_m WHERE question_id = :questionId";

    Map<String, Object> params = new HashMap<>();
    params.put("questionId", questionId);

    List<Map<String, Object>> resultList = jdbc.queryForList(SQL, params);

    if (resultList.size() != 1) {
      return Optional.empty();
    }

    Map<String, Object> item = resultList.get(0);
    Question question = new Question(
        (String) item.get("question_id"),
        (String) item.get("location"),
        ((Number) item.get("field_id")).longValue(),
        (LocalDateTime) item.get("created_at"));

    return Optional.of(question);
  }
}
