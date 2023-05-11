package com.anaandreis.minhapesquisa_trellocloneapp.projectDetailsHome.presentation

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.anaandreis.minhapesquisa_trellocloneapp.R
import com.anaandreis.minhapesquisa_trellocloneapp.databinding.FragmentNewSampleBinding
import com.anaandreis.minhapesquisa_trellocloneapp.databinding.FragmentNewTaskBinding
import com.anaandreis.minhapesquisa_trellocloneapp.projectDetailsHome.domain.ProjectDetailsViewModel
import com.anaandreis.minhapesquisa_trellocloneapp.utils.DialogFunctions
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.datepicker.MaterialDatePicker.Builder.dateRangePicker
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class FragmentNewTask : BottomSheetDialogFragment() {

    lateinit var binding: FragmentNewTaskBinding
    private val sharedViewModel: ProjectDetailsViewModel by activityViewModels()
    private val dialogFunctions = DialogFunctions()




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_new_task, container, false
        )
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
            fragmentNewTask = this@FragmentNewTask
        }



        binding.selecionarPrazoButton.setOnClickListener { getTask() }

        binding.criarTarefaButton.setOnClickListener {
            dialogFunctions.showProgressDialog(requireContext(), "Criando tarefa...")
            creatingBoardCheck()
            if (!creatingBoardCheck())
            {
                dialogFunctions.hideProgressDialog(requireContext())
            } else {
                sharedViewModel.createTask()
            }
        }

        sharedViewModel.createTaskResult.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(activity, "Tarefa criada com sucesso", Toast.LENGTH_SHORT).show()
                projectCreatedSuccessfully() //show toast to the user
                resetAllTexts()
                dismiss()
            } else {
                dialogFunctions.hideProgressDialog(requireContext())
            }
        }
    }


    fun datePicker(){

        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Selecione uma data")
                .build()

        // Set the desired locale
        datePicker.addOnPositiveButtonClickListener { selection ->
            sharedViewModel.taskDate = Date(selection)
            Log.d("Date", "${sharedViewModel.taskDate}")

        }

        datePicker.show(requireActivity().supportFragmentManager, "date_picker")

    }
    fun projectCreatedSuccessfully() {
        dialogFunctions.hideProgressDialog(requireContext())
    }

    fun resetAllTexts(){
        binding.preencherTarefaEt.setText("")
        binding.responsavelTarefaEt.setText("")
    }

    fun getTask(){
        sharedViewModel.taskDescription = binding.preencherTarefaEt.text.toString().trim { it <= ' ' }
        sharedViewModel.taskResponsible = binding.responsavelTarefaEt.text.toString().trim { it <= ' ' }


        if(validateTask(sharedViewModel.taskDescription, sharedViewModel.taskResponsible))
        { datePicker() }
    }

    fun validateTask(task: String, taskResponsible: String): Boolean{
        return when {
            TextUtils.isEmpty(task) -> {
                dialogFunctions.showErrorSnackBar(binding.root, "Descreva a tarefa")
                false
            }
            TextUtils.isEmpty(taskResponsible) -> {
                dialogFunctions.showErrorSnackBar(binding.root, "Escolha um responsável")
                false
            }
            else -> {
                return true
            }
        }

    }

    fun creatingBoardCheck(): Boolean {
        return when {
            sharedViewModel.taskDate != null
            -> {
                true}
            else -> {
                Toast.makeText(activity, "Selecione as datas", Toast.LENGTH_SHORT).show()
                false}

        }
    }

}