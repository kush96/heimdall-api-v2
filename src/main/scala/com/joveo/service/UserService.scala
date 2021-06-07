package com.joveo.service

import com.joveo.constants.GenericConstants.ErrorTypes
import com.joveo.constants.UserConstants.{DEFAULT_PROFILE_PICTURE, DEFAULT_USER_NAME, ErrorMessages, UserStatus}
import com.joveo.dao.`trait`.UserDao
import com.joveo.dto.UserDTOs.{BifrostUserDto, GetUserResponseDto, ScopeDto, SignUpDto}
import com.joveo.fna_api_utilities.core.models.{JoveoError, JoveoErrorResponse}
import com.joveo.model.{Scope, User, UserUtils}


import java.util.{Calendar, UUID}
import scala.concurrent.{ExecutionContext, Future}

case class UserService(userDao: UserDao)(implicit ec: ExecutionContext) {

  /**
   * This function takes multiple emails ,
   * i)   If email not present in users collection, user is added with rhe product and account id sent in request. A mail is also sent accordingly
   * ii)  If email exists adds a scope to existing user and sends corresponding mail
   * iii) Even if for one email, the scope to be added , i.e the productId+accountId combination exists in scope list, error message is returned with list of such emails
   * @param addUsrDto : List of user mails to add to scope
   * @return
   */
  def addUsers(addUsrDto: BifrostUserDto): Future[Either[JoveoError, List[String]]] = {
    for {
      isValidatedList <- Future.sequence(addUsrDto.emails.map(usrMail => validateUser(usrMail, addUsrDto.scope.accountId, addUsrDto.scope.productId)))
      (validUsers,invalidUsers) = isValidatedList.partition(_._1)
      invalidUserMails = invalidUsers.map(_._3.map(_.email))
      (usersToUpdate,newUsersToAdd) = validUsers.partition(_._3.isDefined)
      newUserEmails = newUsersToAdd.map(_._2)
    } yield if (invalidUserMails.nonEmpty) {
      Future(Left(JoveoErrorResponse("", ErrorMessages.USER_ALREADY_ADDED, ErrorTypes.RESOURCE_ALREADY_EXISTS, Some(Map("emails" -> invalidUserMails.flatten.mkString(","))))))
    }
    else {
      for {
        addedUserIds <- addNewUsers(newUserEmails,addUsrDto)
        editedUserIds <- addScopesForUsers(usersToUpdate.map(_._3))
      } yield Right(addedUserIds++editedUserIds)
    }
  }.flatten

  /**
   * @param email
   * @param accountId
   * @param productId
   * @return tuple(._1 = false : If invalid user, i.e, user already exists for account+app combination ; true : If valid user
   *               ._2 = email
   *               ._3 = User : user model obtained from getUser, in case user does not exists, this value is null)
   */
  private def validateUser(email: String, accountId: String, productId: String): Future[(Boolean,String,Option[User])] = {
    userDao.getFullUser(email).flatMap {
      case Some(user) => {
        if (user.scopes.map(scope => (scope.accountId, scope.productId)).contains(accountId, productId)) {
          Future(false,email,Some(user))
        } else {
          Future(true,email,Some(user))
        }
      }
      case None => Future(true,email,None)
    }
  }

  /**
   * Add all new users and return List of generated ids
   * TODO : Link profile pictureUrl for default user
   * TODO : send mail correspondingly
   * @param userMailList
   * @param userDto
   * @return
   */
  private def addNewUsers(userMailList: List[String], userDto: BifrostUserDto) = Future.sequence(userMailList.map(mail =>
    userDao.addUser(User(UUID.randomUUID().toString,
      mail, DEFAULT_USER_NAME, scopeDtoToModel(userDto.scope),
      DEFAULT_PROFILE_PICTURE, Map()))))

  /**
   * Add new scopes for existing users and return their user ids
   * @param userListOpt
   * @return
   */
  private def addScopesForUsers(userListOpt: List[Option[User]]) = Future.sequence(userListOpt.map(usrOpt=>userDao.addScopeForUser(usrOpt.get)))


  private def scopeDtoToModel(scopeDto: ScopeDto): List[Scope] = {
    List(Scope(scopeDto.productId, scopeDto.accountId, scopeDto.roleId, scopeDto.createdBy, Calendar.getInstance().getTime, appMetadataStrToClass(scopeDto), UserStatus.USER_STATUS_ACTIVE))
  }

  private def appMetadataStrToClass(scopeDto: ScopeDto) = {
    UserUtils.serialize(scopeDto)
  }

  /**
   * get full user including all scopes , and profile info
   * @param email
   * @return
   */
  def getFullUser(email: String): Future[Either[JoveoError, GetUserResponseDto]] = {
    userDao.getFullUser(email) map {
      case None => Left(JoveoErrorResponse("", ErrorMessages.USER_NOT_ADDED, ErrorTypes.RESOURCE_UNAVAILABLE_ERROR, None))
      case Some(usr) => Right(GetUserResponseDto(usr.email, usr.displayName, usr.profilePictureUrl, ModelToScopeDto(usr.scopes)))
    }
  }

  private def ModelToScopeDto(scopes: List[Scope]): List[ScopeDto] = {
    scopes.map(scope => ScopeDto(scope.productId, scope.accountId, scope.roleId, UserUtils.toJValue(scope.metadata), scope.createdBy))
  }


}
