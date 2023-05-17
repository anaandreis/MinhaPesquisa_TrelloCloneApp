package com.anaandreis.minhapesquisa_trellocloneapp.projectDetailsHome.domain

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anaandreis.minhapesquisa_trellocloneapp.FirestoreClass
import com.anaandreis.minhapesquisa_trellocloneapp.newProject.Project
import com.anaandreis.minhapesquisa_trellocloneapp.projectDetailsHome.data.Samples
import com.anaandreis.minhapesquisa_trellocloneapp.projectDetailsHome.data.Tasks
import com.anaandreis.minhapesquisa_trellocloneapp.projectDetailsHome.data.Warning
import com.anaandreis.minhapesquisa_trellocloneapp.utils.Constants
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.launch
import org.checkerframework.checker.units.qual.s
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ProjectDetailsViewModel: ViewModel() {

    private val mFireStore = FirebaseFirestore.getInstance()



    //variables for creating tasks, warnings and samples
    var warning: String = ""
    var warningAuthor: String = ""
    val createWarningResult: MutableLiveData<Boolean> = MutableLiveData()

    var sample: String = ""
    var samplesAuthor: String = ""
    private val currentDate = Date()
    val createSampleResult: MutableLiveData<Boolean> = MutableLiveData()

    var taskDescription:String = ""
    var taskResponsible:String = ""
    var taskDate: Date? = null
    val createTaskResult: MutableLiveData<Boolean> = MutableLiveData()

    //variables for fetching projects, tasks and samples
    var currentProjectInfo: Project = Project()
    var currentProjectId: MutableLiveData<String?> = MutableLiveData()

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



    fun warningSnapshotListener(){
        val collectionRef = currentProjectId.value?.let {
            mFireStore.collection("warnings")
                .whereEqualTo("projectId", it)
        }
        projectsListenerRegistration = collectionRef?.addSnapshotListener { snapshot, exception ->
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
        val collectionRef = currentProjectId.value?.let {
            mFireStore.collection("tasks")
                .whereEqualTo("projectId", it)
        }

        projectsListenerRegistration = collectionRef?.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                Log.e("TASKS", "ERROR", exception)
                return@addSnapshotListener
            }

            val tasks = mutableListOf<Tasks>()
            for (doc in snapshot?.documents ?: emptyList()) {
                val task = doc.toObject(Tasks::class.java)
                if (task != null) {
                    tasks.add(task)

                }
            }

            _taskList.value = tasks
        }
    }

    fun samplesSnapshotListener() {
        val collectionRef = currentProjectId.value?.let {
            mFireStore.collection("samples")
                .whereEqualTo("projectId", it)
        }

        projectsListenerRegistration = collectionRef?.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                Log.e("SAMPLES", "ERROR", exception)
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

    fun createWarning() {

        var warningInfo = Warning(
            warning,
            warningAuthor,
            currentProjectId.value
        )

        Log.d("PROJECT ID WARNING", "{${currentProjectId.value}}")
        val projectDocumentRef = mFireStore.collection("warnings").document()

        projectDocumentRef
            .set(warningInfo, SetOptions.merge())
            .addOnSuccessListener {
                createWarningResult.postValue(true)
            }.addOnFailureListener {
                createWarningResult.postValue(false)
            }
    }


    fun createTask() {


        var taskInfo = Tasks(
            taskDescription,
            taskDate!!,
            taskResponsible,
            currentProjectId.value
        )

        Log.d("TASK", "{${currentProjectId.value}}")
        Log.d("Date", "${taskDate}")
        val projectDocumentRef = mFireStore.collection("tasks").document()

        projectDocumentRef
            .set(taskInfo, SetOptions.merge())
            .addOnSuccessListener {
                createTaskResult.postValue(true)
            }.addOnFailureListener {
                createTaskResult.postValue(false)
            }
    }


    fun createSample() {

        var sampleInfo = Samples(
            sample,
            currentDate,
            samplesAuthor,
            currentProjectId.value
        )

        Log.d("SAMPLE", "{${currentProjectId.value}}")
        val projectDocumentRef = mFireStore.collection("samples").document()

        projectDocumentRef
            .set(sampleInfo, SetOptions.merge())
            .addOnSuccessListener {
                createSampleResult.postValue(true)
            }.addOnFailureListener {
                createSampleResult.postValue(false)
            }
    }

}