package com.mayada1994.ngram.activities

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.mayada1994.ngram.R
import com.mayada1994.ngram.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val viewModel by lazy {
        MainViewModel(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(this, R.color.monte_carlo)
        }

        setSpinner()

        btnSubmit.setOnClickListener {
            val relevance = viewModel.getRelevance(
                fFirstWord.text.toString(),
                fSecondWord.text.toString(),
                ngramSpinner.position + 1
            )
            txtFirstRelevance.text = relevance.first

            txtSecondRelevance.text = relevance.second
        }
    }

    private fun setSpinner() {
        val ngramValues = arrayListOf(1, 2, 3)
        val spinnerArrayAdapter = ArrayAdapter(
            this, R.layout.spinner_item, ngramValues
        )

        ngramSpinner.setAdapter(spinnerArrayAdapter)

        ngramSpinner.setOnItemClickListener { _, _, position, _ ->
            ngramSpinner.position = position
        }

    }
}