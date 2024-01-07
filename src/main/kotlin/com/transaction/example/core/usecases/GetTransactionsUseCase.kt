package com.transaction.example.core.usecases

import com.transaction.example.core.models.TransactionHistoryInfo
import com.transaction.example.infra.repositories.AccountRepository
import com.transaction.example.infra.repositories.TransactionRepository
import com.transaction.example.web.exceptions.EntityNotFoundException
import io.micronaut.data.model.Pageable
import io.micronaut.data.model.Sort
import jakarta.inject.Singleton
import jakarta.transaction.Transactional
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
class GetTransactionsUseCase(
    private val accountRepository: AccountRepository,
    private val transactionRepository: TransactionRepository,
) {
    private val logger: Logger = LoggerFactory.getLogger(GetTransactionsUseCase::class.java)

    @Transactional
    suspend fun execute(accountId: Long, pageable: Pageable?): Result<TransactionHistoryInfo> {
        logger.info("Trying to get transactions for the account with id {}", accountId)
        val account = accountRepository.findById(accountId)
        if (!account.isPresent) return Result.failure(
            EntityNotFoundException("Account with id $accountId doesn't exist")
        )
        val accountObject = account.get()

        val pagination = if (pageable == null) {
            Pageable.UNPAGED
        } else {
            Pageable.from(pageable.number, pageable.size, Sort.of(Sort.Order.asc(ID_FIELD)))
        }
        val list = transactionRepository.findAllByAccount(accountObject, pagination)
        return Result.success(
            TransactionHistoryInfo(
                accountName = accountObject.accountName,
                currentBalance = accountObject.currentAmount,
                transactions = list.content.map { it.toModel() },
                totalSize = list.totalSize,
                totalPages = list.totalPages,
            )
        )
    }

    companion object {
        const val ID_FIELD = "id"
    }
}
