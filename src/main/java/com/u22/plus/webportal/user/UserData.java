package com.u22.plus.webportal.user;

public record UserData(

  String userId,

  String password,

  String userName,

  String role,

  boolean enabled
) {
}
