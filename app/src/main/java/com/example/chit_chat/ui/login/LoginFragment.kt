package com.example.chit_chat.ui.login

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.chit_chat.R
import com.example.chit_chat.common.ACCESS_TOKEN
import com.example.chit_chat.common.PREFERENCES
import com.example.chit_chat.common.SharedPrefsService
import com.example.chit_chat.common.SharedPrefsServiceImpl
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
    private var state: LoginViewModel.State = LoginViewModel.State.DEFAULT
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
//            TODO: values for testing
            if (savedInstanceState == null) {
                loginEmailEditText.setText("test@test.rr")
                loginPasswordEditText.setText("123123Test!")
            } else {
                loginEmailEditText.setText(savedInstanceState?.getString(EMAIL, null))
                loginPasswordEditText.setText(savedInstanceState?.getString(PASSWORD, null))
                state = LoginViewModel.State.fromIndex(savedInstanceState.getInt(STATE))
            }
        }

        return view
    }

    override fun initUi() {
        with(viewBinding) {
            val imm = getSystemService(requireContext(), InputMethodManager::class.java)

            loginEmailEditText.setOnEditorActionListener{ _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    loginEmailEditText.clearFocus()
                    loginPasswordEditText.isFocusable = true
                    loginPasswordEditText.requestFocus()
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }

            loginPasswordEditText.setOnEditorActionListener{ _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    imm?.hideSoftInputFromWindow(loginPasswordEditText.windowToken, 0)
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }

            loginButton.setOnClickListener {
                viewModel.login(
                    loginEmailEditText.text.toString(),
                    loginPasswordEditText.text.toString()
                )
                loginButton.isEnabled = false
            }

//            loginForgetPasswordLinkTextView.setOnClickListener {
//                this@LoginFragment.findNavController()
//                    .navigate(R.id.action_loginFragment_to_forgetPasswordFragment)
//            }

            loginSignUpLinkTextView.setOnClickListener {
//                this@LoginFragment.findNavController()
//                    .navigate(R.id.action_loginFragment_to_signUpFragment)
            }
        }
    }

    private fun applyState(state: LoginViewModel.State) {
        with(viewBinding) {
            loginEmailHintTextView.isGone = true
            loginPasswordHintTextView.isGone = true

            when (state) {
                LoginViewModel.State.EMAIL_ERROR -> {
                    loginEmailHintTextView.isVisible = true
                }

                LoginViewModel.State.PASSWORD_ERROR -> {
                    loginPasswordHintTextView.isVisible = true
                }

                LoginViewModel.State.INTERNET_ERROR -> {
                    val snackBar = Snackbar.make(
                        requireContext(),
                        viewBinding.loginButton,
                        "Что-то пошло не так",
                        Snackbar.LENGTH_SHORT
                    )
                    snackBar.show()
                    loginButton.isEnabled = true
                }

                LoginViewModel.State.NO_ERROR -> {
                    val sharedPreferences =
                        requireContext().getSharedPreferences(PREFERENCES, MODE_PRIVATE)
                    val snackBar = Snackbar.make(
                        requireContext(),
                        viewBinding.loginButton,
                        "${sharedPreferences.getString(ACCESS_TOKEN, "")}",
                        Snackbar.LENGTH_SHORT
                    )
                    snackBar.show()

//                  this@LoginFragment.findNavController()
//                      .navigate(R.id.action_loginFragment_to_homeFragment)
                }

                else -> {}
            }
        }
    }

    override fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                viewModel.event.collect {
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
        outState.putInt(STATE, state.ordinal)
    }

    companion object {
        private const val EMAIL = "email"
        private const val PASSWORD = "password"
        private const val STATE = "state"
    }
}