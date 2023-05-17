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
import com.anaandreis.minhapesquisa_trellocloneapp.projectDetailsHome.data.Warning
import com.google.firebase.firestore.FirebaseFirestore

class warningsAdapter (private val context: Context,
                       private val list: ArrayList<Warning>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var onDeleteButtonClickListener: warningsAdapter.OnDeleteButtonClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.warnings_recyclerview_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        (holder.itemView.findViewById(R.id.warningText) as TextView).text = model.warning
        (holder.itemView.findViewById(R.id.autorTextView) as TextView).text = model.author

        holder.itemView.findViewById<ImageView>(R.id.deletionIconButton)?.setOnClickListener {
            showConfirmationDialog(position, model)
        }
    }

    fun clear() {
        list.clear()
        notifyDataSetChanged()
    }

    fun addAll(newList: List<Warning>) {
        list.addAll(newList)
        notifyDataSetChanged()
    }

    fun setOnDeleteButtonClickListener(onDeleteButtonClickListener: OnDeleteButtonClickListener) {
        this.onDeleteButtonClickListener = onDeleteButtonClickListener
    }

    interface OnDeleteButtonClickListener {
        fun onDeleteButtonClick(position: Int, model: Warning
        )
    }

    private fun showConfirmationDialog(position: Int, model: Warning) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Deletar Aviso")
        builder.setMessage("Tem certeza que quer apagar esse aviso?")
        builder.setPositiveButton("Deletar") { dialog, which ->
            val mFireStore = FirebaseFirestore.getInstance()
            val projectDocumentRef = mFireStore.collection("warning")
                .document(model.warning)

            projectDocumentRef
                .delete()
                .addOnSuccessListener {
                    list.removeAt(position)
                    notifyDataSetChanged()
                    // Call the listener method if available
                    onDeleteButtonClickListener?.onDeleteButtonClick(position, Warning)
                    Toast.makeText(
                        context,
                        "Aviso apagado",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(
                        context,
                        "Falha em apagar aviso: ${exception.message}",
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

