package com.example.chit_chat.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.chit_chat.R
import com.example.chit_chat.di.AppComponentHolder
import com.example.chit_chat.di.ViewModelFactory
import com.example.chit_chat.ui.auth.login.LoginViewModel
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class SplashFragment: Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory<SplashViewModel>
    private val viewModel: SplashViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[SplashViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AppComponentHolder.get().inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val isTokenExist = viewModel.checkToken()

        if (isTokenExist) {
            viewModel.updateProfile()
            this@SplashFragment.findNavController()
                .navigate(R.id.action_splashFragment_to_homeFragment)
        } else {
            this@SplashFragment.findNavController()
                .navigate(R.id.action_splashFragment_to_loginFragment)
        }
    }
}