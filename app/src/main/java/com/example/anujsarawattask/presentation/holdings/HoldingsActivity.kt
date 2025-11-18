package com.example.anujsarawattask.presentation.holdings

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.anujsarawattask.R
import com.example.anujsarawattask.databinding.ActivityHoldingsBinding
import com.example.anujsarawattask.domain.model.PortfolioSummary
import com.example.anujsarawattask.utils.AppUtils
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@AndroidEntryPoint
class HoldingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHoldingsBinding

    private val viewModel: HoldingsViewModel by viewModels()

    private val holdingsAdapter = HoldingsAdapter()

    private val currencyFormat = NumberFormat.getCurrencyInstance(
        Locale.Builder()
            .setLanguage("en")
            .setRegion("IN")
            .build()
    ).apply {
        maximumFractionDigits = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHoldingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerView()
        setupSwipeRefresh()
        setupSummaryCard()
        observeViewModel()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
    }

    private fun setupRecyclerView() {
        binding.holdingsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@HoldingsActivity)
            adapter = holdingsAdapter
            setHasFixedSize(true)
        }
        val divider = DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        divider.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider_horizontal)!!)
        binding.holdingsRecyclerView.addItemDecoration(divider)

    }

    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout.apply {
            setColorSchemeColors(
                ContextCompat.getColor(this@HoldingsActivity, R.color.purple_500)
            )
            setOnRefreshListener {
                viewModel.refreshHoldings()
            }
        }
    }

    private fun setupSummaryCard() {
        binding.portfolioSummary.rlPnL.setOnClickListener {
            viewModel.toggleSummaryExpansion()
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewState.collect { state ->
                    updateUi(state)
                }
            }
        }
    }

    private fun updateUi(state: HoldingsViewState) {
        binding.progressBar.visibility = if (state.isLoading) View.VISIBLE else View.GONE
        binding.swipeRefreshLayout.isRefreshing = state.isRefreshing

        if (state.holdings.isNotEmpty()) {
            holdingsAdapter.submitList(state.holdings)
        }

        state.portfolioSummary?.let { summary ->
            updatePortfolioSummary(summary)
        }

        updateSummaryExpansion(state.isSummaryExpanded)

        state.error?.let { error ->
            showError(error)
            viewModel.clearError()
        }
    }

    private fun updatePortfolioSummary(summary: PortfolioSummary) {
        binding.portfolioSummary.apply {
            binding.portfolioSummary.root.visibility = View.VISIBLE
            tvCurrentValue.text = currencyFormat.format(summary.currentValue)
            tvTotalInvestment.text = currencyFormat.format(summary.totalInvestment)
            tvTodaysPnl.text = currencyFormat.format(summary.todaysPnl)

            val totalPnl = currencyFormat.format(summary.totalPnl)
            val indiaLocale = Locale.Builder().setLanguage("en").setRegion("IN").build()
            val pct = String.format(indiaLocale, "%.2f", summary.totalPnlPercentage)
            val fullText = "$totalPnl ($pct%)"
            val spannable = SpannableString(fullText)
            spannable.setSpan(
                AbsoluteSizeSpan(12, true),
                totalPnl.length,
                fullText.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            tvTotalPnl.text = spannable
            tvTotalPnl.setTextColor(AppUtils.getPnlColor(this@HoldingsActivity, summary.totalPnl))
        }
    }

    private fun updateSummaryExpansion(isExpanded: Boolean) {
        binding.portfolioSummary.apply {
            if (isExpanded) {
                expandableContent.visibility = View.VISIBLE
                divider.visibility = View.VISIBLE
                tvPnl.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_expand_more,
                    0
                )
                val fadeIn =
                    AnimationUtils.loadAnimation(this@HoldingsActivity, android.R.anim.fade_in)
                expandableContent.startAnimation(fadeIn)
            } else {
                expandableContent.visibility = View.GONE
                divider.visibility = View.GONE
                tvPnl.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_expand_less,
                    0
                )
            }
        }
    }

    @SuppressLint("RestrictedApi")
    private fun showError(message: String) {
        val snackbar = Snackbar.make(binding.root, "", Snackbar.LENGTH_INDEFINITE)
        val snackbarLayout = snackbar.view as Snackbar.SnackbarLayout

        snackbarLayout.removeAllViews()

        val customView = layoutInflater.inflate(
            R.layout.snackbar_retry_dismiss,
            snackbarLayout,
            false
        )

        customView.findViewById<TextView>(R.id.tvMessage).text = message

        customView.findViewById<Button>(R.id.btnRetry).setOnClickListener {
            viewModel.refreshHoldings()
            snackbar.dismiss()
        }

        customView.findViewById<Button>(R.id.btnDismiss).setOnClickListener {
            snackbar.dismiss()
        }

        snackbarLayout.addView(customView)

        snackbar.show()
    }
}