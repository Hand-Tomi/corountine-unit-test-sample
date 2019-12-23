package com.sugaryple.testsample

import android.content.Context
import androidx.lifecycle.Observer
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

@ExperimentalCoroutinesApi
class MainViewModelTest : Spek({
    mockLiveDataTaskExecutor()
    mockCoroutineDispatcher()
    mockDispatcherIO(Dispatchers.Unconfined)

    Feature("金額確認機能テスト") {
        val spyInfoMessage by memoized { spyk<Observer<String>>() }
        val resultSlot by memoized { slot<Int>() }
        val context by memoized {
            mockk<Context>().apply {
                every {
                    getString(R.string.exact_amount_message)
                }.returns("exact_amount_message")
                every {
                    getString(R.string.sufficient_amount_message, capture(resultSlot))
                }.answers { resultSlot.captured.toString() }
                every {
                    getString(R.string.insufficient_amount_message, capture(resultSlot))
                }.answers { resultSlot.captured.toString() }
            }
        }

        val viewModel by memoized {
            MainViewModel(
                checkAmountUseCase = CheckAmountUseCase(),
                getMessageCheckAmountUseCase = GetMessageCheckAmountUseCase(context)
            )
        }

        beforeEachGroup {
            viewModel.infoMessage.observeForever(spyInfoMessage)
        }
        afterEachGroup {
            viewModel.infoMessage.removeObserver(spyInfoMessage)
        }

        Scenario( "足りない金額で金額確認する") {
            val price = 1000
            val money = 300
            Given("300円で金額設定する") {
                viewModel.moneyText.value = money.toString()
            }
            When("金額確認実行") {
                viewModel.selectedCheckButton()
            }
            Then("足りない分テキスト表示") {
                verify {
                    spyInfoMessage.onChanged(
                        eq((price - money).toString())
                    )
                }
            }
        }

        Scenario( "値段より超える金額で金額確認する") {
            val price = 1000
            val money = 1300
            Given("1300円で金額設定する") {
                viewModel.moneyText.value = money.toString()
            }
            When("金額確認実行") {
                viewModel.selectedCheckButton()
            }
            Then("残り分テキスト表示") {
                verify {
                    spyInfoMessage.onChanged(
                        eq((money - price).toString())
                    )
                }
            }
        }

        Scenario( "丁度金額で金額確認する") {
            val money = 1000
            Given("1000円で金額設定する") {
                viewModel.moneyText.value = money.toString()
            }
            When("金額確認実行") {
                viewModel.selectedCheckButton()
            }
            Then("丁度テキスト表示") {
                verify {
                    spyInfoMessage.onChanged(
                        eq("exact_amount_message")
                    )
                }
            }
        }
    }
})
