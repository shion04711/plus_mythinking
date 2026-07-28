package com.u22.plus.webportal.mondai;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * コースデータの永続化を担当するRepository。
 * テーブル定義・SQL詳細はDB担当と調整の上、確定させること。
 */
@Repository
public class CourseRepository {

  @Autowired
  private NamedParameterJdbcTemplate jdbc;

  /**
   * courseId が既に登録されているか確認する。
   */
  public boolean existsById(String courseId) {

    final String SQL = "SELECT COUNT(*) FROM course_m WHERE course_id = :courseId";

    Map<String, Object> params = new HashMap<>();
    params.put("courseId", courseId);

    Integer count = jdbc.queryForObject(SQL, params, Integer.class);
    return count != null && count > 0;
  }

  /**
   * コースを1件保存する。
   */
  public Course save(Course course) {

    final String SQL_INSERT = "INSERT INTO course_m (course_id, course_name, start_date, end_date) "
        + "VALUES (:courseId, :courseName, :startDate, :endDate)";

    Map<String, Object> params = new HashMap<>();
    params.put("courseId", course.courseId());
    params.put("courseName", course.courseName());
    params.put("startDate", course.startDate());
    params.put("endDate", course.endDate());

    jdbc.update(SQL_INSERT, params);

    return course;
  }
}
