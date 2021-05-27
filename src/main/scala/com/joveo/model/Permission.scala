package com.joveo.model

import org.mongodb.scala.bson.annotations.BsonProperty

case class Permission(
                       @BsonProperty("_id") id: String = "",
                       permissionName: String,
                       description: String,
                       isAllowed: Boolean = true, // Flag to invert the permission logic.
                       // e.g if permissionName is "placements" and isAllowed is set to false, then the permission states that user does NOT have permission to placements
//                       createdOn: Date = new Date(),
                       createdBy: String = "System"
                     )

