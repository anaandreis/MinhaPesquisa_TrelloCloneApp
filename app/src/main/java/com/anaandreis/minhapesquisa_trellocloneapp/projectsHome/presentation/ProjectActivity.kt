package com.anaandreis.minhapesquisa_trellocloneapp.projectsHome.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.anaandreis.minhapesquisa_trellocloneapp.R

class ProjectActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project)
        this.findNavController(R.id.myNavHostFragment)
    }
}