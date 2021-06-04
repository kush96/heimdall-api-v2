package com.joveo.dto

import java.util.Date


object RoleDTOs {
  case class RoleDto(displayName: String,
                     description: String,
                     createdBy: String,
                     accountId: String,
                     application: String,
                     isDefaultRole: Boolean,
                     permissions: List[String]
                    )

  case class UpdateRoleRequestDto(
                                 id: String,
                                 displayName: String,
                                 description: String,
                                 permissionsToAdd: List[String],
                                 permissionsToDelete: List[String]
                                 )

  case class GetRoleResponseDto(
                                 id: String,
                                 displayName: String,
                                 description: String,
                                 createdBy: String,
                                 accountId: String,
                                 application: String,
                                 isDefaultRole: Boolean,
                                 permissions: List[String],
                                 createdOn: Date,
                                 isDeleted: Boolean
                               )
  case class RoleCountDto(
                         id: String,
                         userCount: Int
                         )

  object RequestDTOs {


  }

  object ResponseDTOs {

  }
}
