package com.convertit.convertitapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.convertit.convertitapp.MainCurrenciesList
import com.convertit.convertitapp.databinding.CardviewCurrenciesBinding
import com.convertit.convertitapp.models.CurrenciesListBase
import com.convertit.convertitapp.viewModel.MainViewModel


class CurrenciesListAdapter (private val viewModel: MainViewModel,
                             private val lifecycleOwner: LifecycleOwner): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var _binding: CardviewCurrenciesBinding? = null
    private val binding get() = _binding!!

    private var currenciesList : List<CurrenciesListBase> = MainCurrenciesList.currenciesList

    fun getListUpdated(data: List<CurrenciesListBase>){
       this.currenciesList = data
        notifyDataSetChanged()
    }

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

        fun bind(currency: CurrenciesListBase){
            binding.tvAcronymCurrency.text = currency.acronym
            binding.tvCurrencyName.text = currency.currencyName
            binding.tvCurrencyValue.text = currency.currencyValue.toString()
        }
    }
}