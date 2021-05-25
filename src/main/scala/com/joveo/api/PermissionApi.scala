package com.joveo.api


import com.joveo.Application.joveoSecureEndpoint
import com.joveo.constants.PermissionContants
import com.joveo.dto.PermissionDTOs.PermissionDto
import com.joveo.fna_api_utilities.core.JoveoTapir.{jsonBody, path}
import com.joveo.service.PermissionService
import kamon.util.SameThreadExecutionContext.logger
import org.json4s.{Formats, Serialization}
import sttp.tapir.generic.auto.schemaForCaseClass
import sttp.tapir.query

import scala.concurrent.{ExecutionContext, Future}
/*
* TODO :
*  1. logging : i) what all events need to be logged ?
*  2. error handling : JoveoErrorResponse
*
* */



class PermissionApi(permissionService: PermissionService)(implicit
                                                          formats: Formats,
                                                          serialization: Serialization,
                                                          ec: ExecutionContext) {

  val addPermission = joveoSecureEndpoint.post
    .in("permission")
    .in(jsonBody[PermissionDto])
    .out(jsonBody[String])
    .serverLogic { case (authUser, permDto) => {
      logger.info("Inserted into DB")
      permissionService.addPermission(permDto.copy(createdBy = authUser.id)) map {
        case Right(permId) => Right(permId)
        case Left(joveoError) => Left(joveoError)
      }
    }
    }
  //logging
  val getPermissionByName = joveoSecureEndpoint.get.in("permission")
    .in(query[String]("permissionName"))
    .out(jsonBody[PermissionDto])
    .serverLogic { case (authUser, permissionName) => {
      permissionService.getPermissionByName(permissionName) map {
        case Right(permission) => Right(PermissionDto(permissionId = permission.id, permissionName = permission.permissionName, createdBy = permission.createdBy, isAllowed = permission.isAllowed, description = permission.description))
        case Left(joveoError) => Left(joveoError)
      }
    }
    }
  val updatePermission = joveoSecureEndpoint.put
    .in("permission")
    .in(jsonBody[PermissionDto])
    .out(jsonBody[String])
    .serverLogic { case (authUser, permDto) => {
      logger.info("Inserted into DB")
      permissionService.updatePermission(permDto) map {
        case Right(permId) => Right(PermissionContants.UPDATE_PERMISSION_RESPONSE)
        case Left(joveoError) => Left(joveoError)
      }
    }
    }
  val route = List(getPermissionByName, addPermission)
}