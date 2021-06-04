package com.joveo.dao

//import akka.actor.TypedActor.dispatcher

import com.joveo.dao.`trait`.RoleDao
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
  override def getRoleById(displayName: String): Future[Option[Role]] = {
    collection.find(equal("displayName", displayName)).headOption()
  }


  override def addRole(role: Role): Future[String] = {
    for {
      _ <- collection.insertOne(role).toFuture()
    } yield role.id
  }


  override def updateRole(role: Role): Future[Boolean] = {
    collection.updateOne(Filters.equal("_id", role.id),
      combine(
        set("displayName", role.displayName),
        set("description", role.description),
        set("permissions", role.permissions)
      )
    ).toFuture().map(_.wasAcknowledged())
  }

  override def deleteRole(role: Role): Future[Boolean] = {
    collection.updateOne(Filters.equal("_id", role.id),
      combine(
        set("isDeleted", role.isDeleted)
      )
    ).toFuture().map(_.wasAcknowledged())
  }

  override def getRole(displayName: String, accountId: String ,application: String): Future[Option[Role]] = {
    collection.find(and(equal("displayName", displayName), equal("accountId", accountId),equal("application", application))).headOption()
  }

  //def getRolesForAccount(accountId: String, application: String) =


}
