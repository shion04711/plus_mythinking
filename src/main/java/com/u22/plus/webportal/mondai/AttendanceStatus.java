package com.u22.plus.webportal.mondai;

/**
 * 出席状況。
 * HTML側のラジオボタン(name="atten")のvalue属性に対応させること。
 * 例: <input type="radio" name="atten" value="ATTEN">出席
 *     <input type="radio" name="atten" value="DENY">欠席
 */
public enum AttendanceStatus {
  ATTEN, // 出席
  DENY   // 欠席
}
