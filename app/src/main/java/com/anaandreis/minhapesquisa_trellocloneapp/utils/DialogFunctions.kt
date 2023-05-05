package com.anaandreis.minhapesquisa_trellocloneapp.utils

import android.app.Dialog
import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.anaandreis.minhapesquisa_trellocloneapp.R
import com.anaandreis.minhapesquisa_trellocloneapp.signUp.presentation.SignUpFragment
import com.google.android.material.snackbar.Snackbar

class DialogFunctions {
    private var mProgressDialog: Dialog? = null

    fun showProgressDialog(context: Context, text: String) {
          mProgressDialog = Dialog(context)
          mProgressDialog?.setContentView(R.layout.dialog_progress)

          var textView = mProgressDialog?.findViewById<TextView>(R.id.progress_dialog_tv)
          textView?.text = text
          mProgressDialog?.show()
    }

    fun hideProgressDialog(context: Context){
        mProgressDialog?.dismiss()
        mProgressDialog = null
    }

   fun showErrorSnackBar(view: View, message: String) {
       val snackBar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
       snackBar.view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.mainRed))
       snackBar.show()
    }
}