package com.joveo.dto

import org.json4s.JsonAST.JValue


object UserDTOs {

  case class BifrostUserDtoMultipleScopes(
                          email: String,
                          scopes: List[ScopeDto],
                          appMetadata: Map[String, String]
                        )

  case class BifrostUserDto(
                             emails: List[String],
                             scope: ScopeDto,
                             appMetadata: Map[String, JValue]
                           )
  case class ScopeDto(
                       productId: String,
                       accountId: String,
                       roleId: String,
                       metadata: JValue,
                       createdBy : String
                  )

  case class SignUpDto(
                      email : String,
                      password: String,
                      name : String,
                      profilePictureUrl : String,
                      language : String,
                      )
  case class GetUserResponseDto(
                        email : String,
                        name : String,
                        profilePictureUrl : String,
                        scopes : List[ScopeDto]
                      )
}

