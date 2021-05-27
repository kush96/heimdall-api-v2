package com.joveo.dao.`trait`

import com.joveo.model.User

import scala.concurrent.Future

trait UserDao {

  def addUser(user : User) : Future[String]
  def getUser(user:User)
}
