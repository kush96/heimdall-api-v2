package com.joveo.api

import com.joveo.Application.joveoSecureEndpoint
import com.joveo.dto.UserDTOs.BifrostUserDto
import com.joveo.fna_api_utilities.core.JoveoTapir.jsonBody
import com.joveo.service.UserService
import kamon.util.SameThreadExecutionContext.logger
import org.json4s.{Formats, Serialization}
import sttp.tapir.generic.auto.schemaForCaseClass
import sttp.tapir.generic.auto._
import scala.concurrent.{ExecutionContext, Future}

class UserApi(userService: UserService)(implicit
                                                    formats: Formats,
                                                    serialization: Serialization,
                                                    ec: ExecutionContext) {
  private val userPath = "user"
  private val userEndpoint = joveoSecureEndpoint.in(userPath)

  val addUser = userEndpoint.post
    .in(jsonBody[BifrostUserDto])
    .out(jsonBody[String])
    .serverLogic { case (authUser, usrDto) => {
      logger.info("Inserted into DB")
      userService.addUser(usrDto)
    }
    }

  val route = List(addUser)
}
