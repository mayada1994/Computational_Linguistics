package com.mayada1994.zipfslaw.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mayada1994.zipfslaw.R
import com.mayada1994.zipfslaw.adapters.WordAdapter
import com.mayada1994.zipfslaw.entities.TextFile
import com.mayada1994.zipfslaw.entities.ZipfWord
import com.mayada1994.zipfslaw.viewmodels.TableViewModel
import kotlinx.android.synthetic.main.fragment_table.*

class TableFragment : Fragment() {
    private var textFile: TextFile? = null

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
            buildTable(
                viewModel.analyzeText(it.text)
            )
        }

        btnBack.setOnClickListener {
            fragmentManager?.popBackStack()
        }
    }

    private fun buildTable(zipfWords: List<ZipfWord>) {
        rvWords.adapter = WordAdapter(zipfWords)
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
}