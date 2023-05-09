package com.anaandreis.minhapesquisa_trellocloneapp.projectDetailsHome.presentation

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.anaandreis.minhapesquisa_trellocloneapp.R
import com.anaandreis.minhapesquisa_trellocloneapp.databinding.ActivityProjectDetailsHomeBinding
import com.anaandreis.minhapesquisa_trellocloneapp.newProject.Project
import com.anaandreis.minhapesquisa_trellocloneapp.utils.Constants
import com.anaandreis.minhapesquisa_trellocloneapp.utils.DialogFunctions
import com.google.firebase.firestore.FirebaseFirestore

class ActivityProjectDetailsHome : AppCompatActivity() {

    private lateinit var binding: ActivityProjectDetailsHomeBinding
    private val sharedViewModel: ProjectDetailsViewModel by viewModels()
    val Dialog = DialogFunctions()
    private lateinit var actionBar: androidx.appcompat.app.ActionBar
    private val mFirestore = FirebaseFirestore.getInstance()
    var projectdocumentID=""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProjectDetailsHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        this.findNavController(R.id.myNavHostFragment)


        if(intent.hasExtra(Constants.DOCUMENT_ID)) {
            projectdocumentID = intent.getStringExtra(Constants.DOCUMENT_ID)!!
        }

        Dialog.showProgressDialog(this, resources.getString(R.string.please_wait))

        getProjectDetails(projectdocumentID)

        // calling the action bar
        actionBar = supportActionBar ?: return

        if (actionBar != null) {

            // Customize the back button
            actionBar.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_24);

            // showing the back button in action bar
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


    }

    // this event will enable the back
    // function to the button on press
    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onContextItemSelected(item)
    }


    fun projectDetails(project: Project){
        Dialog.hideProgressDialog(this)
        binding.topAppBar.title= project.name

    }



    fun getProjectDetails(documentId: String) {
        mFirestore.collection(Constants.PROJECTS)
            .document(documentId)
            .get()
            .addOnSuccessListener {
                    document ->
                Log.e("GetProjectListDEtails", document.toString())
                val board = document.toObject(Project::class.java)!!
                board.documentID = document.id

                projectDetails(document.toObject(Project::class.java)!!)


            }.addOnFailureListener {
                Log.e("Project Details", "failed")
            }
    }
}
