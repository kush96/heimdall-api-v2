package com.joveo
import com.joveo.fna_api_utilities.core.JoveoServer
import modules.AllModules


object Application extends JoveoServer {
  case class Client(id: String, name: String, margin: Double)
  override val applicationId: String = "DemoApp"
  override def appHost: String = "localhost"
  override def appPort: Int = 8080

  val modules = new AllModules
  import modules._
  override val tapirRoutes = modules.endpoints.tapirRoutes
}
