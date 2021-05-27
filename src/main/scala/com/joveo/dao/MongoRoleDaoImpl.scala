package com.joveo.dao

//import akka.actor.TypedActor.dispatcher

import com.joveo.model.Role
import org.mongodb.scala._
import org.mongodb.scala.model.Filters
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.model.Updates.{combine, set}

import scala.concurrent.{ExecutionContext, Future}


class MongoRoleDaoImpl(collection: MongoCollection[Role])(implicit ec: ExecutionContext) extends RoleDao {

  // TODO : Add logs
  //  Error handling
  //  Validation on json body
  override def getRoleByName(roleKey: String): Future[Option[Role]] = {
    collection.find(equal("roleKey", roleKey)).headOption()
  }


  override def addRole(role: Role): Future[String] = {
    for {
      _ <- collection.insertOne(role).toFuture()
    } yield role.roleKey
  }


  override def updateRole(role: Role): Future[Boolean] = {
    collection.updateOne(Filters.equal("roleKey", role.roleKey),
      combine(
        set("isActive", true),
        set("description", role.description),
        set("permissions", role.permissions)
      )
    ).toFuture().map(_.wasAcknowledged())
  }

}
