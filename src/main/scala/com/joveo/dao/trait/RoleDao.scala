package com.joveo.dao.`trait`

import com.joveo.model.Role

import scala.concurrent.Future

trait RoleDao {
  def addRole(role: Role): Future[String]

  def getRoleByName(roleKey: String): Future[Option[Role]]

  def updateRole(role: Role): Future[Boolean]
}
