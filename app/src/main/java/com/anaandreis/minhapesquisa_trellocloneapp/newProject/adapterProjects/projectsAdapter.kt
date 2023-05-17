package com.anaandreis.minhapesquisa_trellocloneapp.newProject.adapterProjects

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.anaandreis.minhapesquisa_trellocloneapp.R
import com.anaandreis.minhapesquisa_trellocloneapp.newProject.Project
import com.anaandreis.minhapesquisa_trellocloneapp.utils.Constants
import com.google.firebase.firestore.FirebaseFirestore


class projectsAdapter (private val context: Context,
                       private val list: ArrayList<Project>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var onClickListener: OnClickListener? = null
    private var onDeleteButtonClickListener: OnDeleteButtonClickListener? = null


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

        holder.itemView.setOnClickListener {
            onClickListener?.onClick(position, model)
        }

        holder.itemView.findViewById<ImageView>(R.id.deleteIconButton)?.setOnClickListener {
            showConfirmationDialog(position, model)
        }
    }


    fun clear() {
        list.clear()
        notifyDataSetChanged()
    }

    fun addAll(newList: List<Project>) {
        list.addAll(newList)
        notifyDataSetChanged()
    }

    fun setOnDeleteButtonClickListener(onDeleteButtonClickListener: OnDeleteButtonClickListener) {
        this.onDeleteButtonClickListener = onDeleteButtonClickListener
    }

    interface OnDeleteButtonClickListener {
        fun onDeleteButtonClick(position: Int, model: Project)
    }

    interface OnClickListener{
        fun onClick(position: Int, model: Project)
    }

    fun setOnClickListener(onClickListener: OnClickListener){
        this.onClickListener = onClickListener

    }

    private fun showConfirmationDialog(position: Int, model: Project) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Deletar projeto")
        builder.setMessage("Tem certeza que quer apagar esse projeto?")
        builder.setPositiveButton("Deletar") { dialog, which ->
            val mFireStore = FirebaseFirestore.getInstance()
            val projectDocumentRef = mFireStore.collection(Constants.PROJECTS)
                .document(model.documentID)

            projectDocumentRef
                .delete()
                .addOnSuccessListener {
                    list.removeAt(position)
                    notifyDataSetChanged()
                    // Call the listener method if available
                    onDeleteButtonClickListener?.onDeleteButtonClick(position, model)
                    Toast.makeText(
                        context,
                        "Projeto apagado",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(
                        context,
                        "Falha em apagar projeto: ${exception.message}",
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



