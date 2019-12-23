package com.sugaryple.testsample

import android.content.Context

class GetMessageCheckAmountUseCase(private val context: Context) {
    operator fun invoke(
        state: AmountState
    ): String = when (state) {
        is AmountState.Insufficient -> {
            context.getString(R.string.insufficient_amount_message, state.balance)
        }
        is AmountState.Sufficient -> {
            if (state.balance == 0) {
                context.getString(R.string.exact_amount_message)
            } else {
                context.getString(R.string.sufficient_amount_message, state.balance)
            }
        }
    }
}