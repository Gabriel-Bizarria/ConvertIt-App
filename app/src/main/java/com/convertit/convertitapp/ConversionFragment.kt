package com.convertit.convertitapp

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
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

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()

        //MOCK DE VALORES DE CONVERSÃO PARA TESTE
        val USD = 5.02
        val EUR = 5.56


        val currencies = resources.getStringArray(R.array.main_currencies_list)
        val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_list_currencies, currencies)

        with(binding.itMainCurrencies){
            setAdapter(adapter)
        }

        binding.convertButton.setOnClickListener {
            var value = binding.tiValue.text.toString()
            if(value.isNullOrBlank()){
                Toast.makeText(
                    requireContext(),
                    "This value is not valid!",
                    Toast.LENGTH_SHORT).show()
            }else{
                var valueConverted = value.toDouble() * USD

                binding.tvValueToConvert.text = formatter.format(value.toDouble())
                binding.tvResultConverted.text = formatter.format(valueConverted)

                binding.cvResult.visibility = View.VISIBLE
            }
        }
    }



}