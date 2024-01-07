package com.transaction.example.core.models

data class AccountInfo(
    val id: Long? = null,
    val accountName: String,
    val currentAmount: Double,
)
