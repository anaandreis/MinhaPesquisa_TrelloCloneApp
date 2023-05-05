package com.anaandreis.minhapesquisa_trellocloneapp


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class FirestoreClass {
    private val mFireStore = FirebaseFirestore.getInstance()


    fun getCurrentUserId(): String{
        var currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserId=""
        if (currentUser != null) {
            currentUserId = currentUser.uid
        }
        return currentUserId
    }

    fun getCurrentUserEmail(): String {
        var currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserEmail=""
        if (currentUser != null) {
            currentUserEmail = currentUser.email.toString()
        }
        return currentUserEmail
    }

}