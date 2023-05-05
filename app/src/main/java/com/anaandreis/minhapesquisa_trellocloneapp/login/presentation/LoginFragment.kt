package com.anaandreis.minhapesquisa_trellocloneapp.login.presentation

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController

import com.anaandreis.minhapesquisa_trellocloneapp.R
import com.anaandreis.minhapesquisa_trellocloneapp.databinding.FragmentLoginBinding
import com.anaandreis.minhapesquisa_trellocloneapp.projectsHome.presentation.ProjectActivity
import com.anaandreis.minhapesquisa_trellocloneapp.signUp.data.models.User

import com.anaandreis.minhapesquisa_trellocloneapp.utils.DialogFunctions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.anaandreis.minhapesquisa_trellocloneapp.FirestoreClass

class LoginFragment : Fragment() {


    private lateinit var auth: FirebaseAuth
    lateinit var binding: FragmentLoginBinding
    private val dialogFunctions = DialogFunctions()
    private val firestoreClass = FirestoreClass()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
            binding = DataBindingUtil.inflate<FragmentLoginBinding>(
            inflater,
            R.layout.fragment_login, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backButton.setOnClickListener{ view: View ->
            view.findNavController().navigate(R.id.action_loginFragment_to_homeFragment)}

        binding.loginButton.setOnClickListener {signIn()}
    }

    private fun signIn() {
        val email = binding.editTextTextEmailAddress.text.toString().trim { it <= ' ' }
        val password = binding.password.text.toString().trim { it <= ' ' }

        if(validateForm(email, password)){
            dialogFunctions.showProgressDialog(requireContext(),"Aguarde um momento")

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    dialogFunctions.hideProgressDialog(requireContext())
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("LOGIN", "signInWithEmail:success")
                        val user = auth.currentUser
                        signedInUser()

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("LOGIN", "signInWithEmail:failure", task.exception)
                        Toast.makeText(requireContext(), "Authentication failed.",
                            Toast.LENGTH_SHORT).show()

                    }
                }
        }
    }

    private fun validateForm(email: String, password: String): Boolean {
        return when {
            TextUtils.isEmpty(email) -> {
                dialogFunctions.showErrorSnackBar(binding.root, "Digite seu email")
                false
            }
            TextUtils.isEmpty(password) -> {
                dialogFunctions.showErrorSnackBar(binding.root, "Digite sua senha")
                false
            }
            else -> {
                return true
            }
        }
    }

    fun signedInUser(){
        val database = FirebaseFirestore.getInstance()
        database.collection("users")
            .document(firestoreClass.getCurrentUserId())
            .get()
            .addOnSuccessListener { document ->
                val loggedInUser = document.toObject(User::class.java)
                if(loggedInUser != null) {
                    signInSuccess(loggedInUser) }
            }.addOnFailureListener{
                    e-> Log.e("firestore", "Error getting document", e)
            }    }
    fun signInSuccess(user: User){
        startActivity(Intent(requireContext(), ProjectActivity::class.java))

    }
}