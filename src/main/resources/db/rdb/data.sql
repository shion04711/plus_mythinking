-- テストデータを入れる前に、テーブルの中身を空にします
TRUNCATE TABLE input_logs, student_m, course_m RESTART IDENTITY CASCADE;

-- 1. コースマスタ
INSERT INTO course_m (course_name, start_date, end_date) VALUES 
('[DEMO] 基本情報技術者コース', '2026-04-01', '2026-09-30'),
('[DEMO] 応用情報技術者コース', '2026-05-01', '2026-10-31');

-- 2. 生徒マスタ
INSERT INTO student_m (student_id, course_id, student_name) VALUES 
('20251234', 1, 'テスト 太郎'),
('20253456', 1, 'テスト 花子'),
('20209999', 2, 'テスト 五郎');


INSERT INTO error_reason_m (reason_id, reason_name) VALUES 
(1, '[DEMO] 理解不足（分かってなかった）'),
(2, '[DEMO] 知識不足（知らなかった）'),
(3, '[DEMO] 時間切れ'),
(4, '[DEMO] 思い込み'),
(5, '[DEMO] 読み間違い'),
(6, '[DEMO] 書き間違い'),
(7, '[DEMO] 計算ミス'),
(8, '[DEMO] ケアレスミス（その他）')
ON CONFLICT (reason_id) DO NOTHING; -- すでにあればスキップ

-- 3. 入力ログ
INSERT INTO input_logs (student_id, course_id, reason_id, study_minutes, correct_answer, incorrect_answer, question_text, created_at) VALUES 
-- 直近のログ
('20251234', 1, 1, 60, 'A', 'B', '[DEMO] 2進数「1010」を10進数に変換せよ。', CURRENT_TIMESTAMP),
('20253456', 1, 2, 30, 'クレーン', 'フォーク', '[DEMO] 所有権が移転するタイミングに関する問題。', CURRENT_TIMESTAMP),
-- 5年以上前のログ（検証用）
('20209999', 2, 3, 45, '古い正解', '古い不正解', '[DEMO] 【削除検証用】5年以上前に登録された過去のログテキスト。', '2020-04-01 10:00:00');

-- ============================================================
-- ここまでテスト用データ 下は消さないでください
-- ============================================================

-- ミス原因マスタ 内容は仮（追々修正）
