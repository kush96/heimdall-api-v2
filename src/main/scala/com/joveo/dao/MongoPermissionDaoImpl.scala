package com.joveo.dao

//import akka.actor.TypedActor.dispatcher

import com.joveo.fna_api_utilities.core.models.{JoveoError, JoveoErrorResponse}
import com.joveo.model.Permission
import org.mongodb.scala._
import org.mongodb.scala.model.Filters
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.model.Updates.{combine, set}

import scala.concurrent.{ExecutionContext, Future}


class MongoPermissionDaoImpl(collection: MongoCollection[Permission])(implicit ec: ExecutionContext) extends PermissionDao {

  // TODO : Add logs
  //  Error handling
  //  Validation on json body
  override def getPermissionByName(permissionName: String): Future[Option[Permission]] = {
    collection.find(equal("permissionName", permissionName)).headOption()
  }

  //.recover
  override def addPermission(permission: Permission) = {
    for {
      _ <- collection.insertOne(permission).toFuture()
    } yield permission.id
  }


  override def updatePermission(permission: Permission) = {
    collection.updateOne(Filters.equal("permissionName", permission.permissionName),
      combine(
        set("isActive", true),
        set("description", permission.description)
      )
    ).toFuture().map(_.wasAcknowledged())
  }

}
