package com.convertit.convertitapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.convertit.convertitapp.viewModel.ConversionViewModel
import com.convertit.convertitapp.R
import com.convertit.convertitapp.databinding.FragmentConversionBinding
import com.convertit.convertitapp.ui.helpers.formatter
import com.convertit.convertitapp.model.Request
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ConversionFragment : Fragment() {

    private var _binding: FragmentConversionBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ConversionViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(this).get(ConversionViewModel::class.java)

        // Inflate the layout for this fragment
        _binding = FragmentConversionBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()

        val mainCurrencies = resources.getStringArray(R.array.main_currencies_list)
        val firstAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_list_currencies, mainCurrencies)

        with(binding.itMainCurrencies){
            setAdapter(firstAdapter)
        }

        val secondCurrencies = resources.getStringArray(R.array.second_currencies_list)
        val secondAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_list_currencies, secondCurrencies)

        with(binding.itSecondCurrency){
            setAdapter(secondAdapter)
        }

        binding.convertButton.setOnClickListener {
            val firstCurrency = binding.itMainCurrencies.text.toString()
            val secondCurrency = binding.itSecondCurrency.text.toString()
            var value = binding.tiValue.text.toString()
            val request = Request(firstCurrency, secondCurrency, value.toDouble())

            if(value.isNullOrBlank() || firstCurrency == secondCurrency){
                Toast.makeText(
                    requireContext(),
                    "This value is not valid or the same currencies are selected!",
                    Toast.LENGTH_SHORT).show()
            }else{
                binding.tvValueToConvert.text = formatter.format(value.toDouble())

                lifecycleScope.async {
                    viewModel.getCurrency(request).observe(viewLifecycleOwner) {
                        binding.tvResultConverted.text = formatter.format(it)
                    }
                }

                binding.cvResult.visibility = View.VISIBLE
            }
        }
    }
}