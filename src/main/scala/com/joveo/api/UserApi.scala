package com.joveo.api

import com.joveo.Application.joveoSecureEndpoint
import com.joveo.dto.UserDTOs.{AuthResponse, BifrostUserDto, LoginDto}
import com.joveo.fna_api_utilities.core.JoveoTapir.jsonBody
import com.joveo.service.UserService
import kamon.util.SameThreadExecutionContext.logger
import org.json4s.{Formats, Serialization}
import sttp.tapir.RenderPathTemplate.Defaults.query
import sttp.tapir.generic.auto.schemaForCaseClass

import scala.concurrent.ExecutionContext

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

  val getToken = userEndpoint.in("account")
      .in("token").post
      .in(jsonBody[LoginDto])
      .out(jsonBody[AuthResponse])
      .serverLogic{ case (authUser, loginDto) => {
      userService.refreshTokenSignIn(loginDto)
    }}

  val credentialSignin = userEndpoint.in("login").post
    .in(jsonBody[LoginDto])
    .out(jsonBody[AuthResponse])
    .serverLogic{ case (authUser, loginDto) => {
      userService.signIn(loginDto)
    }}

  val externalProviderLogin = userEndpoint.in("external-provider")
    .in("login").in(jsonBody[LoginDto])
    .out(jsonBody[AuthResponse])
    .serverLogic{
      case (authUser, loginDto) => {
        userService.googleSignIn(loginDto)
      }
    }

  val route = List(addUser, getToken, credentialSignin, externalProviderLogin)
}
