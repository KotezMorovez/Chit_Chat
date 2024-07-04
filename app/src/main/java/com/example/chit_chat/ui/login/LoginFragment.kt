package com.example.chit_chat.ui.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.chit_chat.R
import com.example.chit_chat.databinding.FragmentLoginBinding
import com.example.chit_chat.di.AppComponentHolder
import com.example.chit_chat.di.ViewModelFactory
import com.example.chit_chat.ui.common.BaseFragment
import com.example.chit_chat.ui.main.MainActivity
import javax.inject.Inject

class LoginFragment : BaseFragment<FragmentLoginBinding>() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory<LoginViewModel>
    private val viewModel: LoginViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[LoginViewModel::class.java]
    }

    override fun createViewBinding(): FragmentLoginBinding {
        return FragmentLoginBinding.inflate(layoutInflater)
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
        val view = super.onCreateView(inflater, container, savedInstanceState)

        with(viewBinding) {
            loginEmailEditText.setText(savedInstanceState?.getString(EMAIL, null))
            loginPasswordEditText.setText(savedInstanceState?.getString(PASSWORD, null))
        }

        return view
    }

    override fun initUi() {
        with(viewBinding) {
            loginButton.setOnClickListener {
                viewModel.login(
                    loginEmailEditText.text.toString(),
                    loginPasswordEditText.text.toString()
                )
            }

            loginForgetPasswordLink.setOnClickListener {
//                this@LoginFragment.findNavController()
//                    .navigate(R.id.action_loginFragment_to_forgetPasswordFragment)
            }

            loginSignUpLink.setOnClickListener {
//                this@LoginFragment.findNavController()
//                    .navigate(R.id.action_loginFragment_to_signUpFragment)
            }
        }
    }

    override fun observeData() {

    }

    override fun onSaveInstanceState(outState: Bundle) {
        with(viewBinding) {
            outState.putString(EMAIL, loginEmailEditText.text.toString().trim())
            outState.putString(PASSWORD, loginPasswordEditText.text.toString().trim())
        }
        super.onSaveInstanceState(outState)
    }

    companion object {
        private const val EMAIL = "email"
        private const val PASSWORD = "password"
    }
}