package com.joveo.api


import com.joveo.Application.joveoSecureEndpoint
import com.joveo.constants.RoleConstants
import com.joveo.dto.RoleDTOs.{RoleDto, UpdateRoleRequestDto}
import com.joveo.fna_api_utilities.core.JoveoTapir.jsonBody
import com.joveo.model.User
import com.joveo.service.RoleService
import kamon.util.SameThreadExecutionContext.logger
import org.json4s.{Formats, Serialization}
import sttp.tapir.generic.auto.schemaForCaseClass
import sttp.tapir.query

import scala.concurrent.ExecutionContext

/*
* TODO :
*  1. logging : i) what all events need to be logged ?
*  2. error handling : JoveoErrorResponse
*
* */


class RoleApi(roleService: RoleService)(implicit formats: Formats,
                                                          serialization: Serialization,
                                                          ec: ExecutionContext) {

  private val rolePath = "role"
  private val roleEndpoint = joveoSecureEndpoint.in(rolePath)

  val addRole = roleEndpoint.post
    .in(jsonBody[RoleDto])
    .out(jsonBody[String])
    .serverLogic { case (authUser, roleDto) => {
      logger.info("Role inserted into DB")
      roleService.addRole(roleDto) map {
        case Right(roleKey) => Right(roleKey)
        case Left(joveoError) => Left(joveoError)
      }
    }
    }


//  val getRoleById = roleEndpoint.get
//    .in(query[String]("displayName"))
//    .out(jsonBody[GetRoleResponseDto])
//    .serverLogic { case (authUser, displayName) => {
//      roleService.getRoleById(displayName) map {
//        case Right(role) => Right(GetRoleResponseDto(role.id, role.displayName, role.description, role.createdBy, role.accountId, role.application, role.isDefaultRole, role.permissions, role.createdOn, role.isDeleted))
//        case Left(joveoError) => Left(joveoError)
//      }
//    }
//    }

  val getRolesForAccount = roleEndpoint.get
    .in(query[String]("application"))
    .in(query[String]("accountId"))
    .out(jsonBody[User])
    .serverLogic { case (authUser, (application, accountId)) => {
      roleService.getRolesForAccount(application, accountId) map {
        case Right(roleKey) => Right(roleKey)
        case Left(joveoError) => Left(joveoError)
      }
    }
    }


  val deleteRole = roleEndpoint.delete
    .in(query[String]("roleId"))
    .out(jsonBody[String])
    .serverLogic { case (authUser, roleId) => {
      logger.info("Deletion from DB")
      roleService.deleteRole(roleId) map {
        case Right(roleDeleted) => Right(RoleConstants.DELETE_ROLE_RESPONSE)
        case Left(joveoError) => Left(joveoError)
      }
    }
    }

  val updateRole = roleEndpoint.put
    .in(jsonBody[UpdateRoleRequestDto])
    .out(jsonBody[String])
    .serverLogic { case (authUser, updateRoleRequestDto: UpdateRoleRequestDto) => {
      logger.info("Inserted into DB")
      roleService.updateRole(updateRoleRequestDto) map {
        case Right(roleUpdated) => Right(RoleConstants.UPDATE_ROLE_RESPONSE)
        case Left(joveoError) => Left(joveoError)
      }
    }
    }

  val route = List(addRole, getRolesForAccount, updateRole, deleteRole)
}