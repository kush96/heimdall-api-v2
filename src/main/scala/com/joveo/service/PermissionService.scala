package com.joveo.service

import com.joveo.constants.GenericConstants.ErrorTypes
import com.joveo.constants.PermissionContants.ErrorMessages
import com.joveo.dao.`trait`.PermissionDao
import com.joveo.dto.PermissionDTOs.PermissionDto
import com.joveo.fna_api_utilities.core.models.{JoveoError, JoveoErrorResponse, NotFoundErrorResponse, UnauthorizedErrorResponse}
import com.joveo.model.Permission

import java.util.UUID
import scala.concurrent.{ExecutionContext, Future}

class PermissionService(permissionDao: PermissionDao)(implicit ec: ExecutionContext) {

  /**
   *
   * @param permissionName
   * @return
   */
  def getPermission(permissionName: String): Future[Either[JoveoError,Permission]] = {
    permissionDao.getPermissionByName(permissionName).map {
      case Some(permission) => Right(permission)
      case None => Left(NotFoundErrorResponse("",ErrorMessages.PERMISSION_ALREADY_EXISTS,ErrorTypes.RESOURCE_UNAVAILABLE_ERROR))
    }
  }

  def addPermission(permissionDto: PermissionDto): Future[Either[JoveoError,String]] = {
    permissionDao.getPermissionByName(permissionDto.permissionName).flatMap {
      case Some(_) => Future(Left(JoveoErrorResponse("",ErrorMessages.PERMISSION_ALREADY_EXISTS,ErrorTypes.RESOURCE_ALREADY_EXISTS,None)))
      case None => permissionDao.addPermission(Permission(id = UUID.randomUUID().toString,permissionName = permissionDto.permissionName, description = permissionDto.description, createdBy = permissionDto.createdBy)).map(permId=>Right(permId))
    }
  }

//  def deletePermission(permissionName: String): Future[Boolean] = getPermissionByName(permissionName).flatMap {
//      case Right(permission: Permission) => permissionDao.updatePermission(permission.copy(isAllowed = false))
//      case Left(ex) => throw ex
//  }
//
  def updatePermission(permissionDto: PermissionDto): Future[Either[JoveoError,Boolean]] = getPermission(permissionDto.permissionName).flatMap {
    case Right(permission: Permission) => permissionDao.updatePermission(permission.copy(description = permissionDto.description, isAllowed = permissionDto.isAllowed)).map(isUpdated=>Right(isUpdated))
    case Left(error) => Future(Left(error))
  }
}
