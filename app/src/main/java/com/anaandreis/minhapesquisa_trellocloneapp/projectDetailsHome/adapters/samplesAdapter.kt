package com.anaandreis.minhapesquisa_trellocloneapp.newProject.adapterProjects

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.anaandreis.minhapesquisa_trellocloneapp.R
import com.anaandreis.minhapesquisa_trellocloneapp.newProject.Project
import com.anaandreis.minhapesquisa_trellocloneapp.projectDetailsHome.data.Samples
import com.anaandreis.minhapesquisa_trellocloneapp.projectDetailsHome.data.Tasks
import com.anaandreis.minhapesquisa_trellocloneapp.projectDetailsHome.data.Warning
import java.text.SimpleDateFormat
import java.util.Locale

class samplesAdapter (private val context: Context,
                    private val list: ArrayList<Samples>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.samples_recyclerview_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        val dateFormat = SimpleDateFormat("dd/MM", Locale.getDefault())
        val formattedDate = dateFormat.format(model.date)

        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val formattedTime = timeFormat.format(model.date)


        (holder.itemView.findViewById(R.id.sampleDateText) as TextView).text = formattedDate
        (holder.itemView.findViewById(R.id.sampleHourText) as TextView).text = formattedTime
        (holder.itemView.findViewById(R.id.sampleAuthorTextView) as TextView).text = model.author


    }

    fun clear() {
        list.clear()
        notifyDataSetChanged()
    }

    fun addAll(newList: List<Samples>) {
        list.addAll(newList)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}

