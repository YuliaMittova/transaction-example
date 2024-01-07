package com.transaction.example.web.exceptions

class WithdrawFundsFailedException(
    private val accountId: Long,
    override val message: String? = "Withdrawing funds from the account with id $accountId failed",
    override val cause: Throwable? = null,
) : RuntimeException(
    message,
    cause,
)
