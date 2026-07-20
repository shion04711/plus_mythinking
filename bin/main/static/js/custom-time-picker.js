/**
 * 10分刻みの時刻セレクトを生成する
 *
 * 使い方:
 * <div class="time-picker" data-name="starttime" data-min-hour="0" data-max-hour="12"></div>
 *
 * ページ内の [data-name] を持つ .time-picker 要素をすべて自動的に
 * 「時」「分」の2つの<select>に置き換える。
 * 分は0,10,20,30,40,50の10分刻みのみ表示する。
 */
(function () {
  function buildPicker(container) {
    const name = container.dataset.name || 'time';
    const minHour = parseInt(container.dataset.minHour ?? '0', 10);
    const maxHour = parseInt(container.dataset.maxHour ?? '23', 10);
    const minuteStep = parseInt(container.dataset.minuteStep ?? '10', 10);

    const hourSelect = document.createElement('select');
    hourSelect.name = `${name}Hour`;
    hourSelect.className = 'time-picker__select';
    hourSelect.setAttribute('aria-label', '時');

    for (let h = minHour; h <= maxHour; h++) {
      const opt = document.createElement('option');
      opt.value = String(h).padStart(2, '0');
      opt.textContent = String(h).padStart(2, '0');
      hourSelect.appendChild(opt);
    }

    const separator = document.createElement('span');
    separator.className = 'time-picker__separator';
    separator.textContent = ':';

    const minuteSelect = document.createElement('select');
    minuteSelect.name = `${name}Minute`;
    minuteSelect.className = 'time-picker__select';
    minuteSelect.setAttribute('aria-label', '分');

    for (let m = 0; m < 60; m += minuteStep) {
      const opt = document.createElement('option');
      opt.value = String(m).padStart(2, '0');
      opt.textContent = String(m).padStart(2, '0');
      minuteSelect.appendChild(opt);
    }

    // フォーム送信用に "HH:mm" を1つの値としてまとめる hidden input
    const hiddenInput = document.createElement('input');
    hiddenInput.type = 'hidden';
    hiddenInput.name = name;

    function syncHidden() {
      hiddenInput.value = `${hourSelect.value}:${minuteSelect.value}`;
    }

    hourSelect.addEventListener('change', syncHidden);
    minuteSelect.addEventListener('change', syncHidden);
    syncHidden();

    container.innerHTML = '';
    container.classList.add('time-picker');
    container.appendChild(hourSelect);
    container.appendChild(separator);
    container.appendChild(minuteSelect);
    container.appendChild(hiddenInput);
  }

  document.addEventListener('DOMContentLoaded', function () {
    document.querySelectorAll('.time-picker[data-name]').forEach(buildPicker);
  });
})();
