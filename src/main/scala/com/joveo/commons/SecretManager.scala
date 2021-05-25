package com.joveo.commons


import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest
import org.json4s.native.Serialization

import scala.util.{Failure, Success, Try}

trait SecretManager {
  def secretAsString(namespace: String, key: String): Try[String]
}

class AWSSecretManager extends SecretManager {

  import org.json4s._
  import org.json4s.native.JsonMethods._

  implicit val serialization = Serialization
  implicit val formats = DefaultFormats

  // Create a Secrets Manager client
  lazy val client = AWSSecretsManagerClientBuilder.standard.build

  def secretAsString(namespace: String, key: String): Try[String] = {
    try {
      val secretJson = secretJsonObj(namespace)
      // Do a .get on the key, fail fast ...
      val secretAsString = secretJson.get(key).get.toString
      Success(secretAsString)
    } catch {
      case e: Exception => Failure(e)
    }
  }

  private def secretJsonObj(namespace: String): Map[String, Any] = {
    val getSecretValueRequest = new GetSecretValueRequest().withSecretId(namespace);

    val getSecretValueResult = client.getSecretValue(getSecretValueRequest)
    val secretJsonString = getSecretValueResult.getSecretString
    toJson(secretJsonString)
  }

  private def toJson(input: String) = {
    parse(input).extract[Map[String, Any]]
  }
}
