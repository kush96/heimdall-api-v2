package com.joveo.model

import org.mongodb.scala.bson.annotations.BsonProperty

import java.util.Date

//{
//"_id": "5dc11223-5918-4b28-949f-0dbeb67a5446",
//"displayName": "allow all",
//"roleKey": "allowAll_mojo_joveo",
//"description": "Allow all permissions",
//"createdBy": "sami",
//"createdOn": "2019-09-25T09:36:28.632Z",
//"isActive": true,
//"instanceId": "",
//"application": "",
//"isDefaultRole": true,
//"permissions": [
//"perm1_id",
//"perm2_id"
//]
//}


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