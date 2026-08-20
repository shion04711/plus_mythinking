package com.u22.plus.webportal.mondai;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.u22.plus.webportal.user.StudentData;
import com.u22.plus.webportal.user.StudentRepository;

/**
 * 講師向け分析ダッシュボード(teach/bunseki.html, /dashboard)の集計ロジックを担当するService。
 *
 * ログイン中の講師が担当するコースに所属する生徒のみを対象とする。
 */
@Transactional
@Service
public class DashboardService {

  @Autowired
  private CourseRepository courseRepository;

  @Autowired
  private StudentRepository studentRepository;

  @Autowired
  private QuestionRepository questionRepository;

  @Autowired
  private ErrorReasonRepository errorReasonRepository;

  private static final String DEFAULT_INFO = "特記事項はありません。";

  /**
   * ログイン中の講師が担当するコースに所属する生徒の、ダッシュボード表示用データ一覧を取得する。
   */
  public List<DashboardStudentView> getDashboardStudents(String teacherId) {

    List<Course> courses = courseRepository.findByTeacherId(teacherId);

    List<Integer> courseIds = courses.stream()
        .map(Course::courseId)
        .toList();

    List<StudentData> students = studentRepository.findByCourseIds(courseIds);

    List<Integer> allReasonIds = errorReasonRepository.findAllReasonIds();

    List<DashboardStudentView> views = new ArrayList<>();
    for (StudentData student : students) {
      views.add(toDashboardStudentView(student, allReasonIds));
    }

    return views;
  }

  private DashboardStudentView toDashboardStudentView(StudentData student, List<Integer> allReasonIds) {

    String studentId = student.studentId();
    Integer courseId = student.courseId();

    // 当日分
    LocalDateTime startOfToday = LocalDate.now().atStartOfDay();
    LocalDateTime endOfToday = LocalDate.now().atTime(LocalTime.MAX);
    List<ReasonCountData> rawDailyCounts =
        questionRepository.countGroupByReason(studentId, courseId, startOfToday, endOfToday);
    List<Long> dailyReasonCounts = fillMissingReasons(allReasonIds, rawDailyCounts);
    long dailyMissCount = dailyReasonCounts.stream().mapToLong(Long::longValue).sum();

    // 累計
    List<ReasonCountData> rawSummaryCounts =
        questionRepository.countGroupByReason(studentId, courseId, null, null);
    List<Long> summaryReasonCounts = fillMissingReasons(allReasonIds, rawSummaryCounts);
    long summaryMissCount = summaryReasonCounts.stream().mapToLong(Long::longValue).sum();

    return new DashboardStudentView(
        studentId,
        student.studentName(),
        student.className(),
        dailyMissCount,
        dailyReasonCounts,
        DEFAULT_INFO,
        summaryMissCount,
        summaryReasonCounts,
        DEFAULT_INFO);
  }

  /**
   * DBのGROUP BY結果を、error_reason_m の全reason_id分に歯抜けなく展開し、
   * reason_id昇順の「件数だけの配列」として返す。（ReportServiceと同じロジック）
   */
  private List<Long> fillMissingReasons(List<Integer> allReasonIds, List<ReasonCountData> rawCounts) {

    List<Long> filled = new ArrayList<>();

    for (Integer reasonId : allReasonIds) {

      long count = rawCounts.stream()
          .filter(data -> data.reasonId().equals(reasonId))
          .findFirst()
          .map(ReasonCountData::count)
          .orElse(0L);

      filled.add(count);
    }

    return filled;
  }
}
