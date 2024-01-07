package com.transaction.example.infra.repositories

import com.transaction.example.infra.entities.Account
import com.transaction.example.infra.entities.Transaction
import io.micronaut.context.annotation.Executable
import io.micronaut.data.annotation.Repository
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.data.repository.PageableRepository

@Repository
interface TransactionRepository : PageableRepository<Transaction, Long> {

    @Executable
    fun findAllByAccount(account: Account, pageable: Pageable): Page<Transaction>

    @Executable
    fun save(transaction: Transaction): Transaction
}
