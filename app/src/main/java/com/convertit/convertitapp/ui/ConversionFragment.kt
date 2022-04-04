package com.convertit.convertitapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.convertit.convertitapp.R
import com.convertit.convertitapp.databinding.FragmentConversionBinding
import com.convertit.convertitapp.models.Request
import com.convertit.convertitapp.ui.helpers.formatter
import com.convertit.convertitapp.ui.viewModel.MainViewModel

class ConversionFragment : Fragment() {

    private var _binding: FragmentConversionBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentConversionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()

        setDropdownMenuAdapters()

        binding.convertButton.setOnClickListener {
            makeApiRequest()
        }
    }

    private fun setDropdownMenuAdapters() {
        val mainCurrencies = resources.getStringArray(R.array.main_currencies_list)
        val firstAdapter =
            ArrayAdapter(requireContext(), R.layout.dropdown_list_currencies, mainCurrencies)

        with(binding.itMainCurrencies) {
            setAdapter(firstAdapter)
        }

        val secondCurrencies = resources.getStringArray(R.array.second_currencies_list)
        val secondAdapter =
            ArrayAdapter(requireContext(), R.layout.dropdown_list_currencies, secondCurrencies)

        with(binding.itSecondCurrency) {
            setAdapter(secondAdapter)
        }
    }

    private fun makeApiRequest() {
        val firstCurrency = binding.itMainCurrencies.text.toString()
        val secondCurrency = binding.itSecondCurrency.text.toString()
        val value = binding.tiValue.text.toString()
        val request: Request

        if (value.isBlank() || firstCurrency == secondCurrency) {
            Toast.makeText(
                requireContext(),
                R.string.toast_alert_not_valid_value,
                Toast.LENGTH_SHORT
            ).show()
        } else {
            request = Request(firstCurrency, secondCurrency, value.toDouble())
            binding.tvValueToConvert.text = formatter.format(value.toDouble())

            viewModel.getConversion(request)
        }

        viewModel.conversionLiveData.observe(viewLifecycleOwner){
            binding.tvResultConverted.text = formatter.format(it)
            binding.cvResult.visibility = View.VISIBLE
        }
    }
}
