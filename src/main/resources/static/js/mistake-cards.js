/**
 * 「間違えた問題」カードの追加・削除。
 *
 * - .mistake-card__add ボタン(カード内のどれを押してもOK)
 *     → 一番最後のカードの内容を複製し、リストの末尾に追加する
 * - .mistake-card__close ボタン(各カード右上の×)
 *     → そのカードを削除する(最低1枚は残す)
 *
 * name属性は entries[0].miss / entries[1].miss ... のように
 * 連番になるよう自動で振り直すので、Controller側は
 * List<Entry> として受け取れる。
 */
(function () {
  document.addEventListener('DOMContentLoaded', function () {
    const list = document.getElementById('mistakeList');
    if (!list) return;

    function reindexCards() {
      const cards = list.querySelectorAll('.mistake-card');
      cards.forEach(function (card, index) {
        card.querySelectorAll('[name]').forEach(function (el) {
          el.name = el.name.replace(/entries\[\d+\]/, `entries[${index}]`);
        });
        card.querySelectorAll('[id]').forEach(function (el) {
          el.id = el.id.replace(/-\d+$/, `-${index}`);
        });
        card.querySelectorAll('label[for]').forEach(function (el) {
          el.htmlFor = el.htmlFor.replace(/-\d+$/, `-${index}`);
        });
      });
    }

    function addCard() {
      const cards = list.querySelectorAll('.mistake-card');
      const lastCard = cards[cards.length - 1];
      const newCard = lastCard.cloneNode(true);

      // 入力値をクリア
      newCard.querySelectorAll('input, textarea').forEach(function (el) {
        el.value = '';
      });

      list.appendChild(newCard);
      reindexCards();
    }

    function removeCard(card) {
      const cards = list.querySelectorAll('.mistake-card');
      if (cards.length <= 1) return; // 最低1枚は残す
      card.remove();
      reindexCards();
    }

    // イベント委譲: 後から追加されたカード内のボタンにも反応する
    list.addEventListener('click', function (e) {
      if (e.target.closest('.mistake-card__add')) {
        addCard();
      }
      if (e.target.closest('.mistake-card__close')) {
        removeCard(e.target.closest('.mistake-card'));
      }
    });
  });
})();
