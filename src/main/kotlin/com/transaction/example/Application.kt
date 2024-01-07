package com.transaction.example

import io.micronaut.runtime.Micronaut.build
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info

@OpenAPIDefinition(
    info = Info(
        title = "transaction-service",
        version = "1.0",
        description = "Transaction Service",
    ),
)
object Api

fun main(args: Array<String>) {
    build()
        .banner(false)
        .args(*args)
        .packages("com.transaction.example")
        .start()
}
