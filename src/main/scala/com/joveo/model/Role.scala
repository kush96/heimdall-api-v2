package com.joveo.model

import org.mongodb.scala.bson.annotations.BsonProperty

import java.util.Date


case class Role(
                       @BsonProperty("_id") id: String = "",
                       displayName: String,
                       description: String,
                       createdBy: String,
                       accountId: String,
                       application: String,
                       isDefaultRole: Boolean,
                       permissions: List[String],
                       createdOn: Date = new Date(),
                       isDeleted: Boolean = false
                     )

case class RoleCount(
                      @BsonProperty("_id") id: String = "",
                      userCount: Int
                    )

case class Pager(page: Int, limit: Int = 10)