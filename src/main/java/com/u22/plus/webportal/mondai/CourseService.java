package com.u22.plus.webportal.mondai;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * コース登録（講師側）の業務ロジックを担当するService。
 *
 * course_id は course_m テーブルの SERIAL（自動採番）のため、
 * フォームからの指定は受け取らない。
 *
 * 主なチェック内容:
 *  - 開始日・終了日の形式チェックと前後関係チェック
 */
@Transactional
@Service
public class CourseService {

  @Autowired
  private CourseRepository courseRepository;

  /**
   * コースを登録する。
   *
   * @param form      フォーム入力値
   * @param teacherId ログイン中の講師ID（コースの担当講師として登録される）
   */
  public Course registCourse(CourseForm form, String teacherId) {

    validate(form, teacherId);

    LocalDate startDate = parseDate(form.getStart(), "開始日");
    LocalDate endDate = parseDate(form.getEnd(), "終了日");

    if (startDate.isAfter(endDate)) {
      throw new CourseRegistException("開始日は終了日より前の日付にしてください。");
    }

    Course course = new Course(
        null,
        form.getCourseName().trim(),
        startDate,
        endDate,
        teacherId);

    return courseRepository.save(course);
  }

  private void validate(CourseForm form, String teacherId) {

    if (teacherId == null || teacherId.isBlank()) {
      throw new CourseRegistException("ログイン情報が取得できませんでした。再度ログインしてください。");
    }

    if (form.getCourseName() == null || form.getCourseName().isBlank()) {
      throw new CourseRegistException("コース名が入力されていません。");
    }
  }

  private LocalDate parseDate(String value, String fieldName) {

    if (value == null || value.isBlank()) {
      throw new CourseRegistException(fieldName + "が入力されていません。");
    }

    try {
      return LocalDate.parse(value.trim());
    } catch (DateTimeParseException e) {
      throw new CourseRegistException(fieldName + "の形式が不正です。");
    }
  }
}
