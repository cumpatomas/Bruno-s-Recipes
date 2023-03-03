package com.cumpatomas.brunosrecipes.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cumpatomas.brunosrecipes.R
import com.cumpatomas.brunosrecipes.domain.model.RecipesModel

class HistoryListAdapter: RecyclerView.Adapter<HistoryListViewHolder>() {

    private var historyList = mutableListOf<RecipesModel>()

    var onItemClickListener: (Int) -> Unit = {}
    var deleteBtnonItemClickListener: (Int, String) -> Unit={ _, _ ->}

    fun setList(list: List<RecipesModel>) {
        historyList = list.toMutableList()
        historyList.sortByDescending { it.datesCooked.last()}
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryListViewHolder {


        val layoutInflater = LayoutInflater.from(parent.context) // aca el contexto lo sacamos de "parent"
        return HistoryListViewHolder(layoutInflater.inflate(R.layout.history_item, parent, false))

    }

    override fun onBindViewHolder(holder: HistoryListViewHolder, position: Int) {
        val item = historyList[position]
        holder.display(item, onItemClickListener, deleteBtnonItemClickListener)

    }

    override fun getItemCount() = historyList.size


}