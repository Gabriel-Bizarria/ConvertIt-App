package com.convertit.convertitapp

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.convertit.convertitapp.databinding.FragmentConversionBinding

class ConversionFragment : Fragment() {

    private var _binding: FragmentConversionBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentConversionBinding.inflate(inflater, container, false)
        val view = binding.root

        //MOCK DE VALORES DE CONVERSÃO PARA TESTE
        val USD = 5.02
        val EUR = 5.56

        binding.convertButton.setOnClickListener {
            val value = binding.tiValue.text.toString().toDouble()
            var valueConverted = value * USD

            binding.tvValueToConvert.text = value.toString()
            binding.tvResultConverted.text = formatter.format(valueConverted)

            binding.cvResult.visibility = View.VISIBLE
        }
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()

        val currencies = resources.getStringArray(R.array.main_currencies_list)
        val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_list_currencies, currencies)

        with(binding.itMainCurrencies){
            setAdapter(adapter)
        }
    }



}