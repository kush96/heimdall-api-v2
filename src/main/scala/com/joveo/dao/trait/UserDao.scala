package com.joveo.dao.`trait`

import com.joveo.model.User

import scala.concurrent.Future

trait UserDao {

  def addUser(user: User): Future[String]

  def addUsers(users: List[User]): Future[List[String]]

  def getFullUser(email: String): Future[Option[User]]

  def addScopeForUser(user : User): Future[String]
}
