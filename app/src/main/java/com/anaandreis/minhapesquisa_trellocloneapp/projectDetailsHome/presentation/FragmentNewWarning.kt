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
    import com.anaandreis.minhapesquisa_trellocloneapp.databinding.FragmentNewWarningBinding
    import com.anaandreis.minhapesquisa_trellocloneapp.newProject.NewProjectViewModel
    import com.anaandreis.minhapesquisa_trellocloneapp.projectDetailsHome.domain.ProjectDetailsViewModel
    import com.anaandreis.minhapesquisa_trellocloneapp.utils.DialogFunctions
    import com.google.android.material.bottomsheet.BottomSheetDialogFragment
    import com.google.android.material.datepicker.MaterialDatePicker.Builder.dateRangePicker

    class FragmentNewWarning : BottomSheetDialogFragment() {

        lateinit var binding: FragmentNewWarningBinding
        private val sharedViewModel: ProjectDetailsViewModel by activityViewModels()
        private val dialogFunctions = DialogFunctions()

        private var checkingProjectID = false

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_new_warning, container, false)

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
                newWarningFragment = this@FragmentNewWarning
            }

            binding.salvarAvisoButton.setOnClickListener {
                dialogFunctions.showProgressDialog(requireContext(), "Criando aviso...")
                getWarningandAuthor()
                if (!validateWarningandAuthor(sharedViewModel.warning, sharedViewModel.warningAuthor) &&
                    !checkingProjectID
                ) {
                    dialogFunctions.hideProgressDialog(requireContext())
                } else { sharedViewModel.createWarning()

                //    sharedViewModel.currentProjectId.observe(viewLifecycleOwner) { projectId ->
                 //       if (!projectId.isEmpty()) {
                  //          checkingProjectID = true
                  //      } else {
                   //         Log.e("ProjectId", "EMPTY")
                   //     }
                    }




                sharedViewModel.createWarningResult.observe(viewLifecycleOwner) { success ->
                    if (success) {
                        Toast.makeText(activity, "Aviso criado com sucesso", Toast.LENGTH_SHORT).show()
                        projectCreatedSuccessfully() //show toast to the user
                        resetAllTexts()
                        dismiss()
                    } else {
                        dialogFunctions.hideProgressDialog(requireContext())
                    }
                }
            }

        }


        fun projectCreatedSuccessfully() {
            dialogFunctions.hideProgressDialog(requireContext())
        }

        fun resetAllTexts(){
            binding.preencherAvisoEt.setText("")
            binding.autorAvisoEt.setText("")
        }

        fun getWarningandAuthor(){
            sharedViewModel.warning = binding.preencherAvisoEt.text.toString().trim { it <= ' ' }
            sharedViewModel.warningAuthor = binding.autorAvisoEt.text.toString().trim { it <= ' ' }

        }

        fun validateWarningandAuthor(warning: String, author: String): Boolean{
            return when {
                TextUtils.isEmpty(warning) -> {
                    dialogFunctions.showErrorSnackBar(binding.root, "Digite aviso")
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