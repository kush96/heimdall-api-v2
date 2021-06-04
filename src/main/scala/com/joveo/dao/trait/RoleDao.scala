package com.joveo.dao.`trait`

import com.joveo.model.Role

import scala.concurrent.Future


trait RoleDao {
  def addRole(role: Role): Future[String]

  def getRoleById(roleKey: String): Future[Option[Role]]

  def updateRole(role: Role): Future[Boolean]

  def deleteRole(role: Role): Future[Boolean]

  def getRole(displayName: String, accountId: String ,application: String): Future[Option[Role]]

}
