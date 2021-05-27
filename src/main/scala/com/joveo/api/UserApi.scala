//package com.joveo.api
//
//import com.joveo.Application.joveoSecureEndpoint
//import com.joveo.dto.UserDTOs.BifrostUser
//import com.joveo.fna_api_utilities.core.JoveoTapir.jsonBody
//import kamon.util.SameThreadExecutionContext.logger
//import org.json4s.{Formats, Serialization}
//import sttp.tapir.generic.auto.schemaForCaseClass
//
//import scala.concurrent.ExecutionContext
//
//class UserApi(userService: UserService)(implicit
//                                                    formats: Formats,
//                                                    serialization: Serialization,
//                                                    ec: ExecutionContext) {
//  private val userPath = "user"
//  private val userEndpoint = joveoSecureEndpoint.in(userPath)
//
//  val addUser = userEndpoint.post
//    .in(jsonBody[BifrostUser])
//    .out(jsonBody[String])
//    .serverLogic { case (authUser, permDto) => {
//      logger.info("Inserted into DB")
//
//    }
//    }
//
//  val route = List()
//}
