/**
 * テーブルのソート機能
 */
let currentSortColumn = -1;
let isAscending = true;

function sortTable(columnIndex) {
  const table = document.getElementById("studentTable");
  if (!table) return;

  const tbody = table.querySelector("tbody");
  const rows = Array.from(tbody.querySelectorAll("tr"));

  // 「データがありません」の行がある場合はソートしない
  if (rows.length === 1 && rows[0].cells.length === 1) return;

  // クリックした列のタイプ（text または rate）を取得
  const th = table.querySelectorAll("thead th")[columnIndex];
  if (!th) return;
  const sortType = th.getAttribute("data-sort");

  // 昇順/降順の切り替え
  if (currentSortColumn === columnIndex) {
    isAscending = !isAscending;
  } else {
    currentSortColumn = columnIndex;
    isAscending = true;
  }

  // ヘッダーの矢印アイコン更新
  document.querySelectorAll(".sort-icon").forEach(icon => icon.textContent = "⇅");
  const iconSpan = th.querySelector(".sort-icon");
  if (iconSpan) {
    iconSpan.textContent = isAscending ? "▲" : "▼";
  }

  // 行の並び替え処理
  rows.sort((rowA, rowB) => {
    const cellA = rowA.cells[columnIndex] ? rowA.cells[columnIndex].textContent.trim() : "";
    const cellB = rowB.cells[columnIndex] ? rowB.cells[columnIndex].textContent.trim() : "";

    if (sortType === "rate") {
      // 正答率のソートロジック（未入力を考慮）
      const valA = parseRateValue(cellA);
      const valB = parseRateValue(cellB);
      return isAscending ? valA - valB : valB - valA;
    } else {
      // 文字列・クラス番号のソート（自然順比較）
      return isAscending
        ? cellA.localeCompare(cellB, undefined, { numeric: true })
        : cellB.localeCompare(cellA, undefined, { numeric: true });
    }
  });

  // ソート結果をDOMに反映
  rows.forEach(row => tbody.appendChild(row));
}

/**
 * 正答率文字列（"80%", "75.5", "未入力" など）を数値に変換する関数
 */
function parseRateValue(text) {
  if (!text || text === "未入力") {
    return -Infinity; // 未入力は最下位として扱う
  }
  // '%' や記号を取り除いて数値に変換
  const num = parseFloat(text.replace(/[^0-9.]/g, ''));
  return isNaN(num) ? -Infinity : num;
}