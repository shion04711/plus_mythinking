/**
 * ダッシュボード・生徒選択＆グラフ表示機能 (dashboard.js)
 */
let reasonChart = null;
let currentMode = 'daily'; // 現在のモード ('daily' または 'summary')

// テスト用のデフォルトデータ [ケアレスミス, 理解不足, 時間不足, その他]
const DEFAULT_TEST_DATA = [40, 30, 20, 10];

document.addEventListener("DOMContentLoaded", () => {
  // --- Chart.js の初期化 ---
  const canvas = document.getElementById('reasonChart');
  if (canvas) {
    const ctx = canvas.getContext('2d');
    reasonChart = new Chart(ctx, {
      type: 'pie',
      data: {
        labels: ['ケアレスミス', '理解不足', '時間不足', 'その他'],
        datasets: [{
          data: DEFAULT_TEST_DATA,
          backgroundColor: [
            '#ff6384',
            '#36a2eb',
            '#ffce56',
            '#cc65fe'
          ],
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
              padding: 6,
              font: { size: 11 }
            }
          },
          title: {
            display: true,
            text: '間違えた原因',
            padding: { top: 0, bottom: 4 }
          },
          // ★ ツールチップ表示のカスタマイズ（件数 と 百分率 % を両方表示）
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

  // 1. モードに応じた data 属性から値を取得
  const name = radio.getAttribute('data-name') || '-';
  const classNum = radio.getAttribute('data-class') || '-';
  const rate = radio.getAttribute(isSummary ? 'data-summary-rate' : 'data-daily-rate') || '-';
  const info = radio.getAttribute(isSummary ? 'data-summary-info' : 'data-daily-info') || '-';

  // 2. 上部のテキスト表示欄を更新
  const elName = document.getElementById('displayStudentName');
  const elClass = document.getElementById('displayClassNum');
  const elRate = document.getElementById('displayAccuracyRate');
  const elInfo = document.getElementById('displayStudentInfo');

  if (elName) elName.textContent = name;
  if (elClass) elClass.textContent = classNum;
  if (elRate) elRate.textContent = rate;
  if (elInfo) elInfo.textContent = info;

  // 3. 円グラフの件数データを取得
  if (reasonChart) {
    const careless = parseInt(radio.getAttribute(isSummary ? 'data-summary-careless' : 'data-daily-careless')) || 0;
    const understanding = parseInt(radio.getAttribute(isSummary ? 'data-summary-understanding' : 'data-daily-understanding')) || 0;
    const time = parseInt(radio.getAttribute(isSummary ? 'data-summary-time' : 'data-daily-time')) || 0;
    const other = parseInt(radio.getAttribute(isSummary ? 'data-summary-other' : 'data-daily-other')) || 0;

    let rawCounts = [careless, understanding, time, other];
    const total = rawCounts.reduce((sum, val) => sum + val, 0);

    // ★ 件数の合計が 0 の場合（全正解 or データなし）はテスト用データをダミー表示
    if (total === 0) {
      reasonChart.data.datasets[0].data = DEFAULT_TEST_DATA;
    } else {
      // 件数データをそのまま渡す（Chart.jsが自動で円グラフの割合を決定＆ツールチップで%表示）
      reasonChart.data.datasets[0].data = rawCounts;
    }

    reasonChart.update();
  }
}