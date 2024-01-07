package com.transaction.example.web.exceptions

class InsufficientFundsToWithdrawException(
    accountId: Long,
    amountToWithDraw: Double,
    override val message: String? = "Not enough funds on the account with id $accountId to withdraw $amountToWithDraw",
    override val cause: Throwable? = null,
) : RuntimeException(
  message,
  cause,
)
