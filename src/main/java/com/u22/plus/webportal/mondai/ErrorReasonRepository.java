package com.u22.plus.webportal.mondai;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * ミス原因マスタ(error_reason_m)の永続化を担当するRepository。
 */
@Repository
public class ErrorReasonRepository {

  @Autowired
  private NamedParameterJdbcTemplate jdbc;

  /**
   * 登録されている全ての reason_id を昇順で取得する。
   * error_reason_m に種類が追加・削除されても、この一覧を基準に集計結果を組み立てれば追随できる。
   */
  public List<Integer> findAllReasonIds() {

    final String SQL = "SELECT reason_id FROM error_reason_m ORDER BY reason_id ASC";

    return jdbc.query(SQL, new HashMap<String, Object>(),
        (rs, rowNum) -> rs.getInt("reason_id"));
  }
}
