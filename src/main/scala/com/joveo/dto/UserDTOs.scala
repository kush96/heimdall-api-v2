package com.joveo.dto

import com.joveo.model.Scope

import java.util.Date


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
object main extends App{

  val oldUserScopes = List(("1","2"),("2","3"),("3","4"))
  val newUserScopes = List(("2","3"),("3","4"),("4","5"))
  val scopesAlreadyAdded = oldUserScopes.intersect(newUserScopes)
  print(newUserScopes diff oldUserScopes)

}