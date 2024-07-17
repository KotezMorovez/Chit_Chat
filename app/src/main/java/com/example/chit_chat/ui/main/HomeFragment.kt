package com.example.chit_chat.ui.main

import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.example.chit_chat.databinding.FragmentHomeBinding
import com.example.chit_chat.di.AppComponentHolder
import com.example.chit_chat.ui.common.BaseFragment


class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    override fun createViewBinding(): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AppComponentHolder.get().inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun initUi() {
        val navController = viewBinding.homeNavHost.getFragment<NavHostFragment>().navController
        viewBinding.homeBottomNavigationView.setupWithNavController(navController)

        viewBinding.homeBottomNavigationView.setOnItemSelectedListener {
            return@setOnItemSelectedListener onNavDestinationSelected(it, navController)
        }
    }

    override fun observeData() {}
}