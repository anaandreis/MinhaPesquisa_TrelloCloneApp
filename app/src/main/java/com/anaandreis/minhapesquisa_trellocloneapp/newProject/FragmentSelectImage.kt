package com.anaandreis.minhapesquisa_trellocloneapp.newProject

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import android.Manifest
import androidx.activity.result.contract.ActivityResultContracts
import com.anaandreis.minhapesquisa_trellocloneapp.R
import com.anaandreis.minhapesquisa_trellocloneapp.databinding.FragmentSelectImageBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class FragmentSelectImage :  BottomSheetDialogFragment() {

    companion object {
        private const val READ_STORAGE_PERMISSION_CODE = 1
        private const val PICK_IMAGE_REQUEST_CODE = 2
    }

    lateinit var binding: FragmentSelectImageBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_select_image, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cameraButton.setOnClickListener {
            val readImagePermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            {Manifest.permission.READ_MEDIA_IMAGES}
            else {Manifest.permission.READ_EXTERNAL_STORAGE}

            if(ContextCompat.checkSelfPermission(requireContext(), readImagePermission) == PackageManager.PERMISSION_GRANTED){
                //permission granted
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    READ_STORAGE_PERMISSION_CODE)
            }
        }
    }


}