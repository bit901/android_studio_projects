package com.example.quotesapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class QuotesListAdapter(val context: Context, val list: List<QuotesResponse>, val listener: CopyListener)
    : RecyclerView.Adapter<QuotesViewHolder> () {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuotesViewHolder {
        val layout = LayoutInflater.from(context).inflate(R.layout.list_quotes, parent, false)
        return QuotesViewHolder(layout)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: QuotesViewHolder, position: Int) {
        holder.tv_quote.text = list.get(position).text
        holder.tv_author.text = list.get(position).author
        holder.btn_copy.setOnClickListener {
            listener.onCopyClicked(list.get(holder.adapterPosition).text)
        }
    }

}

class QuotesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var tv_quote: TextView = itemView.findViewById(R.id.tv_quotes)
    var tv_author: TextView = itemView.findViewById(R.id.tv_author)
    var btn_copy: Button = itemView.findViewById(R.id.btn_copy)
}