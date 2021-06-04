package com.joveo.clients

import com.joveo.constants.CognitoConstants
import com.joveo.dto.CognitoDTOs.GoogleLoginRequest
import org.json4s.DefaultFormats
import org.json4s.native.Serialization.write
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient
import software.amazon.awssdk.services.cognitoidentityprovider.model.{AuthenticationResultType, InitiateAuthRequest, InitiateAuthResponse}

import java.util.Map
import java.util.HashMap

class CognitoClient{

  implicit val format = DefaultFormats

  val cognitoClient: CognitoIdentityProviderClient = CognitoIdentityProviderClient.builder()
    .region(Region.of("us-east-1"))
    .build();

  def signIn(userName: String, password: String): InitiateAuthResponse = {
    val paramsMap: Map[String, String] = new HashMap[String, String]()
    paramsMap.put("SECRET_HASH", CognitoConstants.SECRET_HASH)
    paramsMap.put("USERNAME", userName)
    paramsMap.put("PASSWORD", password)

    val authReq: InitiateAuthRequest = InitiateAuthRequest.builder()
      .authFlow(CognitoConstants.USER_PASSWORD_AUTH)
      .clientId(CognitoConstants.CLIENT_ID)
      .authParameters(paramsMap).build()
    cognitoClient.initiateAuth(authReq)
  }

  def signIn(refreshToken: String): InitiateAuthResponse = {
    val paramsMap: Map[String, String] = new HashMap[String, String]()
    paramsMap.put("SECRET_HASH", CognitoConstants.SECRET_HASH)
    paramsMap.put("REFRESH_TOKEN", refreshToken)

    val authReq: InitiateAuthRequest = InitiateAuthRequest.builder()
      .authFlow(CognitoConstants.REFRESH_TOKEN_AUTH)
      .clientId(CognitoConstants.CLIENT_ID)
      .authParameters(paramsMap).build()
    cognitoClient.initiateAuth(authReq)
  }

  def googleSignIn(code: String, redirectUrl: String): InitiateAuthResponse = {
    val signInRequest: GoogleLoginRequest = GoogleLoginRequest(code = code, redirect_uri = redirectUrl)
    val authResp: InitiateAuthResponse = HttpClient.postApiCall(CognitoConstants.GOOGLE_LOGIN_URL, write(signInRequest))
    authResp
  }
}
