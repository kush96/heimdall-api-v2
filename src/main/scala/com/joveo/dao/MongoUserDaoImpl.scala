package com.joveo.dao

import com.joveo.dao.`trait`.UserDao
import com.joveo.model.{Permission, User}
import org.mongodb.scala.MongoCollection
import org.mongodb.scala.model.Filters
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Updates.{combine, push, set}

import scala.concurrent.{ExecutionContext, Future}

case class MongoUserDaoImpl(collection: MongoCollection[User])(implicit ec: ExecutionContext) extends UserDao {

  private val EMAIL_FIELD = "email"
  override def addUser(user: User): Future[String] = {
    for {
      _ <- collection.insertOne(user).toFuture()
    } yield user.id
  }

  override def getUser(email: String): Future[Option[User]] = {
    collection.find(equal(EMAIL_FIELD, email)).headOption()
  }

  override def addScopeForUser(user: User): Future[String] = {
    collection.updateOne(Filters.equal(EMAIL_FIELD, user.email),
      combine(
        push("scopes", user.scopes)
      )
    ).toFuture().map(isInserted => isInserted.wasAcknowledged() match {
      case true => user.id
    })
  }

  override def addUsers(users: List[User]): Future[List[String]] = {
    for {
      _ <- collection.insertMany(users).toFuture()
    } yield users.map(_.id)
  }
}
