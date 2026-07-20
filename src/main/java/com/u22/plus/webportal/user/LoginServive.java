package com.u22.plus.webportal.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpSession;

@Transactional
@Service
public class LoginServive {



  @Autowired
  private HttpSession session;

  @Autowired
  private UserRepository userRepository;

  public boolean login(String userId, String password) {

    UserData userData = userRepository.login(userId, password);
    if (userData == null) {

      return false;
    }

    session.setAttribute("userData", userData);
    return true;
  }
}
