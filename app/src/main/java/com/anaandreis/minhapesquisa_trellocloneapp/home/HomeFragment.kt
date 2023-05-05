package com.anaandreis.minhapesquisa_trellocloneapp.signUp.presentation

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.anaandreis.minhapesquisa_trellocloneapp.FirestoreClass
import com.anaandreis.minhapesquisa_trellocloneapp.R
import com.anaandreis.minhapesquisa_trellocloneapp.databinding.FragmentHomeBinding
import com.anaandreis.minhapesquisa_trellocloneapp.projectsHome.presentation.ProjectActivity


class HomeFragment : Fragment() {

    private val firestoreClass = FirestoreClass()
    lateinit var binding: FragmentHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_home, container, false
        )
        binding.entrarButton.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
        }

        binding.cadastrarButton.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_homeFragment_to_signUpFragment)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        IsUserLoggedAlready()
    }

    fun IsUserLoggedAlready(){
        var currentID = firestoreClass.getCurrentUserId()

        if(currentID.isNotEmpty()){
            startActivity(Intent(requireContext(), ProjectActivity::class.java))
        }
    }
}



