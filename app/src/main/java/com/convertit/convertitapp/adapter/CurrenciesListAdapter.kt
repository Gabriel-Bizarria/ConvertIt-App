package com.convertit.convertitapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.convertit.convertitapp.MainCurrenciesList
import com.convertit.convertitapp.databinding.CardviewCurrenciesBinding
import com.convertit.convertitapp.models.CurrenciesListResult

class CurrenciesListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var _binding: CardviewCurrenciesBinding? = null
    private val binding get() = _binding!!

    private var currenciesList : List<CurrenciesListResult> = MainCurrenciesList.currenciesList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        _binding = CardviewCurrenciesBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
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

        fun bind(currency: CurrenciesListResult){
            binding.tvAcronymCurrency.text = currency.acronym
            binding.tvCurrencyName.text = currency.currencyName
            binding.tvCurrencyValue.text = currency.currencyValue.toString()
        }
    }
}