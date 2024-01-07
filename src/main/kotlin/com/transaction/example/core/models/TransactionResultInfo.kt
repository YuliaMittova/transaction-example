package com.transaction.example.core.models

import com.transaction.example.infra.entities.TransactionType

data class TransactionResultInfo(
    val accountName: String,
    val currentAmount: Double,
    val lastTransactionType: TransactionType,
)
