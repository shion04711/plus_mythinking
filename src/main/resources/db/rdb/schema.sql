/*
--テーブルがすでに存在しています というようなエラーが出たとき、こいつを実行してからやり直してください
DROP TABLE IF EXISTS input_logs;
DROP TABLE IF EXISTS student_m;
DROP TABLE IF EXISTS error_reason_m;
DROP TABLE IF EXISTS course_m;
*/

/*
SERIAL 自動連番を振ります
TIMESTAMP DEFAULT CURRENT_TIMESTAMP 送信された時刻を記録します
ON DELETE SET NULL 外部キーを含むデータが消すとき、そこのデータをNULLにして消していいよと許可します
*/

-- 1. コースマスタ
CREATE TABLE IF NOT EXISTS course_m (
    course_id SERIAL PRIMARY KEY,             -- コースID
    course_name VARCHAR(100) NOT NULL,      -- コース名
    start_date DATE NOT NULL,       -- 開始日
    end_date DATE NOT NULL      -- 終了日
);

-- 2. ミス原因マスタ
CREATE TABLE IF NOT EXISTS error_reason_m (
    reason_id SERIAL PRIMARY KEY,   -- ミス原因ID
    reason_name VARCHAR(100) NOT NULL   -- ミス原因の内容
);

-- 3. 生徒マスタ
CREATE TABLE IF NOT EXISTS student_m (
    student_id VARCHAR(20) PRIMARY KEY,        -- 学籍番号など
    course_id INT REFERENCES course_m(course_id),-- 外部キー コースID
    student_name VARCHAR(50) NOT NULL       -- 生徒名
);

-- 4. 入力ログ
CREATE TABLE IF NOT EXISTS input_logs (
    log_id SERIAL PRIMARY KEY,      -- ログID
    student_id VARCHAR(20) REFERENCES student_m(student_id) ON DELETE SET NULL, -- 生徒ID
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 送信日時
    course_id INT REFERENCES course_m(course_id) ON DELETE SET NULL, -- コースID
    reason_id INT REFERENCES error_reason_m(reason_id) ON DELETE SET NULL, -- ミス原因ID
    study_minutes INT DEFAULT 0, -- 勉強時間
    correct_answer TEXT, -- 正答
    incorrect_answer TEXT, -- 誤答
    question_text TEXT -- 問題文
);