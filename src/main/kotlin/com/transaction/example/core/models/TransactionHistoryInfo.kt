package com.transaction.example.core.models

data class TransactionHistoryInfo(
    val accountName: String,
    val currentBalance: Double,
    val transactions: List<TransactionInfo> = emptyList(),
    val totalSize: Long,
    val totalPages: Int,
)
