package com.finalproject.smartwage.domain.usecases

import com.finalproject.smartwage.data.repository.IncomeRepository

class GetUserIncomeUseCase(private val incomeRepository: IncomeRepository) {

    suspend fun execute(userId: String) = incomeRepository.getUserIncomes(userId)
}