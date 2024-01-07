package com.transaction.example.core.usecases

import com.transaction.example.infra.entities.Account
import com.transaction.example.infra.entities.Transaction
import com.transaction.example.infra.entities.TransactionType.CREDIT
import com.transaction.example.infra.repositories.AccountRepository
import com.transaction.example.infra.repositories.TransactionRepository
import io.mockk.every
import io.mockk.mockk
import java.time.Instant
import java.util.Optional
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class WithdrawFundsUseCaseTest {

    private val accountRepository: AccountRepository = mockk()
    private val transactionRepository: TransactionRepository = mockk()
    private val useCase = WithdrawFundsUseCase(accountRepository, transactionRepository)

    @Test
    fun `when account doesn't exist then exception thrown`() = runBlocking {
        // GIVEN
        every { accountRepository.findById(TEST_ACCOUNT_ID) } returns Optional.empty()
        // WHEN
        val result = useCase.execute(TEST_ACCOUNT_ID, 100.0)
        // THEN
        Assertions.assertTrue(result.isFailure)
    }

    @Test
    fun `when current account balance is less than amount provide then exception thrown`() = runBlocking {
        // GIVEN
        every { accountRepository.findById(TEST_ACCOUNT_ID) } returns Optional.of(TEST_ACCOUNT.copy(currentAmount = 10.0))
        every { transactionRepository.save(any()) } returns Transaction(
            CREDIT, 100.0, Instant.now(), TEST_ACCOUNT
        )
        // WHEN
        val result = useCase.execute(TEST_ACCOUNT_ID, 100.0)
        // THEN
        Assertions.assertTrue(result.isFailure)
    }

    @Test
    fun `when amount is less than allowed minimum then exception thrown`() = runBlocking {
        // GIVEN
        every { accountRepository.findById(TEST_ACCOUNT_ID) } returns Optional.of(TEST_ACCOUNT)
        every { transactionRepository.save(any()) } returns Transaction(
            CREDIT, 0.0, Instant.now(), TEST_ACCOUNT
        )
        // WHEN
        val result = useCase.execute(TEST_ACCOUNT_ID, 0.0)
        // THEN
        Assertions.assertTrue(result.isFailure)
    }

    @Test
    fun `when amount is more than allowed maximum then exception thrown`() = runBlocking {
        // GIVEN
        every { accountRepository.findById(TEST_ACCOUNT_ID) } returns Optional.of(TEST_ACCOUNT)
        every { transactionRepository.save(any()) } returns Transaction(
            CREDIT, 50000.0, Instant.now(), TEST_ACCOUNT
        )
        // WHEN
        val result = useCase.execute(TEST_ACCOUNT_ID, 50000.0)
        // THEN
        Assertions.assertTrue(result.isFailure)
    }

    @Test
    fun `when correct account and amount provided then successful result returned`() = runBlocking {
        // GIVEN
        every { accountRepository.findById(TEST_ACCOUNT_ID) } returns Optional.of(TEST_ACCOUNT)
        every { accountRepository.update(any()) } returns TEST_ACCOUNT.copy(currentAmount = 200.0)
        every { transactionRepository.save(any()) } returns Transaction(
            CREDIT, 100.0, Instant.now(), TEST_ACCOUNT
        )
        // WHEN
        val result = useCase.execute(TEST_ACCOUNT_ID, 100.0)
        // THEN
        Assertions.assertFalse(result.isFailure)
        Assertions.assertEquals(200.0, result.getOrNull()!!.currentAmount)
    }

    private companion object {
        const val TEST_ACCOUNT_ID = 3L
        val TEST_ACCOUNT = Account(TEST_ACCOUNT_ID, "TEST_NAME", 300.0)
    }

}