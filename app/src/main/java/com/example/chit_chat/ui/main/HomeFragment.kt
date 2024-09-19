package com.example.chit_chat.ui.main

import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.chit_chat.databinding.FragmentHomeBinding
import com.example.chit_chat.ui.common.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    override fun createViewBinding(): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun initUi() {
        val navController = viewBinding.homeNavHost.getFragment<NavHostFragment>().navController
        viewBinding.homeBottomNavigationView.setupWithNavController(navController)
    }

    override fun observeData() {}
}