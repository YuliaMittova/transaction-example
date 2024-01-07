package com.transaction.example.web.exceptions

class AddFundsFailedException(
    private val accountId: Long,
    override val message: String? = "Adding funds to the account with id $accountId failed",
    override val cause: Throwable? = null,
) : RuntimeException(
    message,
    cause,
)
