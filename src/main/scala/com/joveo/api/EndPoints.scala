package com.joveo.api

import java.lang.System.currentTimeMillis
import java.net.InetAddress

import akka.actor.ActorSystem
import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.RouteResult.Complete
import akka.http.scaladsl.server._
import akka.http.scaladsl.server.directives._
import akka.http.scaladsl.settings.RoutingSettings
import akka.stream.{ActorMaterializer, Materializer}

import scala.concurrent.ExecutionContext

class Endpoints(permissionsEndpoint: PermissionApi) {
  val tapirRoutes = permissionsEndpoint.route
}
