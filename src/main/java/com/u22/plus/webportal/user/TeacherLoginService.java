package com.u22.plus.webportal.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpSession;

/**
 * 講師側のログイン・ログアウト・新規登録を担当するService。
 * セッション属性名は "teacherData" を使用する。
 */
@Transactional
@Service
public class TeacherLoginService {

  @Autowired
  private HttpSession session;

  @Autowired
  private TeacherRepository teacherRepository;

  public boolean login(String teacherId, String password) {

    TeacherData teacherData = teacherRepository.login(teacherId, password);
    if (teacherData == null) {
      return false;
    }

    session.setAttribute("teacherData", teacherData);
    return true;
  }

  public void logout() {
    session.removeAttribute("teacherData");
  }

  public boolean isLogin() {
    return session.getAttribute("teacherData") != null;
  }

  /**
   * 講師を新規登録する。
   *
   * @throws TeacherRegistException 教員ID重複、パスワード不一致などの場合
   */
  public void register(String teacherId, String password, String password2, String teacherName) {

    if (teacherId == null || teacherId.isBlank()) {
      throw new TeacherRegistException("ユーザIDが入力されていません。");
    }

    if (teacherRepository.existsById(teacherId.trim())) {
      throw new TeacherRegistException("そのユーザIDはすでに登録されています。");
    }

    if (password == null || password.isBlank()) {
      throw new TeacherRegistException("パスワードが入力されていません。");
    }

    if (!password.equals(password2)) {
      throw new TeacherRegistException("パスワードが一致しません。");
    }

    if (teacherName == null || teacherName.isBlank()) {
      throw new TeacherRegistException("表示名が入力されていません。");
    }

    teacherRepository.save(teacherId.trim(), password, teacherName.trim());
  }
}
