package com.anaandreis.minhapesquisa_trellocloneapp.projectDetailsHome.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import com.anaandreis.minhapesquisa_trellocloneapp.newProject.NewProjectViewModel
import com.anaandreis.minhapesquisa_trellocloneapp.newProject.Project
import com.anaandreis.minhapesquisa_trellocloneapp.utils.Constants
import com.google.firebase.firestore.FirebaseFirestore

class ProjectDetailsViewModel: ViewModel() {

    var currentProjectName: String = ""

    private val mFirestore = FirebaseFirestore.getInstance()

    fun getProjectDetails(documentId: String) {
        mFirestore.collection(Constants.PROJECTS)
            .document(documentId)
            .get()
            .addOnSuccessListener {
                    document ->
                Log.e("GetProjectListDEtails", document.toString())
                val board = document.toObject(Project::class.java)!!
                board.documentID = document.id


            }.addOnFailureListener {
               Log.e("Project Details", "failed")
            }
    }
}