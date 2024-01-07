package com.transaction.example.infra.repositories

import com.transaction.example.infra.entities.Account
import io.micronaut.context.annotation.Executable
import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.CrudRepository
import java.util.*

@Repository
interface AccountRepository : CrudRepository<Account, Long> {

    @Executable
    override fun findById(id: Long): Optional<Account>

    @Executable
    fun save(account: Account): Account

    @Executable
    fun update(account: Account): Account
}
