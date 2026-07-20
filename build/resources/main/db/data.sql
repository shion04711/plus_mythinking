/* データ削除：INSERT文と逆順で記載する */

DELETE FROM user_m;

/* ユーザーマスタのデータ（ADMIN権限） */
INSERT INTO
  user_m (user_id, PASSWORD, user_name, ROLE, enabled)
VALUES
  (
    'taro@xxx.co.jp',
    'p@ss',
    '情報太郎',
    'ROLE_ADMIN',
    TRUE
  );

/* ユーザーマスタのデータ（上位権限） */
INSERT INTO
  user_m (user_id, PASSWORD, user_name, ROLE, enabled)
VALUES
  (
    'hanako@xxx.co.jp',
    'p@ss',
    '情報花子',
    'ROLE_TOP',
    TRUE
  );

/* ユーザーマスタのデータ（一般権限） */
INSERT INTO
  user_m (user_id, PASSWORD, user_name, ROLE, enabled)
VALUES
  (
    'goro@xxx.co.jp',
    'p@ss',
    '情報五郎',
    'ROLE_GENERAL',
    TRUE
  );
