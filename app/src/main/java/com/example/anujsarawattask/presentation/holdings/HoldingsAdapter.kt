package com.example.anujsarawattask.presentation.holdings

import android.annotation.SuppressLint
import android.text.SpannableString
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.anujsarawattask.R
import com.example.anujsarawattask.databinding.ItemHoldingBinding
import com.example.anujsarawattask.domain.model.Holding
import com.example.anujsarawattask.utils.AppUtils
import java.text.NumberFormat
import java.util.Locale

class HoldingsAdapter :
    ListAdapter<Holding, HoldingsAdapter.HoldingViewHolder>(HoldingDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HoldingViewHolder {
        val binding = ItemHoldingBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HoldingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HoldingViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class HoldingViewHolder(
        private val binding: ItemHoldingBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val currencyFormat = NumberFormat.getCurrencyInstance(
            Locale.Builder()
                .setLanguage("en")
                .setRegion("IN")
                .build()
        ).apply {
            maximumFractionDigits = 2
        }

        @SuppressLint("SetTextI18n")
        fun bind(holding: Holding) {
            binding.apply {
                tvSymbol.text = holding.symbol

                val pnl = holding.pnl

                tvQuantity.text = getSpannableString(
                    itemView.context.getString(R.string.net_qty),
                    holding.quantity.toString(),
                    "#222222".toColorInt()
                )
                tvLtp.text = getSpannableString(
                    itemView.context.getString(R.string.ltp),
                    currencyFormat.format(holding.ltp),
                    "#222222".toColorInt()
                )
                tvPnl.text = getSpannableString(
                    itemView.context.getString(R.string.pnl),
                    currencyFormat.format(pnl),
                    AppUtils.getPnlColor(itemView.context, pnl)
                )
            }
        }

        private fun getSpannableString(
            identifier: String,
            value: String,
            valueColor: Int
        ): SpannableString {
            val text = "$identifier $value"
            val spannable = SpannableString(text)
            val labelEnd = identifier.length

            spannable.setSpan(
                AbsoluteSizeSpan(12, true),
                0,
                labelEnd,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            spannable.setSpan(
                AbsoluteSizeSpan(16, true),
                labelEnd,
                text.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            spannable.setSpan(
                ForegroundColorSpan(valueColor),
                labelEnd,
                text.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            return spannable
        }
    }

    private class HoldingDiffCallback : DiffUtil.ItemCallback<Holding>() {
        override fun areItemsTheSame(oldItem: Holding, newItem: Holding): Boolean {
            return oldItem.symbol == newItem.symbol
        }

        override fun areContentsTheSame(oldItem: Holding, newItem: Holding): Boolean {
            return oldItem == newItem
        }
    }
}