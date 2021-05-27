package modules
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.joveo.api.{Endpoints, PermissionApi, RoleApi}
import com.joveo.commons.{AWSSecretManager, Environment, SecretManager}
import com.joveo.dao.{MongoPermissionDaoImpl, MongoRoleDaoImpl}
import com.joveo.dao.mongo.Mongo
import com.joveo.service.{PermissionService, RoleService}
import com.softwaremill.macwire._
import com.typesafe.config.ConfigFactory
import org.json4s.DefaultFormats
import org.json4s.native.Serialization

import scala.concurrent.ExecutionContext

trait AkkaModules {
  implicit lazy val system = ActorSystem("heimdall-api-v2")
  implicit lazy val materializer = ActorMaterializer()(system)
  implicit lazy val executor: ExecutionContext = system.dispatcher
  implicit val formats: DefaultFormats.type = DefaultFormats
  implicit val serialization: Serialization.type = Serialization
}

trait ConfigModule {
  lazy val env = Environment.JOVEO_ENV
  lazy val config = ConfigFactory.load(s"${env.toLowerCase}.conf")
  lazy val secretManager: SecretManager = wire[AWSSecretManager]
}


trait MongoModule extends ConfigModule {
  lazy val mongodb: Mongo = wire[Mongo]
  lazy val permissionsCollection = mongodb.permissionsCollection
  lazy val rolesCollection = mongodb.rolesCollection
}

trait DaoModule extends AkkaModules with MongoModule {
  lazy val permissionsRepo = wire[MongoPermissionDaoImpl]
  lazy val rolesRepo = wire[MongoRoleDaoImpl]
}

trait ServicesModule extends AkkaModules with DaoModule {
  lazy val permissionService = wire[PermissionService]
  lazy val roleService = wire[RoleService]
}

trait ApiModule extends ServicesModule with AkkaModules {
  lazy val permissionApi = wire[PermissionApi]
  lazy val roleApi = wire[RoleApi]
  lazy val endpoints = wire[Endpoints]
}


class AllModules extends ApiModule