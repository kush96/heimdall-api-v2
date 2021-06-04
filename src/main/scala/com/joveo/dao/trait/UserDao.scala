package com.joveo.dao.`trait`

import com.joveo.model.{Pager, User}
import org.mongodb.scala.AggregateObservable

import scala.concurrent.Future

trait UserDao {

  def addUser(user: User): Future[String]

  def getUser(email: String): Future[Option[User]]

  def updateUser(user : User): Future[String]

  def userCountForRole(roleId: String): Future[Long]

  def getRoleCountForAccount(application: String, accountId: String): Future[Option[User]]

  def getRoleCountForAccount2(productId: String, accountId: String, pager: Pager) : AggregateObservable[User]
}
