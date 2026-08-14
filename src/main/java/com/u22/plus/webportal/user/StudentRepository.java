package com.u22.plus.webportal.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * 生徒データ(student_m)の永続化を担当するRepository。
 * password カラムは DB担当により student_m に追加される予定。
 */
@Repository
public class StudentRepository {

  @Autowired
  private NamedParameterJdbcTemplate jdbc;

  /**
   * 学籍番号とパスワードでログイン認証する。
   */
  public StudentData login(String studentId, String password) {

    final String SQL_LOGIN = "SELECT student_id, student_name, course_id, class_name, student_number "
        + "FROM student_m WHERE student_id = :studentId AND password = :password";

    Map<String, Object> params = new HashMap<>();
    params.put("studentId", studentId);
    params.put("password", password);

    List<Map<String, Object>> resultList = jdbc.queryForList(SQL_LOGIN, params);

    if (resultList.size() != 1) {
      return null;
    }

    return mapRow(resultList.get(0));
  }

  /**
   * 学籍番号がすでに登録されているか確認する。
   */
  public boolean existsById(String studentId) {

    final String SQL = "SELECT COUNT(*) FROM student_m WHERE student_id = :studentId";

    Map<String, Object> params = new HashMap<>();
    params.put("studentId", studentId);

    Integer count = jdbc.queryForObject(SQL, params, Integer.class);
    return count != null && count > 0;
  }

  /**
   * 生徒を新規登録する。
   */
  public void save(String studentId, String password, String studentName, Integer courseId) {

    final String SQL_INSERT = "INSERT INTO student_m (student_id, course_id, student_name, password) "
        + "VALUES (:studentId, :courseId, :studentName, :password)";

    Map<String, Object> params = new HashMap<>();
    params.put("studentId", studentId);
    params.put("courseId", courseId);
    params.put("studentName", studentName);
    params.put("password", password);

    jdbc.update(SQL_INSERT, params);
  }

  /**
   * 生徒を全件取得する（講師側の生徒一覧画面で使用）。
   */
  public List<StudentData> findAll() {

    final String SQL_LIST = "SELECT student_id, student_name, course_id, class_name, student_number "
        + "FROM student_m ORDER BY student_name";

    List<Map<String, Object>> resultList = jdbc.queryForList(SQL_LIST, new HashMap<>());

    List<StudentData> students = new ArrayList<>();
    for (Map<String, Object> row : resultList) {
      students.add(mapRow(row));
    }

    return students;
  }

  private StudentData mapRow(Map<String, Object> row) {

    Object courseIdObj = row.get("course_id");
    Integer courseId = courseIdObj != null ? ((Number) courseIdObj).intValue() : null;

    Object studentNumberObj = row.get("student_number");
    Integer studentNumber = studentNumberObj != null ? ((Number) studentNumberObj).intValue() : null;

    return new StudentData(
        (String) row.get("student_id"),
        "****",
        (String) row.get("student_name"),
        courseId,
        (String) row.get("class_name"),
        studentNumber);
  }
}
