package com.convertit.convertitapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.convertit.convertitapp.R
import com.convertit.convertitapp.adapter.CurrenciesListAdapter
import com.convertit.convertitapp.databinding.FragmentCurrenciesBinding
import com.convertit.convertitapp.viewModel.MainViewModel

class CurrenciesFragment : Fragment() {
    private var _binding: FragmentCurrenciesBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: CurrenciesListAdapter
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        // Inflate the layout for this fragment
        _binding = FragmentCurrenciesBinding.inflate(layoutInflater, container, false)
        val view = binding.root
        return view
    }

    override fun onResume() {
        super.onResume()

        setDropdownMenuItems()
        initRecyclerView()

        viewModel.currencyLiveData.observe(viewLifecycleOwner){
            
        }

    }


    fun initRecyclerView(){
        adapter = CurrenciesListAdapter()
        binding.currenciesRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.currenciesRecyclerView.adapter = adapter
    }

    fun setDropdownMenuItems(){
        val mainCurrencies = resources.getStringArray(R.array.main_currencies_list)
        val firstAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_list_currencies, mainCurrencies)

        with(binding.itMainCurrencies){
            setAdapter(firstAdapter)
        }
    }
}