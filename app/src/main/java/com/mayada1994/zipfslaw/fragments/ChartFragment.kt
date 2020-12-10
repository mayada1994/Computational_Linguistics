package com.mayada1994.zipfslaw.fragments

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.mayada1994.zipfslaw.R
import com.mayada1994.zipfslaw.entities.ZipfWord
import kotlinx.android.synthetic.main.fragment_chart.*

class ChartFragment : Fragment() {

    private var zipfWords: List<ZipfWord>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            zipfWords = it.getParcelableArrayList(WORDS)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(wordChart) {
            setNoDataText("Дані відсутні")
            setNoDataTextColor(ContextCompat.getColor(requireContext(), R.color.prussian_blue))
        }

        zipfWords?.let { words ->
            val entries = words.mapIndexed { index, word ->
                Entry(
                    word.rank.toFloat(),
                    word.frequency.toFloat()
                )
            }.sortedBy { entry -> entry.x.toInt() }
            setXAxis(words.map { it.rank }.distinct().sorted())
            setYAxisRight()
            setYAxisLeft(entries)
            setWeightChart()
            setChartData(entries)
        }
    }

    private fun setXAxis(inputType: List<Int>) {
        val xAxis = wordChart.xAxis
        with(xAxis) {
            textColor = ContextCompat.getColor(requireContext(), R.color.prussian_blue)
            position = XAxis.XAxisPosition.BOTTOM

            if (inputType.size == 1) {
                axisMaximum = 1f
            }

            val values = arrayListOf("0")
            values.addAll(inputType.map { it.toString() })
            val formatter = IndexAxisValueFormatter(values)
            setDrawGridLines(true)
            enableGridDashedLine(11f, 11f, 0f)
            granularity = 1f
            valueFormatter = formatter
            gridColor = ContextCompat.getColor(requireContext(), R.color.white)
            mLabelWidth = 2
        }
    }

    private fun setYAxisRight() {
        val yAxisRight = wordChart.axisRight
        yAxisRight.isEnabled = false
        yAxisRight.gridColor = ContextCompat.getColor(requireContext(), R.color.white)
    }

    private fun setYAxisLeft(entries: List<Entry>) {
        val yAxisLeft = wordChart.axisLeft
        with(yAxisLeft) {
            granularity = 1f
            removeAllLimitLines()
            textColor = ContextCompat.getColor(requireContext(), R.color.prussian_blue)
            gridColor = ContextCompat.getColor(requireContext(), R.color.white)
            axisMaximum = entries.maxOf { it.y } + 5
            axisMinimum = 0f
            zeroLineWidth = 2.0f
        }
    }

    private fun setWeightChart() {
        with(wordChart) {
            setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.monte_carlo))
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            setNoDataText("Дані відсутні")
            setNoDataTextColor(ContextCompat.getColor(requireContext(), R.color.prussian_blue))
            legend.isEnabled = false
            description.isEnabled = false
        }
    }

    private fun setChartData(mEntries: List<Entry>) {
        val dataSet = LineDataSet(mEntries, "")
        with(dataSet) {
            setDrawCircles(false)
            setDrawCircleHole(false)
            enableDashedLine(1f, 1f, 0f)
            setDrawHorizontalHighlightIndicator(false)
            setDrawVerticalHighlightIndicator(false)
            setDrawValues(false)
            lineWidth = 3.0f
            color = ContextCompat.getColor(requireContext(), R.color.prussian_blue)
        }

        wordChart.data = LineData(dataSet)
        wordChart.setPinchZoom(false)
        wordChart.isDoubleTapToZoomEnabled = false
        wordChart.animateXY(800, 800)
    }

    companion object {
        private const val WORDS = "words"

        @JvmStatic
        fun newInstance(zipfWords: List<ZipfWord>) =
            ChartFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(WORDS, zipfWords as ArrayList<out Parcelable>)
                }
            }
    }
}