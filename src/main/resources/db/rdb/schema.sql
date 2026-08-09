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

-- 1. 先生マスタ
CREATE TABLE IF NOT EXISTS teacher_m (
    teacher_id VARCHAR(20) PRIMARY KEY,        -- 講師ID（教職員番号など）
    teacher_name VARCHAR(50) NOT NULL           -- 講師名
);

-- 2. コースマスタ
CREATE TABLE IF NOT EXISTS course_m (
    course_id SERIAL PRIMARY KEY,              -- コースID
    course_name VARCHAR(100) NOT NULL,         -- コース名
    start_date DATE NOT NULL,                  -- 開始日
    end_date DATE NOT NULL,                    -- 終了日
    teacher_id VARCHAR(20) REFERENCES teacher_m(teacher_id) ON DELETE SET NULL -- 担当講師ID
);

-- 3. ミス原因マスタ
CREATE TABLE IF NOT EXISTS error_reason_m (
    reason_id INT PRIMARY KEY,              -- ミス原因ID
    reason_name VARCHAR(100) NOT NULL          -- ミス原因の内容
);

-- 4. 定型文マスタ（対策アドバイス）
CREATE TABLE IF NOT EXISTS template_m (
    template_id INT PRIMARY KEY,                 -- 定型文ID
    reason_id INT REFERENCES error_reason_m(reason_id) ON DELETE CASCADE, -- ミス原因ID
    content TEXT NOT NULL,                     -- 対策・アドバイス内容
);

-- 5. 生徒マスタ
CREATE TABLE IF NOT EXISTS student_m (
    student_id VARCHAR(20) PRIMARY KEY,        -- 学籍番号など
    course_id INT REFERENCES course_m(course_id),-- 外部キー コースID
    student_name VARCHAR(50) NOT NULL,         -- 生徒名
    class_name VARCHAR(20),                    -- クラス
    student_number INT                         -- 出席番号
);

-- 6. 学習記録（勉強時間・プリント枚数を記録）
CREATE TABLE IF NOT EXISTS study_sessions (
    session_id SERIAL PRIMARY KEY,              -- 学習記録ID
    student_id VARCHAR(20) REFERENCES student_m(student_id) ON DELETE SET NULL, -- 生徒ID
    course_id INT REFERENCES course_m(course_id) ON DELETE SET NULL,             -- コースID
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 送信日時
    study_minutes INT DEFAULT 0,               -- 勉強時間（分）
    print_count INT DEFAULT 0                  -- プリント枚数
);

-- 7. 入力ログ（間違えた問題の記録）
CREATE TABLE IF NOT EXISTS input_logs (
    log_id SERIAL PRIMARY KEY,                 -- ログID
    student_id VARCHAR(20) REFERENCES student_m(student_id) ON DELETE SET NULL, -- 生徒ID
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 送信日時
    course_id INT REFERENCES course_m(course_id) ON DELETE SET NULL, -- コースID
    reason_id INT REFERENCES error_reason_m(reason_id) ON DELETE SET NULL, -- ミス原因ID
    question_text TEXT,                        -- 問題文
    correct_answer TEXT,                       -- 正答
    incorrect_answer TEXT                      -- 誤答
);