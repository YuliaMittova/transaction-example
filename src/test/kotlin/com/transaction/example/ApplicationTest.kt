package com.transaction.example

import io.kotest.matchers.shouldBe
import io.micronaut.runtime.EmbeddedApplication
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Test

@MicronautTest
class ApplicationTest(private val application: EmbeddedApplication<*>) {

    @Test
    fun `application is starting and running`() {
        application.isRunning shouldBe true
    }
}