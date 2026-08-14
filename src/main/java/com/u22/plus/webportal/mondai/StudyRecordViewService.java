package com.u22.plus.webportal.mondai;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 講師側の学習記録閲覧に関する業務ロジックを担当するService。
 */
@Transactional
@Service
public class StudyRecordViewService {

  @Autowired
  private StudyRecordRepository studyRecordRepository;

  /**
   * 指定した生徒(userId)の学習記録を全件取得する（新しい順）。
   */
  public List<StudyRecord> getStudyRecords(String userId) {
    return studyRecordRepository.findByStudentId(userId);
  }
}
