package com.anaandreis.minhapesquisa_trellocloneapp.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.findNavController
import com.anaandreis.minhapesquisa_trellocloneapp.R
import com.anaandreis.minhapesquisa_trellocloneapp.FirestoreClass

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_MinhaPesquisa_TrelloCloneApp)
        setContentView(R.layout.activity_main)
        this.findNavController(R.id.myNavHostFragment)

    }



}