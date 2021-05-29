package com.joveo.model

import org.mongodb.scala.bson.annotations.BsonProperty

import java.util.Date


case class User(
                 @BsonProperty("_id") id: String = "",
                 email: String,
                 displayName: String,
                 scopes: List[Scope],
                 profilePictureUrl: String,
                 appMetadata: Map[String, String]
               )

case class Scope(
                  productId: String,
                  accountId: String,
                  roleKey: String,
                  createdBy: String,
                  createdAt: Date,
                  metadata: String,
                  status :String
                )
