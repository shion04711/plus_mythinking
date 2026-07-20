package com.u22.plus.webportal.user;

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
}
