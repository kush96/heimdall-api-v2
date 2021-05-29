package com.joveo.dao

import com.joveo.dao.`trait`.UserDao
import com.joveo.model.{Permission, User}
import org.mongodb.scala.MongoCollection
import org.mongodb.scala.model.Filters
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Updates.{combine, set}

import scala.concurrent.{ExecutionContext, Future}

case class MongoUserDaoImpl(collection: MongoCollection[User])(implicit ec: ExecutionContext) extends UserDao {

  override def addUser(user: User): Future[String] = {
    for {
      _ <- collection.insertOne(user).toFuture()
    } yield user.id
  }

  override def getUser(email: String): Future[Option[User]] = {
    collection.find(equal("emailId", email)).headOption()
  }

  override def updateUser(user: User): Future[String] = {
    collection.updateOne(Filters.equal("email", user.email),
      combine(
        set("scopes", user.scopes)
      )
    ).toFuture().map(isInserted => isInserted.wasAcknowledged() match {
      case true => user.id
    })
  }

}
