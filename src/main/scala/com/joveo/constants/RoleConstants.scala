package com.joveo.constants

object RoleConstants {
  val UPDATE_ROLE_RESPONSE = "Role Updated Successfully"
  val DELETE_ROLE_RESPONSE = "Role Deleted Successfully"

  object ErrorTypes{
    val RESOURCE_ALREADY_EXISTS = "Resource Already Exists"
    val RESOURCE_CANNOT_BE_DELETED = "Resource Cannot Be Deleted"
    val RESOURCE_DOES_NOT_EXIST = "Resource Does not Exist"
  }
  object ErrorMessages{
    val ROLE_ALREADY_EXISTS = "Role Already Exists"
    val ROLE_CANNOT_BE_DELETED = "Role Cannot Be Deleted, User For The Role Exists"
    val ROLE_DOES_NOT_EXIST = "Role Does Not Exist"
  }
}
