package com.example.moneydonation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.moneydonation.R

class TransactionAdapter(private val transactions: MutableList<com.example.moneydonation.ui.TransactionItem>) :
    RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.tvTransactionName)
        val type: TextView = itemView.findViewById(R.id.tvTransactionType)
        val category: TextView = itemView.findViewById(R.id.tvTransactionCategory)
        val amount: TextView = itemView.findViewById(R.id.tvTransactionAmount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction, parent, false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactions[position]
        holder.name.text = transaction.userName
        holder.type.text = transaction.type
        holder.category.text = transaction.campaign
        holder.amount.text = transaction.amount
    }

    override fun getItemCount(): Int = transactions.size
}
