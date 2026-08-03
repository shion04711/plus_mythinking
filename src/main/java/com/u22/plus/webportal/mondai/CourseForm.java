package com.u22.plus.webportal.mondai;

/**
 * コース作成フォームの入力値。
 * HTML側の name="course_id" / "course_name" / "start" / "end" にバインドされる。
 */
public class CourseForm {

  private String courseId;

  private String courseName;

  /** 開始日（HTML側は type="date" のためフォーム上は文字列 "yyyy-MM-dd"） */
  private String start;

  /** 終了日（同上） */
  private String end;

  public String getCourseId() {
    return courseId;
  }

  public void setCourseId(String courseId) {
    this.courseId = courseId;
  }

  public String getCourseName() {
    return courseName;
  }

  public void setCourseName(String courseName) {
    this.courseName = courseName;
  }

  public String getStart() {
    return start;
  }

  public void setStart(String start) {
    this.start = start;
  }

  public String getEnd() {
    return end;
  }

  public void setEnd(String end) {
    this.end = end;
  }
}
