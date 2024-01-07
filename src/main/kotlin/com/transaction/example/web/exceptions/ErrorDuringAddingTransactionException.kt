package com.transaction.example.web.exceptions

class ErrorDuringAddingTransactionException(
    private val accountId: Long,
    override val message: String? = "Failure during adding transaction for the account with id $accountId",
    override val cause: Throwable? = null,
) : RuntimeException(
    message,
    cause,
)
