package com.example.chit_chat.ui.auth.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.chit_chat.R
import com.example.chit_chat.databinding.FragmentLoginBinding
import com.example.chit_chat.di.AppComponentHolder
import com.example.chit_chat.di.ViewModelFactory
import com.example.chit_chat.ui.common.BaseFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
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
        requireActivity().window!!.setBackgroundDrawableResource(R.drawable.auth_background)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        with(viewBinding) {
//            TODO: values for testing
            if (savedInstanceState == null) {
                loginEmailEditText.setText("test@test.rr")
                loginPasswordEditText.setText("123123Test!")
            } else {
                loginEmailEditText.setText(savedInstanceState?.getString(EMAIL, null))
                loginPasswordEditText.setText(savedInstanceState?.getString(PASSWORD, null))
            }
        }

        return view
    }

    override fun initUi() {
        with(viewBinding) {
            val imm = getSystemService(requireContext(), InputMethodManager::class.java)

            loginEmailEditText.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    loginPasswordEditText.requestFocus()
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }

            loginPasswordEditText.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    imm?.hideSoftInputFromWindow(loginPasswordEditText.windowToken, 0)
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }


            loginButton.setOnClickListener {
                loginButton.isEnabled = false

                viewModel.login(
                    loginEmailEditText.text.toString().trim(),
                    loginPasswordEditText.text.toString().trim()
                )
            }

//            loginForgetPasswordLinkTextView.setOnClickListener {
//                this@LoginFragment.findNavController()
//                    .navigate(R.id.action_loginFragment_to_forgetPasswordFragment)
//            }

            loginSignUpLinkTextView.setOnClickListener {
                this@LoginFragment.findNavController()
                    .navigate(R.id.action_loginFragment_to_signUpFragment)
            }
        }
    }

    private fun applyEvent(event: LoginViewModel.Event) {
        with(viewBinding) {
            loginEmailHintTextView.isGone = true
            loginPasswordHintTextView.isGone = true
            loginButton.isEnabled = true

            if (event is LoginViewModel.Event.Success) {
//                  this@LoginFragment.findNavController()
//                      .navigate(R.id.action_loginFragment_to_homeFragment)
            }

            if (event is LoginViewModel.Event.NetworkError) {
                val snackBar = Snackbar.make(
                    requireContext(),
                    viewBinding.loginButton,
                    requireContext().getText(R.string.auth_error_toast),
                    Snackbar.LENGTH_SHORT
                )
                snackBar.show()
            }
        }
    }

    private fun applyState(state: LoginViewModel.State) {
        with(viewBinding) {
            loginEmailHintTextView.isVisible = state.isValidEmail
            loginPasswordHintTextView.isVisible = state.isValidPassword
        }
    }

    override fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                viewModel.event.collect {
                    applyEvent(it)
                }
            }
            launch {
                viewModel.state.collect {
                    applyState(it)
                }
            }
        }
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