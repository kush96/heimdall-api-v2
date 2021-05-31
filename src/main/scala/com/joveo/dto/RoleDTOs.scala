package com.joveo.dto


object RoleDTOs {
  case class RoleDto(displayName: String,
                     description: String,
                     createdBy: String,
                     accountId: String,
                     application: String,
                     isDefaultRole: Boolean,
                     permissions: List[String])

  case class RoleUpdateRequestDto(

                                 )


  object RequestDTOs {


  }

  object ResponseDTOs {

  }
}
