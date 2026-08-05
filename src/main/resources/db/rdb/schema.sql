/*
--テーブルがすでに存在しています　というようなエラーが出たとき、こいつを実行してからやり直してください
DROP TABLE IF EXISTS input_logs;
DROP TABLE IF EXISTS students;
DROP TABLE IF EXISTS error_reasons;
DROP TABLE IF EXISTS courses;
*/

/*
SERIAL　自動連番を振ります
TIMESTAMP DEFAULT CURRENT_TIMESTAMP　送信された時刻を記録します
ON DELETE SET NULL　外部キーを含むデータが消すとき、そこのデータをNULLにして消していいよと許可します
*/

-- 1. コース情報 テーブル
CREATE TABLE courses (
    course_id SERIAL PRIMARY KEY,              -- コースID　自動連番が望ましいです
    course_name VARCHAR(100) NOT NULL,		-- コース名
    start_date DATE NOT NULL,		-- 開始日
    end_date DATE NOT NULL		-- 終了日
);

-- 2. ミス原因マスタ
CREATE TABLE error_reasons (
    reason_id SERIAL PRIMARY KEY,	-- ミス原因ID
    reason_name VARCHAR(100) NOT NULL	-- ミス原因の内容
);

-- 3. 生徒情報 テーブル
CREATE TABLE students (
    student_id VARCHAR(20) PRIMARY KEY,        -- 学籍番号など
    course_id INT REFERENCES courses(course_id),-- 外部キー　コースID
    student_name VARCHAR(50) NOT NULL		-- 生徒名
);

-- 4. 入力ログDB
CREATE TABLE input_logs (
    log_id SERIAL PRIMARY KEY,		-- ログID　こちらも自動連番が望ましいです
    student_id VARCHAR(20) REFERENCES students(student_id) ON DELETE SET NULL,-- 生徒ID
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 送信日時
    course_id INT REFERENCES courses(course_id) ON DELETE SET NULL,-- コースID
    reason_id INT REFERENCES error_reasons(reason_id) ON DELETE SET NULL,-- ミス原因ID
    correct_answer TEXT,-- 正答
    incorrect_answer TEXT,-- 誤答
    question_text TEXT -- 問題文　後でNoSQLに逃がす？
);