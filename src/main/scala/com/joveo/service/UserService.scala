package com.joveo.service


import com.joveo.constants.UserConstants.{ErrorMessages, ErrorTypes, UserStatus}
import com.joveo.dao.`trait`.UserDao
import com.joveo.dto.UserDTOs.{BifrostUserDto, BifrostUserDtoMultipleScopes, GetUserResponseDto, ScopeDto, SignUpDto}
import com.joveo.fna_api_utilities.core.models.{InvalidInputErrorResponse, JoveoError, JoveoErrorResponse}
import com.joveo.model.main.formats
import com.joveo.model.{Scope, User, UserUtils}
import org.json4s.native.{JsonMethods, Serialization}

import java.util.{Calendar, UUID}
import scala.concurrent.{ExecutionContext, Future}

case class UserService(userDao : UserDao)(implicit ec: ExecutionContext){

  def addUsers(addUsrDto : BifrostUserDto): Future[Either[JoveoError,List[String]]]={
//    loop through list do validation
//   1. does user exist
//   2. does scope to be added exists?
//   3. if yes, joveo error
//   4. if no for all users
//   5. bulk write users
    for {
      isValidatedList <- Future.sequence(addUsrDto.emails.map(usrMail => validateAddUserDto(usrMail,addUsrDto.scope.accountId,addUsrDto.scope.productId)))
    } yield if (isValidatedList.contains(false)) {
      Future(Left(JoveoErrorResponse("", "", "")))
    }
    else {
      addNewUserScopeToUser(addUsrDto)
    }
  }.flatten

  private def addNewUserScopeToUser(addUsrDto:BifrostUserDto) ={
    val emails = addUsrDto.emails
    Future.sequence(emails.map(usrMail => {
      userDao.getUser(usrMail).flatMap {
        case None => userDao.addUser(User(UUID.randomUUID().toString,usrMail,"",scopeDtoToModel(List(addUsrDto.scope)),"",Map()))
        case Some(usr) => userDao.updateUser(User(usr.id,usrMail,"",scopeDtoToModel(List(addUsrDto.scope)),"",Map()))
      }
    })).map(id=>Right(id))
  }
  private def validateAddUserDto(email : String,accountId : String,productId : String) : Future[Boolean] ={
    userDao.getUser(email).flatMap{
      case Some(user) => {
        if (user.scopes.map(scope => (scope.accountId, scope.productId)).contains(accountId, productId)) {
          Future(false)
        } else {
          Future(true)
        }
      }
      case None => Future(true)
    }
  }
  def addUser(userDto: BifrostUserDtoMultipleScopes): Future[Either[JoveoError,String]] = userDao.getUser(userDto.email).flatMap{
    case None => userDao.addUser(User(UUID.randomUUID().toString,userDto.email,"",scopeDtoToModel(userDto.scopes),"",userDto.appMetadata)).map(id=>Right(id))
    case Some(user) => {
      val curScopes = user.scopes.map(scope=>(scope.accountId,scope.productId))
      val newScopes = scopeDtoToModel(userDto.scopes).map(scope=>(scope.accountId,scope.productId))
      val scopePairsToAdd = newScopes diff curScopes
      val scopesToAdd = scopeDtoToModel(userDto.scopes).takeWhile(scope=>scopePairsToAdd.contains((scope.accountId,scope.productId)))
      if(scopesToAdd.isEmpty)
        Future(Left(JoveoErrorResponse("401",ErrorMessages.USER_ALREADY_ADDED,ErrorTypes.RESOURCE_ALREADY_EXISTS )))
      else
        userDao.updateUser(user.copy(scopes=scopesToAdd)).map(id=>Right(id))
    }
  }
  private def scopeDtoToModel(scopesDto:List[ScopeDto]):List[Scope]={
    scopesDto.map(scopeDto=> Scope(scopeDto.productId,scopeDto.accountId,scopeDto.roleId,scopeDto.createdBy,Calendar.getInstance().getTime,appMetadataStrToClass(scopeDto),UserStatus.USER_STATUS_ACTIVE))
  }
  private def appMetadataStrToClass(scopeDto : ScopeDto) ={
    UserUtils.serialize2(scopeDto)
  }
  def getUser(email : String):Future[Either[JoveoError,GetUserResponseDto]] = {
    userDao.getUser(email) map {
      case None => Left(JoveoErrorResponse("401",ErrorMessages.USER_NOT_ADDED,ErrorTypes.RESOURCE_NOT_FOUND ))
      case Some(usr) => Right(GetUserResponseDto(usr.email,usr.displayName,usr.profilePictureUrl,ModelToScopeDto(usr.scopes)))
    }
  }
  private def ModelToScopeDto(scopes:List[Scope]):List[ScopeDto]={
    scopes.map(scope => ScopeDto(scope.productId,scope.accountId,scope.roleId,JsonMethods.parse(Serialization.write(scope.metadata)),scope.createdBy))
  }
}
