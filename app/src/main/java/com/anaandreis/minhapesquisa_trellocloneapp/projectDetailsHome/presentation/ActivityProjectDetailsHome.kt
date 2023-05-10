package com.anaandreis.minhapesquisa_trellocloneapp.projectDetailsHome.presentation

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anaandreis.minhapesquisa_trellocloneapp.R
import com.anaandreis.minhapesquisa_trellocloneapp.databinding.ActivityProjectDetailsHomeBinding
import com.anaandreis.minhapesquisa_trellocloneapp.newProject.Project
import com.anaandreis.minhapesquisa_trellocloneapp.newProject.adapterProjects.projectsAdapter
import com.anaandreis.minhapesquisa_trellocloneapp.newProject.adapterProjects.tasksAdapter
import com.anaandreis.minhapesquisa_trellocloneapp.newProject.adapterProjects.warningsAdapter
import com.anaandreis.minhapesquisa_trellocloneapp.projectDetailsHome.data.Tasks
import com.anaandreis.minhapesquisa_trellocloneapp.projectDetailsHome.data.Warning
import com.anaandreis.minhapesquisa_trellocloneapp.utils.Constants
import com.anaandreis.minhapesquisa_trellocloneapp.utils.DialogFunctions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.firestore.FirebaseFirestore

class ActivityProjectDetailsHome : AppCompatActivity() {

    private lateinit var binding: ActivityProjectDetailsHomeBinding
    private val sharedViewModel: ProjectDetailsViewModel by viewModels()
    val Dialog = DialogFunctions()
    private lateinit var actionBar: androidx.appcompat.app.ActionBar
    private val mFirestore = FirebaseFirestore.getInstance()
    var projectdocumentID=""
    private lateinit var recyclerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProjectDetailsHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        sharedViewModel.getWarningList()
        sharedViewModel.warningsList.observe(this, Observer { warningList ->
            if (warningList.isNotEmpty()) {
                populateWarningsRecyclerView(warningList as ArrayList<Warning>)
            }
        })
        sharedViewModel.taskList.observe(this, Observer { taskList ->
            if ( taskList.isNotEmpty()) {
                populateTasksRecyclerView(taskList as ArrayList<Tasks>)
            }
        })


        binding.bottomNavigation.setOnItemSelectedListener() { item ->
            when(item.itemId) {
                R.id.avisos -> {
                    // Respond to navigation item 1 click
                    binding.taskButton.visibility= View.GONE
                    binding.amostrasButton.visibility=View.GONE
                    binding.warningButton.visibility=View.VISIBLE
                    binding.avisosRecyclerView.visibility=View.VISIBLE
                    binding.tasksRecyclerView.visibility=View.GONE


                    true
                }
                R.id.tarefas -> {
                    // Respond to navigation item 1 click
                    binding.taskButton.visibility= View.VISIBLE
                    binding.amostrasButton.visibility=View.GONE
                    binding.warningButton.visibility=View.GONE

                    sharedViewModel.getTaskList()

                    binding.avisosRecyclerView.visibility=View.GONE
                    binding.tasksRecyclerView.visibility=View.VISIBLE

                    true
                }
                R.id.amostras -> {
                    binding.taskButton.visibility= View.GONE
                    binding.amostrasButton.visibility=View.VISIBLE
                    binding.warningButton.visibility=View.GONE
                    binding.avisosRecyclerView.visibility=View.GONE
                    binding.tasksRecyclerView.visibility=View.GONE
                    true
                }
                else -> false
            }
        }

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
        sharedViewModel.currentProjectInfo = project
        sharedViewModel.currentProjectId = project.documentID
        supportActionBar?.title = project.name

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

    fun populateWarningsRecyclerView(WarningsList: ArrayList<Warning>) {

        binding.avisosRecyclerView.visibility = View.VISIBLE
        recyclerView = binding.avisosRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = warningsAdapter(this, ArrayList())
        adapter.clear() // clear the adapter's list before adding new items
        adapter.addAll(WarningsList) // add new items to the adapter's list
        recyclerView.adapter = adapter


    }

    fun populateTasksRecyclerView(TasksList: ArrayList<Tasks>) {

        binding.tasksRecyclerView.visibility = View.VISIBLE
        recyclerView = binding.tasksRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = tasksAdapter(this, ArrayList())
        adapter.clear() // clear the adapter's list before adding new items
        adapter.addAll(TasksList) // add new items to the adapter's list
        recyclerView.adapter = adapter


    }
}
