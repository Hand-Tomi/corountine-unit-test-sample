package com.sugaryple.testsample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.sugaryple.testsample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val viewModel by lazy {
        ViewModelProviders.of(
            this,
            BaseViewModelFactory {
                MainViewModel(
                    checkAmountUseCase = CheckAmountUseCase(),
                    getMessageCheckAmountUseCase = GetMessageCheckAmountUseCase(
                        this.application.applicationContext
                    )
                )
            }
        ).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityMainBinding>(
            this,
            R.layout.activity_main
        ).apply {
            viewModel = this@MainActivity.viewModel
            lifecycleOwner = this@MainActivity
        }
        observeViewModel()
    }

    private fun observeViewModel() {
        with(viewModel) {
            infoMessage.observe(this@MainActivity, Observer {
                showToastMessage(it)
            })
        }
    }

    private fun showToastMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
