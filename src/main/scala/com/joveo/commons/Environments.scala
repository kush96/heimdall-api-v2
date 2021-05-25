package com.joveo.commons

object Environment {

  lazy val JOVEO_ENV = sys.env.getOrElse("JOVEO_ENV", "devlocal")

}

