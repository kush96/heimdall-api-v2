package com.joveo.clients

import software.amazon.awssdk.services.cognitoidentityprovider.model.InitiateAuthResponse

object HttpClient {

  def postApiCall(url: String, jsonPayload: String = "{}", headers: Map[String, String] = Map.empty): InitiateAuthResponse = {
    // Http(url).timeout(connTimeoutMs = 10000, readTimeoutMs = 10000).method("POST").postData(jsonPayload).headers(Map("Content-Type"->"application/json") ++ headers ).asString
    // Copy paste from Rajesh's SDK
    return null
  }
}
