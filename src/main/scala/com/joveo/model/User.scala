package com.joveo.model

import org.mongodb.scala.bson.annotations.BsonProperty


case class User(
                 @BsonProperty("_id") id: String = "",
                 email: String,
                 //displayName: String,
                 scopes: List[Scope],
                 profilePictureUrl: String,
                 appMetadata: String
               )

case class Scope(
                  productId: String,
                  accountId: String,
                  roleId: String,
                  createdBy: String,
                  createdAt: String,
                  metadata: String,
                  status :String
                )
