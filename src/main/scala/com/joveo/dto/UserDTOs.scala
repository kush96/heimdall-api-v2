package com.joveo.dto

object UserDTOs {

  case class BifrostUserDto(
                          email: String,
                          displayName: String,
                          scopes: List[ScopeDto],
                          profilePictureUrl: String,
                          appMetadata: Map[String, String]
                        )

  case class ScopeDto(
                    productId: String,
                    accountId: String,
                    roleKey: String,
                    metadata: String,
                    createdBy : String
                  )

}

