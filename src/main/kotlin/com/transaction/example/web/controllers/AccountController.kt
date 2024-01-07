package com.transaction.example.web.controllers

import com.transaction.example.infra.entities.Account
import com.transaction.example.infra.repositories.AccountRepository
import com.transaction.example.web.exceptions.AccountCannotBeCreatedException
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import org.slf4j.LoggerFactory

@Controller("/api")
open class AccountController(
    private val accountRepository: AccountRepository,
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Post(uri = "createAccount", produces = ["application/json"])
    suspend fun createAccount(accountName: String, amount: Double): HttpResponse<Account?> {
        logger.info("Account creation with name $accountName started")
        val resultAccount = accountRepository.save(Account(accountName, amount))
            ?: throw AccountCannotBeCreatedException(accountName)
        return HttpResponse.ok(resultAccount)
    }
}
