package com.joveo.api


import com.joveo.Application.joveoSecureEndpoint
import com.joveo.constants.RoleConstants
import com.joveo.dto.RoleDTOs.RoleDto
import com.joveo.fna_api_utilities.core.JoveoTapir.jsonBody
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
      logger.info("Inserted into DB")
      roleService.addRole(roleDto) map {
        case Right(roleKey) => Right(roleKey)
        case Left(joveoError) => Left(joveoError)
      }
    }
    }


  val getRoleByName = roleEndpoint.get
    .in(query[String]("roleKey"))
    .out(jsonBody[RoleDto])
    .serverLogic { case (authUser, roleKey) => {
      roleService.getRoleByName(roleKey) map {
        case Right(role) => Right(RoleDto(role.displayName, role.description, role.createdBy, role.accountId, role.application, role.isDefaultRole, role.permissions))
        case Left(joveoError) => Left(joveoError)
      }
    }
    }

  val updateRole = roleEndpoint.put
    .in(jsonBody[RoleDto])
    .out(jsonBody[String])
    .serverLogic { case (authUser, roleDto) => {
      logger.info("Inserted into DB")
      roleService.updateRole(roleDto) map {
        case Right(roleUpdated) => Right(RoleConstants.UPDATE_ROLE_RESPONSE)
        case Left(joveoError) => Left(joveoError)
      }
    }
    }

  val route = List(addRole, getRoleByName, updateRole)
}