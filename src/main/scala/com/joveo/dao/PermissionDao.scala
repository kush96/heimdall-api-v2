package com.joveo.dao



import com.joveo.fna_api_utilities.core.models.JoveoError
import com.joveo.model.Permission
import org.mongodb.scala.{Completed, MongoCollection}

import scala.concurrent.Future

trait PermissionDao {
  def getPermissionByName(permissionName: String): Future[Option[Permission]]
  def addPermission(permissionDto: Permission): Future[String]
  def updatePermission(permission: Permission): Future[Boolean]
}
