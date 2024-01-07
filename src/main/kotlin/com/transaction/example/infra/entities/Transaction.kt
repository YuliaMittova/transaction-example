package com.transaction.example.infra.entities

import com.transaction.example.core.models.TransactionInfo
import java.time.Instant
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@Entity
public data class Transaction(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,
    @Enumerated(EnumType.STRING)
    val type: TransactionType,
    val amount: Double,
    val executedAt: Instant?,
    @ManyToOne
    @JoinColumn(name = "ACCOUNT")
    val account: Account
) {
    constructor(type: TransactionType, amount: Double, executedAt: Instant?, account: Account) :
        this(null, type, amount, executedAt, account)

    constructor() : this(null, TransactionType.DEBIT, 0.0, null, Account())

    fun toModel(): TransactionInfo {
        return TransactionInfo(
            id = id,
            type = this.type.name,
            amount = this.amount,
            executedAt = this.executedAt,
        )
    }

    companion object {
        fun fromModel(
            transactionInfo: TransactionInfo,
            account: Account,
        ): Transaction = Transaction(
            id = transactionInfo.id,
            type = TransactionType.valueOf(transactionInfo.type),
            amount = transactionInfo.amount,
            executedAt = transactionInfo.executedAt,
            account = account,
        )
    }
}

enum class TransactionType(private val value: String) {
    DEBIT("DEBIT"),
    CREDIT("CREDIT")
}
