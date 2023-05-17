package com.anaandreis.minhapesquisa_trellocloneapp.newProject

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.anaandreis.minhapesquisa_trellocloneapp.FirestoreClass
import com.anaandreis.minhapesquisa_trellocloneapp.R
import com.anaandreis.minhapesquisa_trellocloneapp.databinding.FragmentNewProjectBinding
import com.anaandreis.minhapesquisa_trellocloneapp.utils.DialogFunctions
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import java.util.Date


class NewProjectFragment : BottomSheetDialogFragment() {

    lateinit var binding: FragmentNewProjectBinding
    private val dialogFunctions = DialogFunctions()
    private val sharedViewModel: NewProjectViewModel by activityViewModels()
    private val FirestoreClass = FirestoreClass()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_new_project, container, false)

    return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

            binding.apply {
                //         // Specify the fragment as the lifecycle owner

                lifecycleOwner = viewLifecycleOwner

                // Assign the view model to a property in the binding class
                newProjectViewModel = sharedViewModel

                // Assign the fragment
                newProjectFragment = this@NewProjectFragment

            }

        binding.selecionarDatasButton.setOnClickListener{ getNameandDescription()  }

        binding.selecionarMembrosButton.setOnClickListener { showCreateAddMembers() }

        //binding.selecionarCapa.setOnClickListener{ showCreateSelectImage()}


        binding.criarProjetoButton.setOnClickListener {
            dialogFunctions.showProgressDialog(requireContext(), "Criando projeto...")
            creatingBoardCheck()
            if (!creatingBoardCheck()){
                dialogFunctions.hideProgressDialog(requireContext())
            } else { sharedViewModel.createBoard() }
        }

        sharedViewModel.createdBy = FirestoreClass.getCurrentUserEmail()

        sharedViewModel.createBoardResult.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(activity, "Projeto criado com sucesso", Toast.LENGTH_SHORT).show()
                projectCreatedSuccessfully() //show toast to the user
                resetAllTexts()
                dismiss()
                sharedViewModel.createBoardResult.value = false
            } else {
                dialogFunctions.hideProgressDialog(requireContext())
            }
        }
    }

    fun resetAllTexts(){
        binding.preencherNomeEt.setText("")
        binding.descricaoProjetoEt.setText("")
    }

    fun projectCreatedSuccessfully() {
        dialogFunctions.hideProgressDialog(requireContext())
    }

    fun showCreateAddMembers(){
    val modalBottomSheet = FragmentAddMembers()
    modalBottomSheet.show(parentFragmentManager, "ADD MEMBERS")
}

  //  fun showCreateSelectImage(){
  //      val modalBottomSheet = FragmentSelectImage()
  //      modalBottomSheet.show(parentFragmentManager, "SELECIONAR CAPA")
  //  }

    fun dateRangePicker(): Unit {
        val dateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText("Select dates")
            .setSelection(
                androidx.core.util.Pair(
                    MaterialDatePicker.thisMonthInUtcMilliseconds(),
                    MaterialDatePicker.todayInUtcMilliseconds()
                )
            )
            .build()

        dateRangePicker.addOnPositiveButtonClickListener { dateSelection ->
            sharedViewModel.startDate = Date(dateSelection.first ?: 0)
            sharedViewModel.endDate = Date(dateSelection.second ?: 0)
        }

        dateRangePicker.show(requireActivity().supportFragmentManager, "date_range_picker")
    }


    fun getNameandDescription(){
        sharedViewModel.projectName = binding.preencherNomeEt.text.toString().trim { it <= ' ' }
        sharedViewModel.projectDescription = binding.descricaoProjetoEt.text.toString().trim { it <= ' ' }

        if(validateNameandDescription(sharedViewModel.projectName, sharedViewModel.projectDescription))
        { dateRangePicker() }
    }

    fun validateNameandDescription(projectName: String, projectDescription: String): Boolean{
        return when {
            TextUtils.isEmpty(projectName) -> {
                dialogFunctions.showErrorSnackBar(binding.root, "Digite nome do projeto")
                false
            }
            TextUtils.isEmpty(projectDescription) -> {
                dialogFunctions.showErrorSnackBar(binding.root, "Digite seu email")
                false
            }
            else -> {
                return true
            }
        }

    }


    fun creatingBoardCheck(): Boolean {
        return when {
            sharedViewModel.startDate != null
                    && sharedViewModel.endDate != null
                    && sharedViewModel.assignedTo.value != null -> {

                true }
            sharedViewModel.assignedTo.value.isNullOrEmpty() -> {
                dialogFunctions.showErrorSnackBar(binding.root, "Selecione pelo menos um usuário")
                false}
            else -> {
                dialogFunctions.showErrorSnackBar(binding.root, "Selecione o prazo")
                false}

        }
    }




    companion object {
        const val TAG = "ModalBottomSheet"
    }
}