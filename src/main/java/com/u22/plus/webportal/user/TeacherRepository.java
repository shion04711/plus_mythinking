package com.u22.plus.webportal.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * 講師データ(teacher_m)の永続化を担当するRepository。
 * password カラムは DB担当により teacher_m に追加される予定。
 */
@Repository
public class TeacherRepository {

  @Autowired
  private NamedParameterJdbcTemplate jdbc;

  /**
   * 教員IDとパスワードでログイン認証する。
   */
  public TeacherData login(String teacherId, String password) {

    final String SQL_LOGIN = "SELECT teacher_id, teacher_name FROM teacher_m "
        + "WHERE teacher_id = :teacherId AND password = :password";

    Map<String, Object> params = new HashMap<>();
    params.put("teacherId", teacherId);
    params.put("password", password);

    List<Map<String, Object>> resultList = jdbc.queryForList(SQL_LOGIN, params);

    if (resultList.size() != 1) {
      return null;
    }

    Map<String, Object> item = resultList.get(0);
    return new TeacherData(
        (String) item.get("teacher_id"),
        "****",
        (String) item.get("teacher_name"));
  }

  /**
   * 教員IDがすでに登録されているか確認する。
   */
  public boolean existsById(String teacherId) {

    final String SQL = "SELECT COUNT(*) FROM teacher_m WHERE teacher_id = :teacherId";

    Map<String, Object> params = new HashMap<>();
    params.put("teacherId", teacherId);

    Integer count = jdbc.queryForObject(SQL, params, Integer.class);
    return count != null && count > 0;
  }

  /**
   * 講師を新規登録する。
   */
  public void save(String teacherId, String password, String teacherName) {

    final String SQL_INSERT = "INSERT INTO teacher_m (teacher_id, teacher_name, password) "
        + "VALUES (:teacherId, :teacherName, :password)";

    Map<String, Object> params = new HashMap<>();
    params.put("teacherId", teacherId);
    params.put("teacherName", teacherName);
    params.put("password", password);

    jdbc.update(SQL_INSERT, params);
  }
}
