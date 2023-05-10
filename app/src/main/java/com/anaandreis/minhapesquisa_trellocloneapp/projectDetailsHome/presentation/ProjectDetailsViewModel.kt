package com.anaandreis.minhapesquisa_trellocloneapp.projectDetailsHome.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anaandreis.minhapesquisa_trellocloneapp.FirestoreClass
import com.anaandreis.minhapesquisa_trellocloneapp.newProject.NewProjectViewModel
import com.anaandreis.minhapesquisa_trellocloneapp.newProject.Project
import com.anaandreis.minhapesquisa_trellocloneapp.projectDetailsHome.data.Samples
import com.anaandreis.minhapesquisa_trellocloneapp.projectDetailsHome.data.Tasks
import com.anaandreis.minhapesquisa_trellocloneapp.projectDetailsHome.data.Warning
import com.anaandreis.minhapesquisa_trellocloneapp.utils.Constants
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.launch
import org.checkerframework.checker.units.qual.m

class ProjectDetailsViewModel: ViewModel() {

    private val mFireStore = FirebaseFirestore.getInstance()
    var currentProjectInfo: Project = Project()
    var currentProjectId: String = ""
    private val _warningsList: MutableLiveData<List<Warning>> = MutableLiveData()
    private val _taskList: MutableLiveData<List<Tasks>> = MutableLiveData()
    private val _samplesList: MutableLiveData<List<Samples>> = MutableLiveData()

    var projectsListenerRegistration: ListenerRegistration? = null
    val warningsList: LiveData<List<Warning>>
        get() = _warningsList

    val taskList: LiveData<List<Tasks>>
        get() = _taskList

    val samplesList: LiveData<List<Samples>>
        get() = _samplesList


    fun getWarningList() {

        viewModelScope.launch {
            try {
                warningSnapshotListener()
            } catch (e: Exception) {
                Log.e("FETCH WARNING", "ERROR")  // handle error
            }
        }
    }

    fun getTaskList() {
        viewModelScope.launch {
            try {
                taskSnapshotListener()
            } catch (e: Exception) {
                Log.e("FETCH TASKS", "ERROR")  // handle error
            }
        }
    }

    fun getSamplesList() {
        viewModelScope.launch {
            try {
                samplesSnapshotListener()
            } catch (e: Exception) {
                Log.e("FETCH TASKS", "ERROR")  // handle error
            }
        }
    }



    fun warningSnapshotListener() {
        val collectionRef = mFireStore.collection("warnings")

        projectsListenerRegistration = collectionRef.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                Log.e("WARNING", "ERROR", exception)
                return@addSnapshotListener
            }

            val warnings = mutableListOf<Warning>()
            for (doc in snapshot?.documents ?: emptyList()) {
                val warning = doc.toObject(Warning::class.java)
                if (warning != null) {
                    warnings.add(warning)
                    Log.e("Aqui", "{$warnings.size}")
                }
            }

            _warningsList.value = warnings
        }
    }

    fun taskSnapshotListener() {
        val collectionRef = mFireStore.collection("tasks")

        projectsListenerRegistration = collectionRef.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                Log.e("WARNING", "ERROR", exception)
                return@addSnapshotListener
            }

            val tasks = mutableListOf<Tasks>()
            for (doc in snapshot?.documents ?: emptyList()) {
                val task = doc.toObject(Tasks::class.java)
                if (task != null) {
                    tasks.add(task)
                    Log.e("Aqui", "{$tasks.size}")
                }
            }

            _taskList.value = tasks
        }
    }

    fun samplesSnapshotListener() {
        val collectionRef = mFireStore.collection("samples")

        projectsListenerRegistration = collectionRef.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                Log.e("WARNING", "ERROR", exception)
                return@addSnapshotListener
            }

            val samples = mutableListOf<Samples>()
            for (doc in snapshot?.documents ?: emptyList()) {
                val sample = doc.toObject(Samples::class.java)
                if (sample != null) {
                    samples.add(sample)
                    Log.e("Aqui", "{$samples.size}")
                }
            }

            _samplesList.value = samples
        }
    }

}