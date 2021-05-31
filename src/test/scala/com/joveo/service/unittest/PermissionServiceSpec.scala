package com.joveo.service.unittest


import com.joveo.Application.system.dispatcher
import com.joveo.dao.`trait`.PermissionDao
import com.joveo.model.Permission
import com.joveo.service.PermissionService
import org.mockito.Matchers.any
import org.mockito.Mockito.{mock, when}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.{convertToAnyShouldWrapper, equal}

import scala.concurrent.Future

class PermissionServiceSpec extends AnyFlatSpec {
  val permissionDaoMock = mock(classOf[PermissionDao])
  val permissionService = new PermissionService(permissionDaoMock)


  "getting permission from name" should "return full permission information" in {
    val dummyPermissionName = "dummy user"
    val dummyId = "1"
    val dummyDescription = "dummy Descr"
    val dummyIsAllowed = true
    val dummyPermission = Permission(id = dummyId,dummyPermissionName,dummyDescription,dummyIsAllowed)
    when(permissionDaoMock.getPermissionByName(any[String])).thenReturn(Future(Some(dummyPermission)))

    permissionService.getPermission(dummyPermissionName) map {
      case Right(permission) => permission should equal(dummyPermission)
    }
  }


}
