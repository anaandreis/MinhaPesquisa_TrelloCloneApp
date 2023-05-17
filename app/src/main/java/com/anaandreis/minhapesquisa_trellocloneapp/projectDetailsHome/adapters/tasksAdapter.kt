package com.anaandreis.minhapesquisa_trellocloneapp.newProject.adapterProjects

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.anaandreis.minhapesquisa_trellocloneapp.R
import com.anaandreis.minhapesquisa_trellocloneapp.newProject.Project
import com.anaandreis.minhapesquisa_trellocloneapp.projectDetailsHome.data.Tasks
import com.anaandreis.minhapesquisa_trellocloneapp.projectDetailsHome.data.Warning
import com.anaandreis.minhapesquisa_trellocloneapp.utils.Constants
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Locale

class tasksAdapter (private val context: Context,
                       private val list: ArrayList<Tasks>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var onDeleteButtonClickListener: tasksAdapter.OnDeleteButtonClickListener? = null
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
        (holder.itemView.findViewById(R.id.responsableTextView) as TextView).text = model.responsible

        val dateFormat = SimpleDateFormat("dd/MM", Locale.getDefault())
        val formattedDate = dateFormat.format(model.date)

        (holder.itemView.findViewById(R.id.prazoTextView) as TextView).text = formattedDate

        holder.itemView.findViewById<ImageView>(R.id.deleteButton)?.setOnClickListener {
            showConfirmationDialog(position, model)
        }
    }

    fun clear() {
        list.clear()
        notifyDataSetChanged()
    }

    fun addAll(newList: List<Tasks>) {
        list.addAll(newList)
        notifyDataSetChanged()
    }

    fun setOnDeleteButtonClickListener(onDeleteButtonClickListener: OnDeleteButtonClickListener) {
        this.onDeleteButtonClickListener = onDeleteButtonClickListener
    }

    interface OnDeleteButtonClickListener {
        fun onDeleteButtonClick(position: Int, model: Tasks)
    }

    private fun showConfirmationDialog(position: Int, model: Tasks) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Deletar projeto")
        builder.setMessage("Tem certeza que quer apagar esse projeto?")
        builder.setPositiveButton("Deletar") { dialog, which ->
            val mFireStore = FirebaseFirestore.getInstance()
            val projectDocumentRef = mFireStore.collection("tasks")
                .document(model.task)

            projectDocumentRef
                .delete()
                .addOnSuccessListener {
                    list.removeAt(position)
                    notifyDataSetChanged()
                    // Call the listener method if available
                    onDeleteButtonClickListener?.onDeleteButtonClick(position, model)
                    Toast.makeText(
                        context,
                        "Tarefa apagada",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(
                        context,
                        "Falha em apagar tarefa: ${exception.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
        builder.setNegativeButton("Cancelar") { dialog, which ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}

