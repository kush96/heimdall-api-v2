package com.joveo.api

import com.joveo.Application.joveoSecureEndpoint
import com.joveo.service.UserService
import org.json4s.{Formats, Serialization}

import scala.concurrent.ExecutionContext

class UserApi(userService: UserService)(implicit
                                                    formats: Formats,
                                                    serialization: Serialization,
                                                    ec: ExecutionContext) {
  private val userPath = "user"
  private val userEndpoint = joveoSecureEndpoint.in(userPath)

//  val addUser = userEndpoint.post
//    .in(jsonBody[BifrostUserDto])
//    .out(jsonBody[String])
//    .serverLogic { case (authUser, usrDto) => {
//      logger.info("Inserted into DB")
//      userService.addUser(usrDto)
//    }
//    }

  //val route = List(addUser)
}
