package com.joveo.model

import org.mongodb.scala.bson.annotations.BsonProperty


case class User(
                        @BsonProperty("_id") id: String = "",
                        email: String,
                        displayName: String,
                        metadata: Map[String, AnyRef],
                        scope: Map[String, AnyRef],
                        profilePictureUrl: String,
                        appMetadata: Map[String, AnyRef]
                      )
