package com.joveo.constants

object UserConstants {

  object ErrorMessages{
    val USER_ALREADY_ADDED = "user already been added to account"
    val REFRESH_TOKEN_EMPTY = "Refresh Token cannot be empty"
    val EMAIL_EMPTY = "Email cannot be empty"
    val GOOGLE_CODE_EMPTY = "Google Code cannot be empty"
  }
  object ErrorTypes{
    val RESOURCE_ALREADY_EXISTS = "Resource Already Exists"
    val INVALID_INPUT = "Invalid Input"
  }
  object UserStatus{
    val USER_STATUS_ACTIVE = "active"
    val USER_STATUS_INVITED = "active"
    val USER_STATUS_DISABLED = "disabled"
  }
}
