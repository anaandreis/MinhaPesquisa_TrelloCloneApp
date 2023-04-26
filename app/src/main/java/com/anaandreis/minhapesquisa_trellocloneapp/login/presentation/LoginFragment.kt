package com.anaandreis.minhapesquisa_trellocloneapp.login.presentation

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

import com.anaandreis.minhapesquisa_trellocloneapp.utils.DialogFunctions
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    lateinit var binding: FragmentLoginBinding
    private val dialogFunctions = DialogFunctions()

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

        binding.backButton.setOnClickListener{ view: View ->
        view.findNavController().navigate(R.id.action_loginFragment_to_homeFragment)}

        binding.loginButton.setOnClickListener {signIn()}
        return binding.root
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

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("LOGIN", "signInWithEmail:failure", task.exception)
                        Toast.makeText(requireContext(), "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    fun validateForm(email: String, password: String): Boolean {
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


}