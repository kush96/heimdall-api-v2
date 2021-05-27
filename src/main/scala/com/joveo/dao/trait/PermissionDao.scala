package com.joveo.dao.`trait`

import com.joveo.model.Permission

import scala.concurrent.Future


trait PermissionDao {
  def getPermissionByName(permissionName: String): Future[Option[Permission]]

  def addPermission(permissionDto: Permission): Future[String]

  def updatePermission(permission: Permission): Future[Boolean]
}
