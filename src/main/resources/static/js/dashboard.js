/**
 * ダッシュボード・生徒選択＆グラフ表示機能 (dashboard.js)
 */
let reasonChart = null;

// テスト用のデフォルトデータ [ケアレスミス, 理解不足, 時間不足, その他]
const DEFAULT_TEST_DATA = [40, 30, 20, 10];

document.addEventListener("DOMContentLoaded", () => {
  // Chart.js の初期化
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
          // ★ 円の半径をキャンバス領域限界（90%）まで大きく表示
          radius: '90%'
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        // ★ キャンバス周囲の不要なパディングを排除
        layout: {
          padding: 0
        },
        plugins: {
          // ★ 下部の凡例（内訳）の余白詰め・コンパクト化
          legend: {
            display: true,
            position: 'bottom',
            labels: {
              boxWidth: 10,     // アイコンの幅を少し小さく
              boxHeight: 10,    // アイコンの高さ
              padding: 6,       // 凡例同士の間隔・上下余白をギリギリまで詰める
              font: {
                size: 11        // フォントを少しだけ小さくして収まりを調整
              }
            }
          },
          // ★ タイトル下の余白を詰めて円グラフの領域を広げる
          title: {
            display: true,
            text: '間違えた原因',
            padding: {
              top: 0,
              bottom: 4
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
 * 生徒ラジオボタン選択時の情報更新関数
 */
function selectStudent(radio) {
  if (!radio) return;

  // 1. ラジオボタンの data 属性から値を取得
  const name = radio.getAttribute('data-name') || '-';
  const classNum = radio.getAttribute('data-class') || '-';
  const rate = radio.getAttribute('data-rate') || '-';
  const info = radio.getAttribute('data-info') || '-';

  // 2. 上部の表示欄を更新
  const elName = document.getElementById('displayStudentName');
  const elClass = document.getElementById('displayClassNum');
  const elRate = document.getElementById('displayAccuracyRate');
  const elInfo = document.getElementById('displayStudentInfo');

  if (elName) elName.textContent = name;
  if (elClass) elClass.textContent = classNum;
  if (elRate) elRate.textContent = rate;
  if (elInfo) elInfo.textContent = info;

  // 3. 円グラフのデータを更新
  if (reasonChart) {
    const careless = parseInt(radio.getAttribute('data-careless'));
    const understanding = parseInt(radio.getAttribute('data-understanding'));
    const time = parseInt(radio.getAttribute('data-time'));
    const other = parseInt(radio.getAttribute('data-other'));

    let chartData = [
      isNaN(careless) ? 0 : careless,
      isNaN(understanding) ? 0 : understanding,
      isNaN(time) ? 0 : time,
      isNaN(other) ? 0 : other
    ];

    // 数値の合計が 0 の場合はテスト用データを割り当て
    const total = chartData.reduce((sum, val) => sum + val, 0);
    if (total === 0) {
      chartData = DEFAULT_TEST_DATA;
    }

    reasonChart.data.datasets[0].data = chartData;
    reasonChart.update();
  }
}