package modules
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.joveo.api.{Endpoints, PermissionApi, UserApi}
import com.joveo.commons.{AWSSecretManager, Environment, SecretManager}
import com.joveo.dao.{MongoPermissionDaoImpl, MongoUserDaoImpl}
import com.joveo.commons.mongo.Mongo
import com.joveo.service.{PermissionService, UserService}
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
  lazy val usersCollection = mongodb.userCollection
}

trait DaoModule extends AkkaModules with MongoModule {
  lazy val permissionsRepo = wire[MongoPermissionDaoImpl]
  lazy val usersRepo = wire[MongoUserDaoImpl]
}

trait ServicesModule extends AkkaModules with DaoModule {
  lazy val permissionService = wire[PermissionService]
  lazy val userService = wire[UserService]
}

trait ApiModule extends ServicesModule with AkkaModules {
  lazy val permissionApi = wire[PermissionApi]
  lazy val userApi = wire[UserApi]
  lazy val endpoints = wire[Endpoints]
}


class AllModules extends ApiModule