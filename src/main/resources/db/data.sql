-- ミス原因マスタ　ここは消さないでください　内容は仮で置いているので、追々修正する方向で
INSERT INTO error_reasons (reason_name) VALUES 
('[DEMO] 理解不足（分かってなかった）'),
('[DEMO] 知識不足（知らなかった）'),
('[DEMO] 時間切れ'),
('[DEMO] 思い込み');
('[DEMO] 読み間違い');
('[DEMO] 書き間違い');
('[DEMO] 計算ミス');
('[DEMO] ケアレスミス（その他）');

-- 以下はダミーデータです　いらなくなったら消してください

-- 1. コース情報
INSERT INTO courses (course_name, start_date, end_date) VALUES 
('[DEMO] 基本情報技術者コース', '2026-04-01', '2026-09-30'),
('[DEMO] 応用情報技術者コース', '2026-05-01', '2026-10-31');

-- 2. 生徒情報
INSERT INTO students (student_id, course_id, student_name) VALUES 
('20251234', 1, 'テスト　太郎'),
('20253456', 1, 'テスト　花子'),
('20259999', 2, 'テスト　五郎');

-- 3. 入力ログ
INSERT INTO input_logs (student_id, course_id, reason_id, correct_answer, incorrect_answer, question_text, created_at) VALUES 
-- 直近のログ
('2026A001', 1, 1, 'A', 'B', '[DEMO] 2進数「1010」を10進数に変換せよ。', NOW()),
('2026A002', 1, 2, 'クレーン', 'フォーク', '[DEMO] 所有権が移転するタイミングに関する問題。', NOW()),
-- 5年以上前のログ（5年自動削除・NoSQL移行の検証用）
('2026B001', 2, 3, '古い正解', '古い不正解', '[DEMO] 【削除検証用】5年以上前に登録された過去のログテキスト。', '2020-04-01 10:00:00');