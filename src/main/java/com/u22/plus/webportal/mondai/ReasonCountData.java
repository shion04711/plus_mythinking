package com.u22.plus.webportal.mondai;

/**
 * 円グラフを作るため、原因ごとに件数をカウントするのに使います
 */
public record ReasonCountData(
    /** 原因ID*/
    Integer reasonId,
    /** 件数 */
    long count
) {
}