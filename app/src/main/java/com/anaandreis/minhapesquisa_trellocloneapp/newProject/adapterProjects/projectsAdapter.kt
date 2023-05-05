package com.anaandreis.minhapesquisa_trellocloneapp.newProject.adapterProjects

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.anaandreis.minhapesquisa_trellocloneapp.R
import com.anaandreis.minhapesquisa_trellocloneapp.newProject.Project

class projectsAdapter (private val context: Context,
                       private val list: ArrayList<Project>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.project_recyclerview_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        (holder.itemView.findViewById(R.id.projectTitle) as TextView).text = model.name
        (holder.itemView.findViewById(R.id.projectDescription) as TextView).text = model.projectDescription
    }

    fun clear() {
        list.clear()
        notifyDataSetChanged()
    }

    fun addAll(newList: List<Project>) {
        list.addAll(newList)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}

