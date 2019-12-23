package com.sugaryple.testsample

import androidx.lifecycle.*
import kotlinx.coroutines.launch

class MainViewModel(
    private val checkAmountUseCase: CheckAmountUseCase,
    private val getMessageCheckAmountUseCase: GetMessageCheckAmountUseCase
) : ViewModel() {

    val moneyText = MutableLiveData<String>()

    private val _infoMessage = MutableLiveData<String>()
    val infoMessage: LiveData<String>
        get() = _infoMessage

    fun selectedCheckButton() {
        val money = moneyText.value?.toInt() ?: return
        checkAmount(money)
    }

    private fun checkAmount(money: Int) {
        viewModelScope.launch {
            runCatching {
                val state = checkAmountUseCase(money)
                getMessageCheckAmountUseCase(state)
            }.onSuccess {
                _infoMessage.value = it
            }.onFailure {
                _infoMessage.value = it.message
            }
        }
    }

}