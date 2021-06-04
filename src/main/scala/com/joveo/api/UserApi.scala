package com.joveo.api

import com.joveo.Application.joveoSecureEndpoint
import com.joveo.dto.UserDTOs.{BifrostUserDto, BifrostUserDtoMultipleScopes, GetUserResponseDto, SignUpDto}
import com.joveo.fna_api_utilities.core.JoveoTapir._
import com.joveo.fna_api_utilities.core.models.JoveoErrorResponse
import com.joveo.service.UserService
import kamon.util.SameThreadExecutionContext.logger
import org.json4s.JsonAST.JValue
import org.json4s.native.Serialization
import org.json4s.{Formats, Serialization}
import sttp.tapir.CodecFormat.Json
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
    .out(jsonBody[List[String]])
    .serverLogic { case (authUser, usrDto) => {
        logger.info("Adding user for ")
        userService.addUsers(usrDto)
      }
    }
  val getUser = userEndpoint.get
    .in(query[String]("email"))
    .out(jsonBody[GetUserResponseDto])
    .serverLogic { case (authUser, usrDto) => {
      logger.info("Adding user for ")
      userService.getUser(usrDto)
    }
    }
//  val signUpUser = userEndpoint.in("signup").post
//    .in(jsonBody[SignUpDto])
//    .out(jsonBody[String])
//    .serverLogic { case (authUser, usrDto) => {
//      logger.info("Inserted into DB")
//      userService.addUser(usrDto)
//    }
//    }
  val route = List(addUser,getUser)
}
//object main extends App{
//
//  import scala.concurrent.ExecutionContext.Implicits.global
//  val x = Future(List(1,2,3,4))
//  val y = Future(List(5,6,7,8))
//  for{
//    a <- x
//  }print(a)
//
//  for{
//    a <- y
//  }print(a)
//
//}