package br.com.dio.coinconverter.ui

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import br.com.dio.coinconverter.App
import br.com.dio.coinconverter.core.extensions.text
import br.com.dio.coinconverter.data.model.Coin
import br.com.dio.coinconverter.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        bindAdapters()
        bindListeners()
    }

    private fun bindListeners() {
        binding.txtValue.editText?.doAfterTextChanged {
            binding.btnConverter.isEnabled = it != null && it.toString().isNotEmpty()
        }
        binding.btnConverter.setOnClickListener {
            Log.e("TAG", "bindingListeners" + binding.txtValue.text)

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

}