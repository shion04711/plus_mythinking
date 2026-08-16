package com.u22.plus.webportal.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpSession;

/**
 * 生徒側のログイン・ログアウト・新規登録を担当するService。
 * セッション属性名は "studentData" を使用する。
 */
@Transactional
@Service
public class StudentLoginService {

  @Autowired
  private HttpSession session;

  @Autowired
  private StudentRepository studentRepository;

  public boolean login(String studentId, String password) {

    StudentData studentData = studentRepository.login(studentId, password);
    if (studentData == null) {
      return false;
    }

    session.setAttribute("studentData", studentData);
    return true;
  }

  public void logout() {
    session.removeAttribute("studentData");
  }

  public boolean isLogin() {
    return session.getAttribute("studentData") != null;
  }

  /**
   * 生徒を新規登録する。
   *
   * @throws StudentRegistException 学籍番号重複、パスワード不一致などの場合
   */
  public void register(String studentId, String password, String password2, String studentName, Integer courseId) {

    if (studentId == null || studentId.isBlank()) {
      throw new StudentRegistException("ユーザIDが入力されていません。");
    }

    if (studentRepository.existsById(studentId.trim())) {
      throw new StudentRegistException("そのユーザIDはすでに登録されています。");
    }

    if (password == null || password.isBlank()) {
      throw new StudentRegistException("パスワードが入力されていません。");
    }

    if (!password.equals(password2)) {
      throw new StudentRegistException("パスワードが一致しません。");
    }

    if (studentName == null || studentName.isBlank()) {
      throw new StudentRegistException("表示名が入力されていません。");
    }

    studentRepository.save(studentId.trim(), password, studentName.trim(), courseId);
  }
}
