package com.joveo.api

class Endpoints(permissionsEndpoint: PermissionApi,userEndpoints : UserApi) {
  val tapirRoutes = permissionsEndpoint.route ::: userEndpoints.route
}
