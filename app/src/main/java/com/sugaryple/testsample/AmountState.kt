package com.sugaryple.testsample

sealed class AmountState(val balance: Int) {
    class Insufficient(balance: Int): AmountState(balance)
    class Sufficient(balance: Int): AmountState(balance)
}