package com.anaandreis.minhapesquisa_trellocloneapp.projectDetailsHome.presentation

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.anaandreis.minhapesquisa_trellocloneapp.R

class ProjectDetailsHomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project_details_home)

        // calling the action bar
        var actionBar = getSupportActionBar()

        if (actionBar != null) {

            // Customize the back button
            actionBar.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_24);

            // showing the back button in action bar
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    // this event will enable the back
    // function to the button on press
    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onContextItemSelected(item)
    }
}