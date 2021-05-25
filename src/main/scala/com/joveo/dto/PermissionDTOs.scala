package com.joveo.dto



object PermissionDTOs {

  case class PermissionDto(permissionId : String,permissionName: String, description: String,createdBy:String,isAllowed:Boolean)

  case class AddUpdatePermissionDto(permissionName: String,
                                    description: String,
                                    createdBy: String,
                                    isAllowed:Boolean)

  object RequestDTOs {


  }

  object ResponseDTOs {

  }
}
