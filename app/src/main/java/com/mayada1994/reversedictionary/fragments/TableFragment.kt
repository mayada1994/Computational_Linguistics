package com.mayada1994.reversedictionary.fragments

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.mayada1994.reversedictionary.R
import com.mayada1994.reversedictionary.adapters.WordAdapter
import com.mayada1994.reversedictionary.entities.TextFile
import com.mayada1994.reversedictionary.entities.ZipfWord
import com.mayada1994.reversedictionary.viewmodels.TableViewModel
import kotlinx.android.synthetic.main.fragment_table.*
import kotlinx.android.synthetic.main.sort_dialog.view.*

class TableFragment : Fragment() {

    private var textFile: TextFile? = null

    private var zipfWords: List<ZipfWord> = emptyList()

    private val viewModel by lazy {
        TableViewModel(requireActivity().application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            textFile = it.getParcelable(TEXT_FILE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_table, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textFile?.let {
            txtFilename.text = it.filename

            zipfWords = viewModel.analyzeText(it.text)

            updateTable(zipfWords)

            btnSort.setOnClickListener { showSortOptions() }
        }

        btnBack.setOnClickListener {
            fragmentManager?.popBackStack()
        }
    }

    private fun updateTable(zipfWords: List<ZipfWord>, gravity: Int = Gravity.CENTER) {
        rvWords.adapter = WordAdapter(zipfWords, gravity)
    }

    companion object {
        private const val TEXT_FILE = "text_file"

        @JvmStatic
        fun newInstance(textFile: TextFile) =
            TableFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(TEXT_FILE, textFile)
                }
            }
    }

    private fun showSortOptions() {
        val dialogView = layoutInflater.inflate(R.layout.sort_dialog, null)
        val alertDialog = AlertDialog.Builder(requireContext()).setView(dialogView).create()

        with(dialogView) {
            btnRegular.setOnClickListener {
                updateTable(zipfWords.sortedBy { it.word }, Gravity.START)
                alertDialog.dismiss()
            }
            btnFrequency.setOnClickListener {
                updateTable(zipfWords.sortedByDescending { it.frequency }, Gravity.START)
                alertDialog.dismiss()
            }
            btnReverse.setOnClickListener {
                updateTable(viewModel.getReverseDictionary(zipfWords), Gravity.END)
                alertDialog.dismiss()
            }
        }
        alertDialog.show()
    }
}