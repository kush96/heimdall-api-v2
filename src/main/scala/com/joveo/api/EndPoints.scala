package com.joveo.api

class Endpoints(permissionsEndpoint: PermissionApi,userEndpoints : UserApi, roleEndpoints: RoleApi) {
  val tapirRoutes = permissionsEndpoint.route ::: roleEndpoints.route
}
