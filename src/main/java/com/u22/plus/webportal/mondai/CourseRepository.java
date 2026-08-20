package com.u22.plus.webportal.mondai;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.RowMapper;

/**
 * コースデータの永続化を担当するRepository。
 * テーブル定義・SQL詳細はDB担当と調整の上、確定させること。
 */
@Repository
public class CourseRepository {

  @Autowired
  private NamedParameterJdbcTemplate jdbc;

  private final RowMapper<Course> courseRowMapper = (rs, rowNum) -> new Course(
      rs.getInt("course_id"),
      rs.getString("course_name"),
      rs.getObject("start_date", LocalDate.class),
      rs.getObject("end_date", LocalDate.class),
      rs.getString("teacher_id")
  );

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
   * IDでコースを1件取得する。
   */
  public Course findById(Integer courseId) {

    final String SQL = "SELECT course_id, course_name, start_date, end_date, teacher_id "
        + "FROM course_m WHERE course_id = :courseId";

    Map<String, Object> params = new HashMap<>();
    params.put("courseId", courseId);

    List<Course> list = jdbc.query(SQL, params, courseRowMapper);
    return list.isEmpty() ? null : list.get(0);
  }


  /**
   * コースを全件取得する。
   */
  public List<Course> findAll() {

    final String SQL = "SELECT course_id, course_name, start_date, end_date, teacher_id FROM course_m ORDER BY course_id";

    return jdbc.query(SQL, courseRowMapper);
  }


  /**
   * 指定した講師が担当するコースを全件取得する。
   * 講師側の生徒一覧・ダッシュボード画面で使用する。
   */
  public List<Course> findByTeacherId(String teacherId) {

    final String SQL = "SELECT course_id, course_name, start_date, end_date, teacher_id "
        + "FROM course_m WHERE teacher_id = :teacherId ORDER BY course_id";

    Map<String, Object> params = new HashMap<>();
    params.put("teacherId", teacherId);

    return jdbc.query(SQL, params, courseRowMapper);
  }


  /**
   * コースを1件保存する。
   */
  public Course save(Course course) {

    final String SQL_INSERT = "INSERT INTO course_m (course_name, start_date, end_date, teacher_id) "
        + "VALUES (:courseName, :startDate, :endDate, :teacherId)";

    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue("courseName", course.courseName());
    params.addValue("startDate", course.startDate());
    params.addValue("endDate", course.endDate());
    params.addValue("teacherId", course.teacherId());
    KeyHolder keyHolder = new GeneratedKeyHolder();
    
    jdbc.update(SQL_INSERT, params, keyHolder, new String[] { "course_id" });

    Integer generatedId = keyHolder.getKey().intValue();

    // 採番された ID を含めた新しい Course レコードを返す
    return new Course(
        generatedId,
        course.courseName(),
        course.startDate(),
        course.endDate(),
        course.teacherId()
    );
  }
}
