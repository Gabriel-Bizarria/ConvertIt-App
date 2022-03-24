package com.convertit.convertitapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.convertit.convertitapp.MainCurrenciesList
import com.convertit.convertitapp.databinding.CardviewCurrenciesBinding
import com.convertit.convertitapp.models.CurrenciesListBase
import com.convertit.convertitapp.viewModel.MainViewModel


class CurrenciesListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var _binding: CardviewCurrenciesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel = MainViewModel()

    private var currenciesList : List<CurrenciesListBase> = MainCurrenciesList.currenciesList

    fun getListUpdated(){
        //TODO("Montar a função usando um observer que irá observar as mudanças de estado do LiveData de Lista da ViewModel")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        _binding = CardviewCurrenciesBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        getListUpdated()
        return CurrencyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is CurrencyViewHolder -> {
                val currency = currenciesList[position]
                holder.bind(currency)
            }
        }
    }

    override fun getItemCount(): Int = currenciesList.size

    class CurrencyViewHolder constructor(
        val binding: CardviewCurrenciesBinding
    ): RecyclerView.ViewHolder(binding.root){

        fun bind(currency: CurrenciesListBase){
            binding.tvAcronymCurrency.text = currency.acronym
            binding.tvCurrencyName.text = currency.currencyName
            binding.tvCurrencyValue.text = currency.currencyValue.toString()
        }
    }
}