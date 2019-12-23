package com.sugaryple.testsample

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CheckAmountUseCase {
    suspend operator fun invoke(
        money: Int
    ): AmountState = withContext(Dispatchers.IO) {
        when {
            money in 0 until price -> AmountState.Insufficient(price - money)
            money >= price -> AmountState.Sufficient(money - price)
            else -> throw IllegalStateException()
        }
    }

    companion object {
        private const val price = 1000
    }
}