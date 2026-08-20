/**
 * ダッシュボード・生徒選択＆グラフ表示機能 (dashboard.js)
 */
let reasonChart = null;
let currentMode = 'daily'; // 現在のモード ('daily' または 'summary')

// DB(error_reason_m) の8項目に対応するラベル
const REASON_LABELS = [
  '理解不足',
  '知識不足',
  '時間切れ',
  '思い込み',
  '読み間違い',
  '書き間違い',
  '計算ミス',
  'ケアレスミス（その他）'
];

// 8項目用のカラーパレット
const REASON_COLORS = [
  '#36a2eb', // 1: 理解不足
  '#4bc0c0', // 2: 知識不足
  '#ffce56', // 3: 時間切れ
  '#9966ff', // 4: 思い込み
  '#ff9f40', // 5: 読み間違い
  '#e75480', // 6: 書き間違い
  '#ff6384', // 7: 計算ミス
  '#c9cbcf'  // 8: ケアレスミス（その他）
];

// テスト用のデフォルトデータ（全8要素）
const DEFAULT_TEST_DATA = [5, 3, 2, 1, 4, 2, 6, 1];

document.addEventListener("DOMContentLoaded", () => {
  // --- Chart.js の初期化 ---
  const canvas = document.getElementById('reasonChart');
  if (canvas) {
    const ctx = canvas.getContext('2d');
    reasonChart = new Chart(ctx, {
      type: 'pie',
      data: {
        labels: REASON_LABELS,
        datasets: [{
          data: DEFAULT_TEST_DATA,
          backgroundColor: REASON_COLORS,
          borderWidth: 1,
          radius: '90%'
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        layout: {
          padding: 0
        },
        plugins: {
          legend: {
            display: true,
            position: 'bottom',
            labels: {
              boxWidth: 10,
              boxHeight: 10,
              padding: 8,
              font: { size: 12 }
            }
          },
          title: {
            display: true,
            text: '間違えた原因',
            padding: { top: 0, bottom: 4 },
            font: {
              size: 25
            }
          },
          // ツールチップ表示のカスタマイズ（件数 と 百分率 % を両方表示）
          tooltip: {
            callbacks: {
              label: function(context) {
                const label = context.label || '';
                const value = context.parsed || 0; // 個々の件数
                
                // グラフ内の合計値を計算
                const total = context.dataset.data.reduce((sum, val) => sum + val, 0);
                
                // 百分率（%）を計算（小数第1位まで）
                const percentage = total > 0 ? ((value / total) * 100).toFixed(1) : 0;
                
                return `${label}: ${value}件 (${percentage}%)`;
              }
            }
          }
        }
      }
    });
  }

  // 初期読み込み時に選択中のラジオボタンがあれば表示を反映
  const checkedRadio = document.querySelector('input[name="studentId"]:checked');
  if (checkedRadio) {
    selectStudent(checkedRadio);
  }
});

/**
 * デイリー / 総括 切替処理
 */
function toggleMode(mode) {
  currentMode = mode;

  // テーブル表示の更新
  const rows = document.querySelectorAll('#studentTable tbody tr');
  rows.forEach(row => {
    const radio = row.querySelector('input[name="studentId"]');
    if (!radio) return;

    const rateCell = row.querySelector('.rate-cell');
    const infoCell = row.querySelector('.info-cell');

    if (mode === 'summary') {
      if (rateCell) rateCell.textContent = radio.getAttribute('data-summary-rate') || '-';
      if (infoCell) infoCell.textContent = radio.getAttribute('data-summary-info') || '-';
    } else {
      if (rateCell) rateCell.textContent = radio.getAttribute('data-daily-rate') || '-';
      if (infoCell) infoCell.textContent = radio.getAttribute('data-daily-info') || '-';
    }
  });

  // 選択中生徒のグラフ・詳細表示を更新
  const selectedRadio = document.querySelector('input[name="studentId"]:checked');
  if (selectedRadio) {
    selectStudent(selectedRadio);
  }
}

/**
 * 生徒ラジオボタン選択時の情報 ＆ 円グラフ更新関数
 */
function selectStudent(radio) {
  if (!radio) return;

  const isSummary = currentMode === 'summary';

  // 生徒情報を取得
  const name = radio.getAttribute('data-name') || '-';
  const classNum = radio.getAttribute('data-class') || '-';

  const missCount = radio.getAttribute(
    isSummary ? 'data-summary-miss' : 'data-daily-miss'
  ) || '0';

  const info = radio.getAttribute(
    isSummary ? 'data-summary-info' : 'data-daily-info'
  ) || '-';

  // 表示の更新
  const elName = document.getElementById('displayStudentName');
  const elClass = document.getElementById('displayClassNum');
  const elMissCount = document.getElementById('displayMissCount');
  const elInfo = document.getElementById('displayStudentInfo');

  if (elName) elName.textContent = name;
  if (elClass) elClass.textContent = classNum;
  if (elMissCount) elMissCount.textContent = missCount + '回';
  if (elInfo) elInfo.textContent = info;

  // 円グラフの更新
  if (reasonChart) {
    // HTML属性からリストデータを取得（例: "5,3,2,1,4,2,6,1" や JSON文字列 "[5,3,...]"）
    const rawDataAttr = radio.getAttribute(
      isSummary ? 'data-summary-reasons' : 'data-daily-reasons'
    );

    let reasonCounts = [];

    if (rawDataAttr) {
      try {
        // JSON形式またはカンマ区切り文字列のパースに対応
        reasonCounts = typeof rawDataAttr === 'string' && rawDataAttr.startsWith('[')
          ? JSON.parse(rawDataAttr)
          : rawDataAttr.split(',').map(v => parseInt(v.trim(), 10) || 0);
      } catch (e) {
        console.error("データのパースに失敗しました:", e);
        reasonCounts = [];
      }
    }

    // 8要素になるよう補正（足りない部分は0埋め）
    while (reasonCounts.length < 8) {
      reasonCounts.push(0);
    }

    const total = reasonCounts.reduce((sum, val) => sum + val, 0);

    // 合計が0の場合はデフォルトデータを表示、データがあればそのままセット
    if (total === 0) {
      reasonChart.data.datasets[0].data = DEFAULT_TEST_DATA;
    } else {
      reasonChart.data.datasets[0].data = reasonCounts;
    }

    reasonChart.update();
  }
}