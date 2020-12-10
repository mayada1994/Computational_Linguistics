package com.mayada1994.zipfslaw.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mayada1994.zipfslaw.R
import com.mayada1994.zipfslaw.entities.ZipfWord
import kotlinx.android.synthetic.main.item_word.view.*

class WordAdapter(
    private val items: List<ZipfWord>
) : RecyclerView.Adapter<WordAdapter.WordViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.item_word, parent, false)
        return WordViewHolder(layout)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        holder.onBind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class WordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun onBind(item: ZipfWord) {
            with(itemView) {
                txtId.text = item.id.toString()
                txtWord.text = item.word
                txtFrequency.text = item.frequency.toString()
                txtRank.text = item.rank.toString()
            }
        }
    }

}