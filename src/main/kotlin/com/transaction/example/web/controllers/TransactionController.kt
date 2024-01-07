package com.transaction.example.web.controllers

import com.transaction.example.core.models.TransactionHistoryInfo
import com.transaction.example.core.models.TransactionResultInfo
import com.transaction.example.core.usecases.AddFundsUseCase
import com.transaction.example.core.usecases.GetTransactionsUseCase
import com.transaction.example.core.usecases.WithdrawFundsUseCase
import com.transaction.example.web.exceptions.AddFundsFailedException
import com.transaction.example.web.exceptions.EntityNotFoundException
import com.transaction.example.web.exceptions.WithdrawFundsFailedException
import io.micronaut.data.model.Pageable
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory

@Controller("/api")
open class TransactionController(
    private val addFundsUseCase: AddFundsUseCase,
    private val withdrawFundsUseCase: WithdrawFundsUseCase,
    private val getTransactionsUseCase: GetTransactionsUseCase,
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Post(uri = "addFunds", produces = ["application/json"])
    suspend fun addFunds(accountId: Long, amount: Double): HttpResponse<TransactionResultInfo> {
        logger.info("Received request to add funds to account with id {} with funds {}", accountId, amount)
        val result = addFundsUseCase.execute(accountId, amount)
        val transactionResultInfo = result.getOrNull()
        return if (result.isFailure) {
            val errorMessage = result.exceptionOrNull()?.message ?: "Adding funds to account with id $accountId failed"
            throw AddFundsFailedException(accountId, errorMessage)
        } else {
            HttpResponse.ok(transactionResultInfo)
        }
    }

    @Post(uri = "withdrawFunds", produces = ["application/json"])
    suspend fun withdrawFunds(accountId: Long, amount: Double): HttpResponse<TransactionResultInfo> {
        logger.info("Received request to withdraw {} funds from the account with id {}", amount, accountId)
        val result = withdrawFundsUseCase.execute(accountId, amount)
        val transactionResultInfo = result.getOrNull()
        return if (result.isFailure) {
            val errorMessage = result.exceptionOrNull()?.message ?: "Withdrawing funds from the account with id $accountId failed"
            throw WithdrawFundsFailedException(accountId, errorMessage)
        } else {
            HttpResponse.ok(transactionResultInfo)
        }
    }

    @Get(produces = ["application/json"], value = "/getTransactions{?pageable}")
    @Transactional
    open suspend fun getTransactions(accountId: Long, pageable: Pageable?): HttpResponse<TransactionHistoryInfo> {
        logger.info("Received request to get transactions for account id {}", accountId)
        val result = getTransactionsUseCase.execute(accountId = accountId, pageable = Pageable.from(2, 1))

        val transactionHistoryInfo = result.getOrNull()
        if (result.isFailure) throw EntityNotFoundException("Attempt to retrieve transactions for account id $accountId failed")
        return HttpResponse.ok(transactionHistoryInfo)
    }
}
