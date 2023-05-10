package com.anaandreis.minhapesquisa_trellocloneapp.newProject.adapterProjects

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.anaandreis.minhapesquisa_trellocloneapp.R
import com.anaandreis.minhapesquisa_trellocloneapp.newProject.Project
import com.anaandreis.minhapesquisa_trellocloneapp.projectDetailsHome.data.Tasks
import com.anaandreis.minhapesquisa_trellocloneapp.projectDetailsHome.data.Warning
import java.text.SimpleDateFormat
import java.util.Locale

class tasksAdapter (private val context: Context,
                       private val list: ArrayList<Tasks>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tasks_recyclerview_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        (holder.itemView.findViewById(R.id.taskDescriptionTextview) as TextView).text = model.task
        (holder.itemView.findViewById(R.id.responsableTextView) as TextView).text = model.responsable

        val dateFormat = SimpleDateFormat("dd/MM", Locale.getDefault())
        val formattedDate = dateFormat.format(model.Date)

        (holder.itemView.findViewById(R.id.prazoTextView) as TextView).text = formattedDate
    }

    fun clear() {
        list.clear()
        notifyDataSetChanged()
    }

    fun addAll(newList: List<Tasks>) {
        list.addAll(newList)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}

