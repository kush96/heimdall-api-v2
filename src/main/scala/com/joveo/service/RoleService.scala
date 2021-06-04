package com.joveo.service

import com.joveo.constants.RoleConstants.{ErrorMessages, ErrorTypes}
import com.joveo.dao.`trait`.{RoleDao, UserDao}
import com.joveo.dto.RoleDTOs.{RoleDto, UpdateRoleRequestDto}
import com.joveo.fna_api_utilities.core.models.{JoveoError, JoveoErrorResponse, UnauthorizedErrorResponse}
import com.joveo.model.{Role, User}

import java.util.UUID
import scala.concurrent.{ExecutionContext, Future}

class RoleService(roleDao: RoleDao, userDao: UserDao)(implicit ec: ExecutionContext) {

  def getRoleById(displayName: String): Future[Either[JoveoError, Role]] = {
    roleDao.getRoleById(displayName).map {
      case Some(role) => Right(role)
      case None => Left(UnauthorizedErrorResponse("404", "Role doesn't exists", ""))
    }
  }

  def addRole(roleDto: RoleDto): Future[Either[JoveoError,String]] = {
    roleDao.getRole(roleDto.displayName, roleDto.accountId, roleDto.application).flatMap {
      case Some(_) => Future(Left(JoveoErrorResponse("409",ErrorMessages.ROLE_ALREADY_EXISTS,ErrorTypes.RESOURCE_ALREADY_EXISTS )))
      case None => roleDao.addRole(Role(id = UUID.randomUUID().toString,
        roleDto.displayName,
        roleDto.description,
        roleDto.createdBy,
        roleDto.accountId,
        roleDto.application,
        roleDto.isDefaultRole,
        roleDto.permissions)
      ).map(roleKey=>Right(roleKey))
    }
  }


  def updateRole(updateRoleRequestDto: UpdateRoleRequestDto): Future[Either[JoveoError,Boolean]] = {

    getRoleById(updateRoleRequestDto.id).flatMap {
      case Right(role: Role) => {
        val permissions = List.concat(role.permissions, updateRoleRequestDto.permissionsToAdd).diff(updateRoleRequestDto.permissionsToDelete).distinct

        roleDao.updateRole(role.copy(
        displayName = updateRoleRequestDto.displayName,
        description = updateRoleRequestDto.description,
        permissions = permissions)).map(isUpdated => Right(isUpdated))}

      case Left(error) => Future(Left(error))
    }
  }

  def deleteRole(roleId: String): Future[Either[JoveoError,Boolean]] = {

    getRoleById(roleId).flatMap{
      case Right(role: Role) => {
        if (role.isDeleted) {
          Future(Left(JoveoErrorResponse("409", ErrorMessages.ROLE_DOES_NOT_EXIST, ErrorTypes.RESOURCE_DOES_NOT_EXIST)))
        } else {
          userDao.userCountForRole(role.id).flatMap {
            case 0 => {
              roleDao.deleteRole(role.copy(
                isDeleted = true)).map(isDeleted => Right(isDeleted))
            }
            case _ => Future(Left(JoveoErrorResponse("409", ErrorMessages.ROLE_CANNOT_BE_DELETED, ErrorTypes.RESOURCE_CANNOT_BE_DELETED)))
          }
        }
      }
      case Left(error) => Future(Left(error))
    }
  }
  def getRolesForAccount(application: String, accountId: String): Future[Either[JoveoError,User]] = {

//    userDao.getRoles(application, accountId).flatMap {
//      case role : User => {
//        roleDao.getRoleById(role.id).map {
//          case Some(role) => Right(role)
//          case None => Left(UnauthorizedErrorResponse("404", "Role doesn't exists", ""))
//        }
//      }
//      case _ => Future(Left(JoveoErrorResponse("409",ErrorMessages.ROLE_ALREADY_EXISTS,ErrorTypes.RESOURCE_ALREADY_EXISTS )))
//    }

//      getRoles(application, accountId).map {
//        case roleCount => Right(roleCount)
//        case _ => Left(JoveoErrorResponse("409",ErrorMessages.ROLE_DOES_NOT_EXIST,ErrorTypes.RESOURCE_DOES_NOT_EXIST ))
//      }
    userDao.getRoleCountForAccount(application, accountId).map {
      case Some(user) => Right(user)
      case None => Left(UnauthorizedErrorResponse("404", "Role doesn't exists", ""))
    }

  }


}
