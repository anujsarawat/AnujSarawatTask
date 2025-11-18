package com.example.anujsarawattask.utils

import android.content.Context
import androidx.core.content.ContextCompat
import com.example.anujsarawattask.R

object AppUtils {

    fun getPnlColor(context: Context, pnl: Double): Int {
        return when {
            pnl > 0 -> ContextCompat.getColor(context, R.color.profit_green)
            pnl < 0 -> ContextCompat.getColor(context, R.color.loss_red)
            else -> ContextCompat.getColor(context, R.color.text_secondary)
        }
    }

}