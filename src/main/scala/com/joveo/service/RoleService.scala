package com.joveo.service

import com.joveo.constants.RoleConstants.{ErrorMessages, ErrorTypes}
import com.joveo.dao.`trait`.RoleDao
import com.joveo.dto.RoleDTOs.RoleDto
import com.joveo.fna_api_utilities.core.models.{JoveoError, JoveoErrorResponse, UnauthorizedErrorResponse}
import com.joveo.model.Role

import java.util.UUID
import scala.concurrent.{ExecutionContext, Future}

class RoleService(roleDao: RoleDao)(implicit ec: ExecutionContext) {

  def getRoleByName(roleKey: String): Future[Either[JoveoError, Role]] = {
    roleDao.getRoleByName(roleKey).map {
      case Some(role) => Right(role)
      case None => Left(UnauthorizedErrorResponse("401", "role doesn't exists", ""))
    }
  }
  def addRole(roleDto: RoleDto): Future[Either[JoveoError,String]] = {

    val roleKey = if(roleDto.isDefaultRole) roleDto.displayName + "_" + roleDto.application else roleDto.displayName + "_" + roleDto.application + "_" + roleDto.accountId

    roleDao.getRoleByName(roleKey).flatMap {
      case Some(_) => Future(Left(JoveoErrorResponse("401",ErrorMessages.ROLE_ALREADY_EXISTS,ErrorTypes.RESOURCE_ALREADY_EXISTS )))
      case None => roleDao.addRole(Role(id = UUID.randomUUID().toString,
        roleDto.displayName,
        roleKey, roleDto.description,
        roleDto.createdBy, roleDto.accountId,
        roleDto.application, roleDto.isDefaultRole,
        roleDto.permissions)
      ).map(roleKey=>Right(roleKey))
    }
  }


  def updateRole(roleDto: RoleDto): Future[Either[JoveoError,Boolean]] = {
    val roleKey = {
      if(roleDto.isDefaultRole)
        roleDto.displayName + "_" + roleDto.application
      else
        roleDto.displayName + "_" + roleDto.application + "_" + roleDto.accountId
    }
    getRoleByName(roleKey).flatMap {
      case Right(role: Role) => roleDao.updateRole(role.copy(description = roleDto.description, permissions = roleDto.permissions)).map(isUpdated => Right(isUpdated))
      case Left(error) => Future(Left(error))
    }
  }
}
