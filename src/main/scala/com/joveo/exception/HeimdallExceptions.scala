package com.joveo.exception

object HeimdallExceptions{
  case class NoResourceFoundException(msg: String) extends Exception(msg)
  case class ResourceAlreadyExistsException(msg: String) extends Exception(msg)
}