package com.anaandreis.minhapesquisa_trellocloneapp.newProject

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.anaandreis.minhapesquisa_trellocloneapp.R
import com.anaandreis.minhapesquisa_trellocloneapp.databinding.FragmentAddMembersBinding
import com.anaandreis.minhapesquisa_trellocloneapp.utils.DialogFunctions
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker.Builder.dateRangePicker

class FragmentAddMembers : BottomSheetDialogFragment() {


    lateinit var binding: FragmentAddMembersBinding
    private val dialogFunctions = DialogFunctions()
    private val sharedViewModel: NewProjectViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_add_members, container, false)

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
            addMembersFragment = this@FragmentAddMembers

        }

        binding.addMemberButton.setOnClickListener{ addEmailtoList() }

}
    fun addEmailtoList(){
        var newEmail = binding.addemailEditView.text.toString().trim { it <= ' ' }
        if (TextUtils.isEmpty(newEmail)){
            dialogFunctions.showErrorSnackBar(binding.root, "Digite um email")
        } else
        {sharedViewModel.addEmailToAssignedList(newEmail)
            dialogFunctions.showErrorSnackBar(binding.root, "Email adicionado")
            resetEmailEditView()
        }
    }

    fun resetEmailEditView(){
        binding.addemailEditView.setText("")
    }
}