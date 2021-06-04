package com.joveo.service

import com.joveo.clients.CognitoClient
import com.joveo.constants.UserConstants.{ErrorMessages, ErrorTypes, UserStatus}
import com.joveo.dao.`trait`.UserDao
import com.joveo.dto.UserDTOs.{AuthResponse, BifrostUserDto, LoginDto, ScopeDto}
import com.joveo.fna_api_utilities.core.models.{JoveoError, JoveoErrorResponse}
import com.joveo.model.{Scope, User}
import software.amazon.awssdk.services.cognitoidentityprovider.model.{AuthenticationResultType, InitiateAuthResponse}

import java.util.{Calendar, UUID}
import scala.concurrent.{ExecutionContext, Future}

class UserService(userDao : UserDao, cognitoClient: CognitoClient)(implicit ec: ExecutionContext){
  def addUser(userDto: BifrostUserDto): Future[Either[JoveoError,String]] = {
    userDao.getUser(userDto.email).flatMap{
      case None => userDao.addUser(User(UUID.randomUUID().toString,userDto.email,userDto.displayName,convertScopeDtoToModel(userDto.scopes),userDto.profilePictureUrl,userDto.appMetadata)).map(id=>Right(id))
      case Some(user) => {
        val curScopes = user.scopes.map(scope=>(scope.accountId,scope.productId))
        val newScopes = convertScopeDtoToModel(userDto.scopes).map(scope=>(scope.accountId,scope.productId))
        val scopePairsToAdd = newScopes diff curScopes
        val scopesToAdd = convertScopeDtoToModel(userDto.scopes).takeWhile(scope=>scopePairsToAdd.contains((scope.accountId,scope.productId)))
        if(scopesToAdd.isEmpty)
          Future(Left(JoveoErrorResponse("401",ErrorMessages.USER_ALREADY_ADDED,ErrorTypes.RESOURCE_ALREADY_EXISTS )))
        userDao.updateUser(user.copy(scopes=scopesToAdd)).map(id=>Right(id))
      }
    }
  }
  private def convertScopeDtoToModel(scopesDto:List[ScopeDto]):List[Scope]={
    scopesDto.map(scopeDto=> Scope(scopeDto.productId,scopeDto.accountId,scopeDto.roleKey,scopeDto.createdBy,Calendar.getInstance().getTime,scopeDto.metadata,UserStatus.USER_STATUS_ACTIVE))
  }

  def refreshTokenSignIn(loginDto: LoginDto): Future[Either[JoveoError, AuthResponse]] = {

    loginDto.refreshToken match {
      case None => Future(Left(JoveoErrorResponse("401", ErrorMessages.REFRESH_TOKEN_EMPTY, ErrorTypes.INVALID_INPUT)))
      case Some(token) => {
        val cognitoAuthResp: InitiateAuthResponse = cognitoClient.signIn(token)
        val authResponse: AuthResponse = convertToAuthResponse(cognitoAuthResp)
        Future.successful(authResponse).map(tokens => Right(tokens))
      }
    }
  }

  def signIn(loginDto: LoginDto): Future[Either[JoveoError, AuthResponse]] = {

    loginDto.email match {
      case None => Future(Left(JoveoErrorResponse("401", ErrorMessages.EMAIL_EMPTY, ErrorTypes.INVALID_INPUT)))
      case Some(email) => {
        val cognitoAuthResp: InitiateAuthResponse = cognitoClient.signIn(email, loginDto.password.getOrElse(""))
        val authResponse: AuthResponse = convertToAuthResponse(cognitoAuthResp)
        Future.successful(authResponse).map(tokens => Right(tokens))
      }
    }
  }

  def googleSignIn(loginDto: LoginDto): Future[Either[JoveoError, AuthResponse]] = {
    loginDto.code match {
      case None => Future(Left(JoveoErrorResponse("401", ErrorMessages.GOOGLE_CODE_EMPTY, ErrorTypes.INVALID_INPUT)))
      case Some(code) => {
        val cognitoAuthResp: InitiateAuthResponse = cognitoClient.googleSignIn(code, loginDto.redirectUrl.getOrElse(""))
        val authResponse: AuthResponse = convertToAuthResponse(cognitoAuthResp)
        Future.successful(authResponse).map(tokens => Right(tokens))
      }
    }
  }

  private def convertToAuthResponse(cognitoAuthResp: InitiateAuthResponse): AuthResponse = {
    val cognitoAuthResult: AuthenticationResultType = cognitoAuthResp.authenticationResult()
    AuthResponse(cognitoAuthResult.accessToken(), cognitoAuthResult.idToken(), Some(cognitoAuthResult.refreshToken()),
      cognitoAuthResult.tokenType(), cognitoAuthResult.expiresIn())
  }
}

