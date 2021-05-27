package com.joveo.dto

import org.mongodb.scala.bson.annotations.BsonProperty

object UserDTOs {

  case class User(
                   @BsonProperty("_id") id: String = "",
                   email: String,
                   displayName: String,
                   metadata: Map[String, AnyRef],
                   scope: Map[String, AnyRef],
                   profilePictureUrl: String,
                   appMetadata: Map[String, AnyRef]
                 )

}
