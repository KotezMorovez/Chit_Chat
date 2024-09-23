package com.example.chit_chat.ui.features.splash.ui

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.chit_chat.R
import com.example.chit_chat.utils.collectWithLifecycle
import com.example.chit_chat.databinding.FragmentSplashBinding
import com.example.chit_chat.ui.common.BaseFragment
import com.example.chit_chat.ui.features.splash.view_model.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding>() {
    private val viewModel by viewModels<SplashViewModel>()

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