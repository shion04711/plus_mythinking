package com.u22.plus.webportal.mondai;

import java.time.LocalDate;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * デイリーレポート・総括レポートの集計ロジックを担当するService。
 * 対象はログイン中の本人（studentId）。
 *
 * 比較コメント・アドバイス文（sikentext相当）は現時点では固定文。
 * 今後、過去データとの比較ロジックや template_m（定型文マスタ）と連携する想定。
 */
@Transactional
@Service
public class ReportService {

  @Autowired
  private StudyRecordRepository studyRecordRepository;

  @Autowired
  private QuestionRepository questionRepository;

  private static final String DEFAULT_COMPARISON_TEXT = "前回までのデータと比較して、勉強の習慣が着実に身についてきています。";
  private static final String DEFAULT_NEXT_ADVICE_TEXT = "間違えた問題を中心に、次回はケアレスミスを減らすことを意識しましょう。";
  private static final String DEFAULT_EXAM_TEXT = "これまでの学習を振り返り、苦手分野を重点的に復習しましょう。";

  /**
   * デイリーレポートを作成する。
   * 「当日分」は created_at が今日の日付である記録の合計とする。
   */
  public DailyReportView getDailyReport(String studentId) {

    List<StudyRecord> records = studyRecordRepository.findByStudentId(studentId);
    List<Question> mistakes = questionRepository.findByStudentId(studentId);

    List<StudyRecord> todayRecords = records.stream()
        .filter(this::isToday)
        .toList();

    int dailyTime = sumStudyMinutes(todayRecords);
    int dailyPrint = sumPrintCount(todayRecords);

    int allTime = sumStudyMinutes(records);
    int allMaisu = sumPrintCount(records);

    Map<ErrorReason, Long> reasonCounts = countReasons(mistakes);

    return new DailyReportView(
        dailyTime,
        dailyPrint,
        allTime,
        allMaisu,
        reasonCounts,
        DEFAULT_COMPARISON_TEXT,
        DEFAULT_NEXT_ADVICE_TEXT);
  }

  /**
   * 総括レポートを作成する。
   */
  public SummaryReportView getSummaryReport(String studentId) {

    List<StudyRecord> records = studyRecordRepository.findByStudentId(studentId);

    int allTime = sumStudyMinutes(records);
    int allMaisu = sumPrintCount(records);

    return new SummaryReportView(allTime, allMaisu, DEFAULT_EXAM_TEXT);
  }

  private int sumStudyMinutes(List<StudyRecord> records) {
    int total = 0;
    for (StudyRecord record : records) {
      total += record.studyMinutes() != null ? record.studyMinutes() : 0;
    }
    return total;
  }

  private int sumPrintCount(List<StudyRecord> records) {
    int total = 0;
    for (StudyRecord record : records) {
      total += record.printCount() != null ? record.printCount() : 0;
    }
    return total;
  }

  /**
   * 間違えた問題(Question)を原因ごとに集計する。
   */
  private Map<ErrorReason, Long> countReasons(List<Question> mistakes) {

    Map<ErrorReason, Long> counts = new EnumMap<>(ErrorReason.class);
    for (ErrorReason reason : ErrorReason.values()) {
      counts.put(reason, 0L);
    }

    for (Question mistake : mistakes) {
      if (mistake.reasonId() == null) {
        continue;
      }
      ErrorReason reason = ErrorReason.fromId(mistake.reasonId());
      counts.merge(reason, 1L, Long::sum);
    }

    return counts;
  }

  /**
   * 学習記録が今日作成されたものかどうかを判定する。
   */
  private boolean isToday(StudyRecord record) {
    return record.createdAt() != null && record.createdAt().toLocalDate().isEqual(LocalDate.now());
  }
}
