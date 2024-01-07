package com.transaction.example.core.usecases

import com.transaction.example.core.models.TransactionResultInfo
import com.transaction.example.core.usecases.TransactionConstants.MAX_AMOUNT_TO_WITHDRAW
import com.transaction.example.core.usecases.TransactionConstants.MIN_AMOUNT_TO_WITHDRAW
import com.transaction.example.infra.entities.Account
import com.transaction.example.infra.entities.Transaction
import com.transaction.example.infra.entities.TransactionType
import com.transaction.example.infra.repositories.AccountRepository
import com.transaction.example.infra.repositories.TransactionRepository
import com.transaction.example.web.exceptions.EntityNotFoundException
import com.transaction.example.web.exceptions.ErrorDuringAddingTransactionException
import com.transaction.example.web.exceptions.IncorrectAmountOfFundsToAddException
import com.transaction.example.web.exceptions.InsufficientFundsToWithdrawException
import jakarta.inject.Singleton
import jakarta.transaction.Transactional
import java.time.Instant
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
class WithdrawFundsUseCase(
    private val accountRepository: AccountRepository,
    private val transactionRepository: TransactionRepository,
) {
    private val logger: Logger = LoggerFactory.getLogger(WithdrawFundsUseCase::class.java)

    @Transactional
    suspend fun execute(accountId: Long, amount: Double): Result<TransactionResultInfo?> {
        logger.info("Trying to withdraw funds to the account with id {} and funds {}", accountId, amount)
        val account = accountRepository.findById(accountId)
        if (!account.isPresent) return Result.failure(
            EntityNotFoundException("Account with id $accountId doesn't exist")
        )
        val accountObject = account.get()
        if (accountObject.currentAmount < amount) {
            return Result.failure(InsufficientFundsToWithdrawException(accountId, amount))
        }
        if (amount <= MIN_AMOUNT_TO_WITHDRAW || amount > MAX_AMOUNT_TO_WITHDRAW) {
            return Result.failure(
                IncorrectAmountOfFundsToAddException(
                    "Incorrect amount of funds to withdraw from the account $accountId. " +
                        "It should be not less than $MIN_AMOUNT_TO_WITHDRAW and not more than $MAX_AMOUNT_TO_WITHDRAW"
                )
            )
        }

        val withDrawTransaction = Transaction(
            type = TransactionType.CREDIT,
            amount = amount,
            executedAt = Instant.now(),
            account = accountObject
        )
        val addedTransaction = transactionRepository.save(withDrawTransaction)
        val updatedAccount = accountRepository.update(
            Account(
                id = accountObject.id,
                accountName = accountObject.accountName,
                currentAmount = accountObject.currentAmount - amount
            )
        )
        return Result.success(
            TransactionResultInfo(
                accountName = updatedAccount.accountName,
                currentAmount = updatedAccount.currentAmount,
                lastTransactionType = addedTransaction.type,
            )
        )
    }
}
