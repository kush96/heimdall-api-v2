package com.joveo.service


import com.joveo.constants.UserConstants.{ErrorMessages, ErrorTypes, UserStatus}
import com.joveo.dao.`trait`.UserDao
import com.joveo.dto.UserDTOs.{BifrostUserDto, ScopeDto}
import com.joveo.fna_api_utilities.core.models.{JoveoError, JoveoErrorResponse}
import com.joveo.model.{ Scope, User, UserUtils}

import java.util.{Calendar, UUID}
import scala.concurrent.{ExecutionContext, Future}

case class UserService(userDao : UserDao)(implicit ec: ExecutionContext){
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
    scopesDto.map(scopeDto=> Scope(scopeDto.productId,scopeDto.accountId,scopeDto.roleKey,scopeDto.createdBy,Calendar.getInstance().getTime,appMetadataStrToClass(scopeDto) ,UserStatus.USER_STATUS_ACTIVE))
  }
  private def appMetadataStrToClass(scopeDto : ScopeDto) ={
    UserUtils.serialize(scopeDto)
  }
}
