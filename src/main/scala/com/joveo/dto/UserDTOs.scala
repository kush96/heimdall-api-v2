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

  case class LoginDto(
                     email: Option[String],
                     password: Option[String],
                     application: Option[String],
                     accountId: Option[String],
                     refreshToken: Option[String],
                     code: Option[String],
                     redirectUrl : Option[String]
                     )

  case class AuthResponse(
                         accessToken: String,
                         idToken: String,
                         refreshToken: Option[String],
                         tokenType: String,
                         expiresIn: Int
                       )

}
object main extends App{

  val oldUserScopes = List(("1","2"),("2","3"),("3","4"))
  val newUserScopes = List(("2","3"),("3","4"),("4","5"))
  val scopesAlreadyAdded = oldUserScopes.intersect(newUserScopes)
  print(newUserScopes diff oldUserScopes)

}