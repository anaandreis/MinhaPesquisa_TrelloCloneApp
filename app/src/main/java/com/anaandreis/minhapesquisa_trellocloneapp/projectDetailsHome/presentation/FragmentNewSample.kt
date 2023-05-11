package com.anaandreis.minhapesquisa_trellocloneapp.projectDetailsHome.presentation

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.anaandreis.minhapesquisa_trellocloneapp.R
import com.anaandreis.minhapesquisa_trellocloneapp.databinding.FragmentNewSampleBinding
import com.anaandreis.minhapesquisa_trellocloneapp.databinding.FragmentNewWarningBinding
import com.anaandreis.minhapesquisa_trellocloneapp.projectDetailsHome.domain.ProjectDetailsViewModel
import com.anaandreis.minhapesquisa_trellocloneapp.utils.DialogFunctions
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FragmentNewSample : BottomSheetDialogFragment() {

    lateinit var binding: FragmentNewSampleBinding
    private val sharedViewModel: ProjectDetailsViewModel by activityViewModels()
    private val dialogFunctions = DialogFunctions()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_new_sample, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            //         // Specify the fragment as the lifecycle owner

            lifecycleOwner = viewLifecycleOwner

            // Assign the view model to a property in the binding class
            newProjectDetailsViewModel = sharedViewModel

            // Assign the fragment
            newSampleFragment = this@FragmentNewSample
        }

        binding.criarAmostraButton.setOnClickListener {
            dialogFunctions.showProgressDialog(requireContext(), "Criando amostra...")
            getSample()
            if (!validateSample(sharedViewModel.sample, sharedViewModel.samplesAuthor)
            ) {
                dialogFunctions.hideProgressDialog(requireContext())
            } else {
                sharedViewModel.createSample()
               // sharedViewModel.currentProjectId.observe(viewLifecycleOwner) { projectId ->
               //     if (!projectId.isEmpty()) {

              //      } else {
               //         Log.e("ProjectId", "EMPTY")
                    }
                }




            sharedViewModel.createSampleResult.observe(viewLifecycleOwner) { success ->
                if (success) {
                    Toast.makeText(activity, "Amostra criado com sucesso", Toast.LENGTH_SHORT).show()
                    projectCreatedSuccessfully() //show toast to the user
                    resetAllTexts()
                    dismiss()
                } else {
                    dialogFunctions.hideProgressDialog(requireContext())
                }
            }
        }




    fun projectCreatedSuccessfully() {
        dialogFunctions.hideProgressDialog(requireContext())
    }

    fun resetAllTexts(){
        binding.preencherAmostraEt.setText("")
        binding.responsavelTarefaEt.setText("")
    }

    fun getSample(){
        sharedViewModel.sample = binding.preencherAmostraEt.text.toString().trim { it <= ' ' }
        sharedViewModel.samplesAuthor = binding.responsavelTarefaEt.text.toString().trim { it <= ' ' }

    }

    fun validateSample(sample: String, author: String): Boolean{
        return when {
            TextUtils.isEmpty(sample) -> {
                dialogFunctions.showErrorSnackBar(binding.root, "Digite a amostra")
                false
            }
            TextUtils.isEmpty(author) -> {
                dialogFunctions.showErrorSnackBar(binding.root, "Digite seu nome")
                false
            }
            else -> {
                return true
            }
        }

    }


}