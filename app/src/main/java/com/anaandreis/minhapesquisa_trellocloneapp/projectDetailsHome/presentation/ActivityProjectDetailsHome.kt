package com.anaandreis.minhapesquisa_trellocloneapp.projectDetailsHome.presentation

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anaandreis.minhapesquisa_trellocloneapp.R
import com.anaandreis.minhapesquisa_trellocloneapp.databinding.ActivityProjectDetailsHomeBinding
import com.anaandreis.minhapesquisa_trellocloneapp.newProject.Project
import com.anaandreis.minhapesquisa_trellocloneapp.newProject.adapterProjects.projectsAdapter
import com.anaandreis.minhapesquisa_trellocloneapp.newProject.adapterProjects.samplesAdapter
import com.anaandreis.minhapesquisa_trellocloneapp.newProject.adapterProjects.tasksAdapter
import com.anaandreis.minhapesquisa_trellocloneapp.newProject.adapterProjects.warningsAdapter
import com.anaandreis.minhapesquisa_trellocloneapp.projectDetailsHome.data.Samples
import com.anaandreis.minhapesquisa_trellocloneapp.projectDetailsHome.data.Tasks
import com.anaandreis.minhapesquisa_trellocloneapp.projectDetailsHome.data.Warning
import com.anaandreis.minhapesquisa_trellocloneapp.projectDetailsHome.domain.ProjectDetailsViewModel
import com.anaandreis.minhapesquisa_trellocloneapp.projectsHome.presentation.ProjectActivity
import com.anaandreis.minhapesquisa_trellocloneapp.utils.Constants
import com.anaandreis.minhapesquisa_trellocloneapp.utils.DialogFunctions
import com.google.firebase.firestore.FirebaseFirestore

class ActivityProjectDetailsHome : AppCompatActivity() {

    private lateinit var binding: ActivityProjectDetailsHomeBinding
    private val sharedViewModel: ProjectDetailsViewModel by viewModels()
    val Dialog = DialogFunctions()
    private lateinit var actionBar: androidx.appcompat.app.ActionBar
    private val mFirestore = FirebaseFirestore.getInstance()
    private var projectdocumentID = ""
    private lateinit var recyclerView: RecyclerView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProjectDetailsHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        if(intent.hasExtra(Constants.DOCUMENT_ID)) {
            projectdocumentID = intent.getStringExtra(Constants.DOCUMENT_ID)!!
            sharedViewModel.currentProjectId.value = projectdocumentID
            Log.d("ID FROM INTENT", "{$projectdocumentID}")
        }

        actionBar = supportActionBar!!
        actionBar?.apply {
            // Customize the back button
            setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_24)

            // Showing the back button in the action bar
            setDisplayHomeAsUpEnabled(true)
        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // Handle the back button press
                val intent = Intent(this, ProjectActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onStart() {
        super.onStart()


        getProjectDetails(projectdocumentID)

        Dialog.showProgressDialog(this, resources.getString(R.string.please_wait))

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

        sharedViewModel.samplesList.observe(this, Observer { samplesList ->
            if ( samplesList.isNotEmpty()) {
                populateSamplesRecyclerView(samplesList as ArrayList<Samples>)
            }
        })


        binding.warningButton.setOnClickListener { showNewWarningProjectSheet() }
        binding.taskButton.setOnClickListener { showNewTasksProjectSheet()}
        binding.amostrasButton.setOnClickListener{ showNewSamplesProjectSheet()}


        binding.bottomNavigation.setOnItemSelectedListener() { item ->
            when(item.itemId) {
                R.id.avisos -> {
                    // Respond to navigation item 1 click
                    binding.taskButton.visibility= View.GONE
                    binding.amostrasButton.visibility=View.GONE
                    binding.warningButton.visibility=View.VISIBLE
                    binding.avisosRecyclerView.visibility=View.VISIBLE
                    binding.tasksRecyclerView.visibility=View.GONE
                    binding.samplesRecyclerView.visibility=View.GONE


                    true
                }
                R.id.tarefas -> {
                    // Respond to navigation item 1 click
                    binding.taskButton.visibility= View.VISIBLE
                    binding.amostrasButton.visibility=View.GONE
                    binding.warningButton.visibility=View.GONE
                    binding.tasksRecyclerView.visibility=View.VISIBLE
                    binding.samplesRecyclerView.visibility=View.GONE

                    sharedViewModel.getTaskList()

                    binding.avisosRecyclerView.visibility=View.GONE


                    true
                }
                R.id.amostras -> {
                    binding.taskButton.visibility= View.GONE
                    binding.amostrasButton.visibility=View.VISIBLE
                    binding.warningButton.visibility=View.GONE
                    binding.avisosRecyclerView.visibility=View.GONE
                    binding.tasksRecyclerView.visibility=View.GONE


                    sharedViewModel.getSamplesList()
                    binding.samplesRecyclerView.visibility=View.VISIBLE
                    true
                }
                else -> false
            }
        }
    }



    fun showNewWarningProjectSheet() {
        val modalBottomSheet = FragmentNewWarning()
        modalBottomSheet.show(supportFragmentManager, "ProjectSheet")
    }

    fun showNewTasksProjectSheet() {
        val modalBottomSheet = FragmentNewTask()
        modalBottomSheet.show(supportFragmentManager, "ProjectSheet")
    }

    fun showNewSamplesProjectSheet() {
        val modalBottomSheet = FragmentNewSample()
        modalBottomSheet.show(supportFragmentManager, "ProjectSheet")
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
                sharedViewModel.currentProjectId.value= board.documentID
                Log.d("PROJECT ID FROM GET PROJECT", "{${sharedViewModel.currentProjectId.value}}")

            }.addOnFailureListener {
                Log.e("Project Details", "failed")
            }
    }



    fun projectDetails(project: Project){
        Dialog.hideProgressDialog(this)
        sharedViewModel.currentProjectInfo = project
        supportActionBar?.title = project.name

    }


    fun populateWarningsRecyclerView(WarningsList: ArrayList<Warning>) {

        binding.avisosRecyclerView.visibility = View.VISIBLE
        recyclerView = binding.avisosRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = warningsAdapter(this, ArrayList())
        adapter.clear() // clear the adapter's list before adding new items
        adapter.addAll(WarningsList) // add new items to the adapter's list
        recyclerView.adapter = adapter

        adapter.setOnDeleteButtonClickListener(object : warningsAdapter.OnDeleteButtonClickListener {
            override fun onDeleteButtonClick(position: Int, model: Warning) {
                // Implement the logic here when the delete button is clicked
            }
        })


    }

    fun populateTasksRecyclerView(TasksList: ArrayList<Tasks>) {

        binding.tasksRecyclerView.visibility = View.VISIBLE
        recyclerView = binding.tasksRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = tasksAdapter(this, ArrayList())
        adapter.clear() // clear the adapter's list before adding new items
        adapter.addAll(TasksList) // add new items to the adapter's list
        recyclerView.adapter = adapter

        adapter.setOnDeleteButtonClickListener(object : tasksAdapter.OnDeleteButtonClickListener {
            override fun onDeleteButtonClick(position: Int, model: Tasks) {
                // Implement the logic here when the delete button is clicked
            }
        })

    }


    fun populateSamplesRecyclerView(TasksList: ArrayList<Samples>) {

        recyclerView = binding.samplesRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = samplesAdapter(this, ArrayList())
        adapter.clear() // clear the adapter's list before adding new items
        adapter.addAll(TasksList) // add new items to the adapter's list
        recyclerView.adapter = adapter
    }
}
