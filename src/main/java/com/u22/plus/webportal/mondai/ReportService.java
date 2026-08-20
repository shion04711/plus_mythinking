package com.u22.plus.webportal.mondai;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * デイリーレポート・総括レポートの集計ロジックを担当するService。
 * 対象はログイン中の本人（studentId）。
 *
 * 間違えた原因の集計は QuestionRepository.countGroupByReason（DB側でGROUP BY）を使用する。
 * error_reason_m に登録されている全 reason_id (昇順) を基準に、0件の原因も歯抜けなく埋めて
 * 「件数だけの配列」として返す（HTML/JS側で reason_id 昇順の数値配列として扱いやすくするため）。
 * これにより、error_reason_m の種類数が増減してもそのまま対応できる。
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

  @Autowired
  private ErrorReasonRepository errorReasonRepository;

  private static final String DEFAULT_COMPARISON_TEXT = "前回までのデータと比較して、勉強の習慣が着実に身についてきています。";
  private static final String DEFAULT_NEXT_ADVICE_TEXT = "間違えた問題を中心に、次回はケアレスミスを減らすことを意識しましょう。";
  private static final String DEFAULT_EXAM_TEXT = "これまでの学習を振り返り、苦手分野を重点的に復習しましょう。";

  /**
   * デイリーレポートを作成する。
   * 「当日分」は created_at が今日の日付である記録の合計とする。
   *
   * @param studentId ログイン中の生徒ID
   * @param courseId  ログイン中の生徒が所属するコースID
   */
  public DailyReportView getDailyReport(String studentId, Integer courseId) {

    List<StudyRecord> records = studyRecordRepository.findByStudentId(studentId);

    List<StudyRecord> todayRecords = records.stream()
        .filter(this::isToday)
        .toList();

    int dailyTime = sumStudyMinutes(todayRecords);
    int dailyPrint = sumPrintCount(todayRecords);

    int allTime = sumStudyMinutes(records);
    int allMaisu = sumPrintCount(records);

    // error_reason_m に登録されている全reason_id（昇順）を基準に、歯抜けなく件数を埋める
    List<Integer> allReasonIds = errorReasonRepository.findAllReasonIds();

    // 累計（全期間）の原因別集計
    List<ReasonCountData> rawCounts =
        questionRepository.countGroupByReason(studentId, courseId, null, null);
    List<Long> reasonCounts = fillMissingReasons(allReasonIds, rawCounts);

    // 当日分の原因別集計
    LocalDateTime startOfToday = LocalDate.now().atStartOfDay();
    LocalDateTime endOfToday = LocalDate.now().atTime(LocalTime.MAX);
    List<ReasonCountData> rawDailyCounts =
        questionRepository.countGroupByReason(studentId, courseId, startOfToday, endOfToday);
    List<Long> dailyReasonCounts = fillMissingReasons(allReasonIds, rawDailyCounts);

    return new DailyReportView(
        dailyTime,
        dailyPrint,
        allTime,
        allMaisu,
        reasonCounts,
        dailyReasonCounts,
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

  /**
   * DBのGROUP BY結果（該当があった原因のみ）を、error_reason_m の全reason_id分に
   * 歯抜けなく展開し、reason_id昇順の「件数だけの配列」として返す。
   *
   * 戻り値のインデックス i は、allReasonIds の i番目の reason_id に対応する
   * （error_reason_m の reason_id が 1 から連番であれば、インデックス = reason_id - 1）。
   * error_reason_m の種類が増減した場合も、allReasonIds を取得し直すだけで自動的に追随する。
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
   * 学習記録が今日作成されたものかどうかを判定する。
   */
  private boolean isToday(StudyRecord record) {
    return record.createdAt() != null && record.createdAt().toLocalDate().isEqual(LocalDate.now());
  }
}
