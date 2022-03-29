package com.convertit.convertitapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.convertit.convertitapp.R
import com.convertit.convertitapp.ui.adapter.CurrenciesListAdapter
import com.convertit.convertitapp.databinding.FragmentCurrenciesBinding
import com.convertit.convertitapp.models.CurrenciesListBase
import com.convertit.convertitapp.ui.viewModel.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CurrenciesFragment : Fragment() {
    private var _binding: FragmentCurrenciesBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: CurrenciesListAdapter
    private lateinit var viewModel: MainViewModel

    lateinit var value: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        // Inflate the layout for this fragment
        _binding = FragmentCurrenciesBinding.inflate(layoutInflater, container, false)

        initRecyclerView()

        viewModel.finalListCurrencies.observe(viewLifecycleOwner, Observer {
            adapter.getListUpdated(it)
        })

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        var list: List<CurrenciesListBase>

        setDropdownMenuItems()

        viewModel.mainCurrencyLiveData.observe(viewLifecycleOwner, Observer {
            lifecycleScope.launch(Dispatchers.IO) {
                 viewModel.getCurrenciesList()
            }
        })

    }

    private fun initRecyclerView(){
        adapter = CurrenciesListAdapter()
        binding.currenciesRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.currenciesRecyclerView.adapter = adapter
    }

    private fun setDropdownMenuItems(){
        val mainCurrencies = resources.getStringArray(R.array.main_currencies_list)
        val firstAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_list_currencies, mainCurrencies)

        with(binding.itMainCurrencies){
            setAdapter(firstAdapter)
        }

        binding.itMainCurrencies.onItemClickListener = AdapterView.OnItemClickListener{
                parent,
                view,
                position,
                id ->
            val selectedItem = parent?.getItemAtPosition(position) as? String
            selectedItem?.let {
                    item -> viewModel.getCurrencySelected(item)
                println("OPTION_SELECTED_AT_DROPDOWN: $item")
            }
        }
    }
}