package com.anaandreis.minhapesquisa_trellocloneapp.userHome

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.navigation.findNavController
import com.anaandreis.minhapesquisa_trellocloneapp.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_MinhaPesquisa_TrelloCloneApp)
        setContentView(R.layout.activity_main)
        this.findNavController(R.id.myNavHostFragment)
    }
}