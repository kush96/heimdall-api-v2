package com.joveo.service


import com.joveo.dao.`trait`.UserDao
import com.joveo.dto.UserDTOs.User

import scala.concurrent.{ExecutionContext, Future}

case class UserService(permissionDao: UserDao)(implicit ec: ExecutionContext){
  def addUser(user: User): Future[String] = {

  }
}
