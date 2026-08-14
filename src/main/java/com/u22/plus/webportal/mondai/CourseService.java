package com.u22.plus.webportal.mondai;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * コース登録（講師側）の業務ロジックを担当するService。
 *
 * 主なチェック内容:
 *  - courseId の重複チェック
 *  - 開始日・終了日の形式チェックと前後関係チェック
 */
@Transactional
@Service
public class CourseService {

  @Autowired
  private CourseRepository courseRepository;

  public Course registCourse(CourseForm form) {

    validateIds(form);

    LocalDate startDate = parseDate(form.getStart(), "開始日");
    LocalDate endDate = parseDate(form.getEnd(), "終了日");

    Integer courseId = Integer.valueOf(form.getCourseId().trim());
    if (startDate.isAfter(endDate)) {
      throw new CourseRegistException("開始日は終了日より前の日付にしてください。");
    }
    
    Course course = new Course(
        courseId,
        form.getCourseName().trim(),
        startDate,
        endDate,
      "teacherId");

    return courseRepository.save(course);
  }

  private void validateIds(CourseForm form) {

    String courseId = form.getCourseId();

    if (courseId == null || courseId.isBlank()) {
      throw new CourseRegistException("コースIDが入力されていません。");
    }

    if (courseRepository.existsById(courseId.trim())) {
      throw new CourseRegistException("コースID「" + courseId + "」は既に登録されています。別のIDを指定してください。");
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
