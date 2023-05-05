package com.anaandreis.minhapesquisa_trellocloneapp.projectDetailsHome.domain

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anaandreis.minhapesquisa_trellocloneapp.FirestoreClass
import com.anaandreis.minhapesquisa_trellocloneapp.newProject.Project
import com.anaandreis.minhapesquisa_trellocloneapp.projectDetailsHome.data.Warning
import com.anaandreis.minhapesquisa_trellocloneapp.utils.Constants
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.SetOptions
import io.grpc.ClientStreamTracer.StreamInfo
import kotlinx.coroutines.launch

class ProjectDetailsViewFragment: ViewModel() {

    var warningAuthor: String = ""
    var warningDescription: String = ""
    var userID: String = ""

    var warningsListenerRegistration: ListenerRegistration? = null
    private val mFireStore = FirebaseFirestore.getInstance()
    private val FirestoreClass = FirestoreClass()
    val createWarningResult: MutableLiveData<Boolean> = MutableLiveData()

    private val _warningsList: MutableLiveData<List<Project>> = MutableLiveData()
    val projectsList: LiveData<List<Project>>
        get() = _warningsList

    fun createWarning() {

        userID = FirestoreClass.getCurrentUserId()

        var warning = Warning(
            warningAuthor,
            warningDescription,
            userID
        )

        mFireStore.collection("warnings")
            .document()
            .set(warning, SetOptions.merge())
            .addOnSuccessListener {
                createWarningResult.postValue(true)
                resetValues()
            }.addOnFailureListener {
                createWarningResult.postValue(false)
            }
    }

    fun resetValues() {
        var warningAuthor: String = ""
        var warningDescription: String = ""
    }

    fun getWarningsList() {

        viewModelScope.launch {
            try {
                warningsSnapshotListener()
            } catch (e: Exception) {
                Log.e("WARNINGS", "ERROR")  // handle error
            }
        }
    }



    fun warningsSnapshotListener() {
        val collectionRef = mFireStore.collection(Constants.PROJECTS)
            .whereArrayContains(
                Constants.ASSIGNED_TO,
                FirestoreClass.getCurrentUserId()
            )
        warningsListenerRegistration = collectionRef.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                Log.e("SNAPSHOT", "Error listening to projects collection", exception)
                return@addSnapshotListener
            }

            val warnings = mutableListOf<Project>()
            for (doc in snapshot?.documents ?: emptyList()) {
                val warning = doc.toObject(Project::class.java)
                if (warning != null) {
                    warnings.add(warning)
                }
            }

            _warningsList.value = warnings
        }
    }
}