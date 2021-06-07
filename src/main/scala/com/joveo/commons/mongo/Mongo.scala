package com.joveo.commons.mongo

import com.joveo.commons.SecretManager
import com.joveo.model.{CDScopeMetadata, MojoGoScopeMetadata, MojoProScopeMetadata, Permission, Scope, ScopeMetadata, User}

import java.net.URLEncoder
import com.typesafe.config.Config
import org.bson.codecs.configuration.CodecRegistries
import org.bson.{BsonReader, BsonWriter}
import org.bson.codecs.{Codec, DecoderContext, EncoderContext}
import org.bson.codecs.configuration.CodecRegistries._
import org.mongodb.scala._
import org.mongodb.scala.bson.codecs.{DEFAULT_CODEC_REGISTRY, Macros}
import org.joda.time.DateTime
import org.json4s.JField
import org.json4s.JsonAST.{JObject, JString, JValue}

import scala.util.{Failure, Success}

class   Mongo(config: Config, secretManager: SecretManager) {

  private def passwordFromSecretManager(): String = {
    val smNamepsace = config.getString("secretManager.mongo.namespace")
    val smKey = config.getString("secretManager.mongo.passwordKey")
    val password = secretManager.secretAsString(smNamepsace, smKey)
    password match {
      case Success(value) => value
      case Failure(exception) => throw exception
    }
  }

  def mongoUri() = {
    val auth = if (config.hasPath("mongo.noauth")) {
      None
    } else {
      val username = config.getString("mongo.username")
      val password = URLEncoder.encode(passwordFromSecretManager(), "UTF-8")
      Option(s"${username}:${password}@")
    }

    val hosts = config.getString("mongo.hosts")

    s"mongodb://${auth.getOrElse("")}$hosts"
  }

  def customCodeRegistry() = {
    fromProviders(
      Macros.createCodecProviderIgnoreNone[Permission](),
      Macros.createCodecProviderIgnoreNone[User](),
      Macros.createCodecProviderIgnoreNone[Scope](),
      Macros.createCodecProviderIgnoreNone[MojoGoScopeMetadata](),
      Macros.createCodecProviderIgnoreNone[MojoProScopeMetadata](),
      Macros.createCodecProviderIgnoreNone[CDScopeMetadata]()
    )
  }

  val mongoClient: MongoClient = MongoClient(mongoUri())
  protected val customCodecRegistry = CodecRegistries.fromCodecs(new JodaDateTimeCodec)
  protected val customCodecRegistry2 = CodecRegistries.fromProviders(Macros.createCodecProvider[ScopeMetadata]())

  val codecRegistry = fromRegistries(customCodeRegistry(), DEFAULT_CODEC_REGISTRY, customCodecRegistry,customCodecRegistry2)

  // Database storing application submissions
  val mojoDB: MongoDatabase = mongoClient.getDatabase("mojo").withCodecRegistry(codecRegistry)

  val permissionsCollection: MongoCollection[Permission] = mojoDB.getCollection[Permission]("permissions")
  val userCollection: MongoCollection[User] = mojoDB.getCollection[User]("users")
}

class JodaDateTimeCodec extends Codec[DateTime] {
  override def encode(writer: BsonWriter, value: DateTime, encoderContext: EncoderContext): Unit = {
    writer.writeDateTime(value.getMillis)
  }

  override def getEncoderClass: Class[DateTime] = classOf[DateTime]

  override def decode(reader: BsonReader, decoderContext: DecoderContext): DateTime = {
    new DateTime(reader.readDateTime())
  }
}
