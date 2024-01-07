package com.transaction.example.web.exceptions

class IncorrectAmountOfFundsToAddException(
    override val message: String? = "Incorrect amount of funds to add",
    override val cause: Throwable? = null,
) : RuntimeException(
    message,
    cause,
)
