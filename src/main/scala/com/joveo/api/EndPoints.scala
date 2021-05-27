package com.joveo.api

class Endpoints(permissionsEndpoint: PermissionApi, roleEndpoint: RoleApi) {
  val tapirRoutes = permissionsEndpoint.route ::: roleEndpoint.route
}

