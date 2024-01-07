package com.transaction.example.infra.entities

import com.transaction.example.core.models.AccountInfo
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class Account(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,
    val accountName: String,
    val currentAmount: Double,
) {
    public constructor(accountName: String, currentAmount: Double) :
        this(null, accountName, currentAmount)

    constructor() : this(null, "", 0.0)

    fun toModel(): AccountInfo {
        return AccountInfo(
            id = id,
            accountName = this.accountName,
            currentAmount = this.currentAmount,
        )
    }

    companion object {
        fun fromModel(
            accountInfo: AccountInfo,
        ): Account = Account(
            id = accountInfo.id,
            accountName = accountInfo.accountName,
            currentAmount = accountInfo.currentAmount,
        )
    }
}