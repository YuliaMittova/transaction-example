package com.transaction.example.web.controllers

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get

@Controller
open class ApplicationController {

  @Get(uri = "health_check", processes = ["text/plain"])
  open fun healthCheck(): String = "I'm healthy"
}
