package com.joveo.dto

import com.joveo.constants.CognitoConstants

object CognitoDTOs {

  case class GoogleLoginRequest(
                                 grant_type: String = CognitoConstants.GRANT_TYPE_AUTH_CODE,
                                 scope: String = CognitoConstants.SCOPE,
                                 redirect_uri: String,
                                 client_id: String = CognitoConstants.CLIENT_ID,
                                 code: String
                               )
}
