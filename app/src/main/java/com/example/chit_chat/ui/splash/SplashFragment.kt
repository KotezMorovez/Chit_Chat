package com.example.chit_chat.ui.splash

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.chit_chat.R
import com.example.chit_chat.utils.collectWithLifecycle
import com.example.chit_chat.databinding.FragmentSplashBinding
import com.example.chit_chat.di.AppComponentHolder
import com.example.chit_chat.di.ViewModelFactory
import com.example.chit_chat.ui.common.BaseFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class SplashFragment : BaseFragment<FragmentSplashBinding>() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory<SplashViewModel>
    private val viewModel: SplashViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[SplashViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AppComponentHolder.get().inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun createViewBinding(): FragmentSplashBinding {
        return FragmentSplashBinding.inflate(layoutInflater)
    }

    override fun initUi() {
        viewModel.checkTokenExist()
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            viewBinding.loaderView.startLoader()
        }
    }

    private fun applyEvent(event: SplashViewModel.Event?) {
        if (event != null) {
            viewBinding.loaderView.stopLoader()
            when (event) {
                SplashViewModel.Event.SUCCESS -> {
                    this@SplashFragment.findNavController()
                        .navigate(R.id.action_splashFragment_to_homeFragment)
                }

                SplashViewModel.Event.FAILURE -> {
                    this@SplashFragment.findNavController()
                        .navigate(R.id.action_splashFragment_to_loginFragment)
                }
            }
        }
    }

    override fun observeData() {
        viewModel.event.collectWithLifecycle(
            viewLifecycleOwner,
        ) {
            applyEvent(it)
        }
    }
}