package com.transaction.example.core.usecases

import com.transaction.example.infra.entities.Account
import com.transaction.example.infra.entities.Transaction
import com.transaction.example.infra.entities.TransactionType.DEBIT
import com.transaction.example.infra.repositories.AccountRepository
import com.transaction.example.infra.repositories.TransactionRepository
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.mockk.every
import io.mockk.mockk
import java.time.Instant
import java.util.Optional
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@MicronautTest
class AddFundsUseCaseTest {

    private val accountRepository: AccountRepository = mockk()
    private val transactionRepository: TransactionRepository = mockk()
    private val useCase = AddFundsUseCase(accountRepository, transactionRepository)

    @Test
    fun `when account doesn't exist then exception thrown`() = runBlocking {
        // GIVEN
        every { accountRepository.findById(TEST_ACCOUNT_ID) } returns Optional.empty()
        // WHEN
        val result = useCase.execute(TEST_ACCOUNT_ID, 100.0)
        // THEN
        assertTrue(result.isFailure)
    }

    @Test
    fun `when amount is less than allowed minimum then exception thrown`() = runBlocking {
        // GIVEN
        every { accountRepository.findById(TEST_ACCOUNT_ID) } returns Optional.of(TEST_ACCOUNT)
        every { transactionRepository.save(any()) } returns Transaction(
            DEBIT, 1.0, Instant.now(), TEST_ACCOUNT
        )
        // WHEN
        val result = useCase.execute(TEST_ACCOUNT_ID, 1.0)
        // THEN
        assertTrue(result.isFailure)
    }

    @Test
    fun `when amount is more than allowed maximum then exception thrown`() = runBlocking {
        // GIVEN
        every { accountRepository.findById(TEST_ACCOUNT_ID) } returns Optional.of(TEST_ACCOUNT)
        every { transactionRepository.save(any()) } returns Transaction(
            DEBIT, 50000.0, Instant.now(), TEST_ACCOUNT
        )
        // WHEN
        val result = useCase.execute(TEST_ACCOUNT_ID, 50000.0)
        // THEN
        assertTrue(result.isFailure)
    }

    @Test
    fun `when correct account and amount provided then successful result returned`() = runBlocking {
        // GIVEN
        every { accountRepository.findById(TEST_ACCOUNT_ID) } returns Optional.of(TEST_ACCOUNT)
        every { accountRepository.update(any()) } returns TEST_ACCOUNT.copy(currentAmount = 620.05)
        every { transactionRepository.save(any()) } returns Transaction(
            DEBIT, 120.05, Instant.now(), TEST_ACCOUNT
        )
        // WHEN
        val result = useCase.execute(TEST_ACCOUNT_ID, 120.05)
        // THEN
        assertFalse(result.isFailure)
        assertEquals(620.05, result.getOrNull()!!.currentAmount)
    }

    private companion object {
        const val TEST_ACCOUNT_ID = 3L
        val TEST_ACCOUNT = Account(TEST_ACCOUNT_ID, "TEST_NAME", 300.0)
    }
}