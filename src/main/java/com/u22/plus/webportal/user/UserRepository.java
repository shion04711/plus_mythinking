package com.u22.plus.webportal.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

  @Autowired
  private NamedParameterJdbcTemplate jdbc;


  public UserData login(String userId, String password) {

    final String SQL_LOGIN = "SELECT user_name, role, enabled FROM user_m WHERE user_id = :userId AND password = :password AND enabled = true";

    Map<String, Object> params = new HashMap<>();
    params.put("userId", userId);
    params.put("password", password);

    List<Map<String, Object>> resultList = jdbc.queryForList(SQL_LOGIN, params);

    UserData userData = null;
    if (resultList.size() == 1) {
      Map<String, Object> item = resultList.get(0);
      String userName = (String) item.get("user_name");
      String role = (String) item.get("role");
      boolean enabled = (boolean) item.get("enabled");
      userData = new UserData(userId, "****", userName, role, enabled);

    }
    return userData;
  }

  /**
   * 生徒（ROLE_GENERAL）を全件取得する。
   * 講師側の生徒一覧画面で使用する。
   */
  public List<UserData> findGeneralUsers() {

    final String SQL_LIST = "SELECT user_id, user_name, role, enabled FROM user_m "
        + "WHERE role = 'ROLE_GENERAL' AND enabled = true ORDER BY user_name";

    List<Map<String, Object>> resultList = jdbc.queryForList(SQL_LIST, new HashMap<>());

    List<UserData> users = new ArrayList<>();

    for (Map<String, Object> item : resultList) {
      String userId = (String) item.get("user_id");
      String userName = (String) item.get("user_name");
      String role = (String) item.get("role");
      boolean enabled = (boolean) item.get("enabled");
      users.add(new UserData(userId, "****", userName, role, enabled));
    }

    return users;
  }
}
