package com.joveo.constants

object UserConstants {

  object ErrorMessages{
    val USER_ALREADY_ADDED = "user already been added to account"
    val USER_NOT_ADDED = "user has not been invited"
  }
  object ErrorTypes{
    val RESOURCE_ALREADY_EXISTS = "Resource Already Exists"
    val RESOURCE_NOT_FOUND = "Resource Doesn't Exist"
  }
  object UserStatus{
    val USER_STATUS_ACTIVE = "active"
    val USER_STATUS_INVITED = "active"
    val USER_STATUS_DISABLED = "disabled"
  }
}
