package com.transaction.example.web.exceptions

class AccountCannotBeCreatedException(
    accountName: String,
    override val message: String? = "Failure during creating account with name $accountName",
    override val cause: Throwable? = null,
) : RuntimeException(
    message,
    cause,
)
