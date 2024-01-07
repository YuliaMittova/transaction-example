package com.transaction.example.core.models

import java.time.Instant

data class TransactionInfo(
    val id: Long? = null,
    val type: String,
    val amount: Double,
    val executedAt: Instant?
)
