package com.anaandreis.minhapesquisa_trellocloneapp.newProject


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anaandreis.minhapesquisa_trellocloneapp.FirestoreClass
import com.anaandreis.minhapesquisa_trellocloneapp.home.MainActivity
import com.anaandreis.minhapesquisa_trellocloneapp.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.gson.Gson
import kotlinx.coroutines.launch
import java.util.Date

    class NewProjectViewModel: ViewModel() {

        var projectName: String = ""
        var projectDescription: String = ""
        var startDate: Date? = null
        var endDate: Date? = null
        var createdBy: String = ""
        var assignedTo: ArrayList<String> = ArrayList()
        private val _projectsList: MutableLiveData<List<Project>> = MutableLiveData()
        val projectsList: LiveData<List<Project>>
            get() = _projectsList

        private val mFireStore = FirebaseFirestore.getInstance()
        val createBoardResult: MutableLiveData<Boolean> = MutableLiveData()
        private val FirestoreClass = FirestoreClass()

        fun createBoard() {
            assignedTo.clear()
            assignedTo.add(FirestoreClass.getCurrentUserId())


            var projectInfo = Project(
                projectName,
                projectDescription,
                startDate!!,
                endDate!!,
                createdBy,
                assignedTo
            )


            mFireStore.collection(Constants.PROJECTS)
                .document()
                .set(projectInfo, SetOptions.merge())
                .addOnSuccessListener {
                    createBoardResult.postValue(true)
                    Log.d("MapSize", "Size of assignedTo map: ${assignedTo.size}")
                    resetValues()
                }.addOnFailureListener {
                    createBoardResult.postValue(false)
                }


        }

        fun resetValues() {
            projectName = ""
            projectDescription = ""
            startDate = null
            endDate = null
            createdBy = ""
            assignedTo = ArrayList()
        }

        fun addEmailToAssignedList(email: String) {
            assignedTo.add(email)
            Log.d("MapSize", "Size of assignedTo map: ${assignedTo.size}")
        }

        fun addProjectToCurrentUser(projectId: String) {
            val currentUser = FirebaseAuth.getInstance().currentUser
            if (currentUser == null) {
                // User is not logged in
                return
            }

            val userRef =
                FirebaseFirestore.getInstance().collection("users").document(currentUser.uid)

            userRef.update("projects", FieldValue.arrayUnion(projectId))
                .addOnSuccessListener {
                    Log.d("", "Project added to user: $projectId")
                }
                .addOnFailureListener { e ->
                    Log.e("", "Error adding project to user: $projectId", e)
                }
        }


        fun getBoardsList() {

            viewModelScope.launch {
                try {
                    mFireStore.collection(Constants.PROJECTS)
                        .whereArrayContains(
                            Constants.ASSIGNED_TO,
                            FirestoreClass.getCurrentUserId()
                        )
                        .get()
                        .addOnSuccessListener { document ->
                            Log.e("GetBoardList", document.documents.toString())
                            val projectsList = arrayListOf<Project>()
                            for (i in  document.documents){
                                val project = i.toObject(Project::class.java)
                                if (project != null) {
                                    projectsList.add(project)
                                }

                            }
                            Log.e("QUANTOS PROJETOS", "{$projectsList.size}")
                            _projectsList.value = projectsList
                        }
                } catch (e: Exception) {
                    Log.e("GETBOARD", "ERROR")  // handle error
                }
            }
        }
    }

