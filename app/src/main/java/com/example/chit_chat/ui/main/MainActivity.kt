package com.example.chit_chat.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.chit_chat.R
import com.example.chit_chat.di.AppComponentHolder

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        AppComponentHolder.get().inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}