package com.joveo.service

//import akka.actor.TypedActor.dispatcher
import com.joveo.constants.PermissionContants.{ErrorMessages, ErrorTypes}
import com.joveo.dao.PermissionDao
import com.joveo.dto.PermissionDTOs.{AddUpdatePermissionDto, PermissionDto}
import com.joveo.exception.HeimdallExceptions.{NoResourceFoundException, ResourceAlreadyExistsException}
import com.joveo.fna_api_utilities.core.models.{JoveoError, JoveoErrorResponse, UnauthorizedErrorResponse}
import com.joveo.model.Permission

import java.util.UUID
import scala.concurrent.{ExecutionContext, Future}

class PermissionService(permissionDao: PermissionDao)(implicit ec: ExecutionContext) {

  def getPermissionByName(permissionName: String): Future[Either[JoveoError,Permission]] = {
    permissionDao.getPermissionByName(permissionName).map {
      case Some(permission) => Right(permission)
      case None => Left(UnauthorizedErrorResponse("401", "permission doesn't exists", ""))
    }
  }
  def addPermission(permissionDto: PermissionDto): Future[Either[JoveoError,String]] = {
    permissionDao.getPermissionByName(permissionDto.permissionName).flatMap {
      case Some(_) => Future(Left(JoveoErrorResponse("401",ErrorMessages.PERMISSION_ALREADY_EXISTS,ErrorTypes.RESOURCE_ALREADY_EXISTS )))
      case None => permissionDao.addPermission(Permission(id = UUID.randomUUID().toString,permissionName = permissionDto.permissionName, description = permissionDto.description, createdBy = permissionDto.createdBy)).map(permId=>Right(permId))
    }
  }

//  def deletePermission(permissionName: String): Future[Boolean] = getPermissionByName(permissionName).flatMap {
//      case Right(permission: Permission) => permissionDao.updatePermission(permission.copy(isAllowed = false))
//      case Left(ex) => throw ex
//  }
//
  def updatePermission(permissionDto: PermissionDto): Future[Either[JoveoError,Boolean]] = getPermissionByName(permissionDto.permissionName).flatMap {
    case Right(permission: Permission) => permissionDao.updatePermission(permission.copy(description = permissionDto.description, isAllowed = permissionDto.isAllowed)).map(isUpdated=>Right(isUpdated))
    case Left(error) => Future(Left(error))
  }
}
