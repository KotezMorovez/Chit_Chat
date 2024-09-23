package com.example.chit_chat.ui.features.main.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.chit_chat.R
import com.example.chit_chat.utils.collectWithLifecycle
import com.example.chit_chat.utils.LogoutHandler
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var logoutHandler: LogoutHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        observeNavData()
    }

    private fun logout() {
        this.findNavController(R.id.nav_host).setGraph(R.navigation.nav_graph)
    }

    private fun observeNavData() {
        logoutHandler.event.collectWithLifecycle(this) {
            logout()
        }
    }
}