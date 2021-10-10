package br.com.dio.coinconverter.ui

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import br.com.dio.coinconverter.core.extensions.*
import br.com.dio.coinconverter.data.model.Coin
import br.com.dio.coinconverter.databinding.ActivityMainBinding
import br.com.dio.coinconverter.presentation.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {


    private val dialog by lazy { createProgressDialog() }
    private val viewModel by viewModel<MainViewModel>()
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        bindAdapters()
        bindListeners()
        bindObserve()
    }

    private fun bindListeners() {
        binding.txtValue.editText?.doAfterTextChanged {
            binding.btnConverter.isEnabled = it != null && it.toString().isNotEmpty()
        }
        binding.btnConverter.setOnClickListener {
            it.hideSoftKeyboard()

            val search = "${binding.txtFrom.text}-${binding.txtTo.text}"
            viewModel.getExchangeValue(search)

        }
    }

    private fun bindAdapters() {
        val list = Coin.values()
        val adapter = ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, list)

        binding.txtAtCompleteFrom.setAdapter(adapter)
        binding.txtAtCompleteTo.setAdapter(adapter)

        binding.txtAtCompleteFrom.setText(Coin.BRL.name, false)
        binding.txtAtCompleteTo.setText(Coin.USD.name, false)
    }
    private fun bindObserve(){
        viewModel.state.observe(this){
            when (it){
                MainViewModel.State.Loading -> dialog.show()
                is MainViewModel.State.Error -> {
                    dialog.dismiss()
                    createDialog {
                        setMessage(it.throwable.message)
                    }.show()
                }
                is MainViewModel.State.Success -> success(it)
            }
        }
    }
    private fun success(it: MainViewModel.State.Success){
        dialog.dismiss()

        val selectedCoin = binding.txtTo.text
        val coin = Coin.values().find { it.name == selectedCoin } ?: Coin.BRL

        val result = it.exchange.bid * binding.txtValue.text.toDouble()

        binding.txtResult.text = result.formatCurrency(coin.locale)
    }
}