package com.joveo.dao

import com.joveo.dao.`trait`.UserDao
import com.joveo.model.{Permission, User}
import org.mongodb.scala.MongoCollection

import scala.concurrent.{ExecutionContext, Future}

case class MongoUserDaoImpl(collection: MongoCollection[Permission])(implicit ec: ExecutionContext) extends UserDao{
  override def addUser(User: User): Future[String] = ???
}
