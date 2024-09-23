package com.example.chit_chat.ui.features.signup.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.chit_chat.R
import com.example.chit_chat.utils.collectWithLifecycle
import com.example.chit_chat.databinding.FragmentSignupBinding
import com.example.chit_chat.ui.common.BaseFragment
import com.example.chit_chat.ui.features.signup.view_model.SignUpViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpFragment : BaseFragment<FragmentSignupBinding>() {
    private val viewModel by viewModels<SignUpViewModel>()

    override fun createViewBinding(): FragmentSignupBinding {
        return FragmentSignupBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        requireActivity().window!!.setBackgroundDrawableResource(R.drawable.bg_auth)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        with(viewBinding) {
            if (savedInstanceState == null) {
                signUpFirstNameEditText.setText("Test")
                signUpLastNameEditText.setText("Pesp")
                signUpUserNameEditText.setText("test1234")
                signUpEmailEditText.setText("test@test.rr")
                signUpPasswordEditText.setText("123123Test!")
            } else {
                signUpFirstNameEditText.setText(savedInstanceState?.getString(FIRST_NAME, null))
                signUpLastNameEditText.setText(savedInstanceState?.getString(LAST_NAME, null))
                signUpUserNameEditText.setText(savedInstanceState?.getString(USER_NAME, null))
                signUpEmailEditText.setText(savedInstanceState?.getString(EMAIL, null))
                signUpPasswordEditText.setText(savedInstanceState?.getString(PASSWORD, null))
            }
        }

        return view
    }

    override fun initUi() {
        with(viewBinding) {
            val imm = getSystemService(requireContext(), InputMethodManager::class.java)

            signUpFirstNameEditText.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    signUpLastNameEditText.requestFocus()
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }

            signUpLastNameEditText.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    signUpEmailEditText.requestFocus()
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }

            signUpEmailEditText.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    signUpPasswordEditText.requestFocus()
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }

            signUpPasswordEditText.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    imm?.hideSoftInputFromWindow(signUpPasswordEditText.windowToken, 0)
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }

            signUpButton.setOnClickListener {
                signUpButton.isEnabled = false
                signUpButtonTextView.isGone = true
                loaderView.isVisible = true

                viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                    viewBinding.loaderView.startLoader()
                }

                viewModel.signUp(
                    signUpFirstNameEditText.text.toString().trim(),
                    signUpLastNameEditText.text.toString().trim(),
                    signUpUserNameEditText.text.toString().trim(),
                    signUpEmailEditText.text.toString().trim(),
                    signUpPasswordEditText.text.toString().trim()
                )
            }

            signUpLoginLinkTextView.setOnClickListener {
                val navController = this@SignUpFragment.findNavController()
                navController.popBackStack()
            }
        }
    }

    private fun applyEvent(state: SignUpViewModel.Event) {
        with(viewBinding) {
            loaderView.isGone = true
            loaderView.stopLoader()
            signUpFirstNameHintTextView.isGone = true
            signUpLastNameHintTextView.isGone = true
            signUpEmailHintTextView.isGone = true
            signUpPasswordHintTextView.isGone = true
            signUpButton.isEnabled = true
            signUpButtonTextView.isVisible = true


            if (state is SignUpViewModel.Event.Success) {
                this@SignUpFragment.findNavController()
                    .navigate(R.id.action_signUpFragment_to_homeFragment)
            } else {
                val snackBar = Snackbar.make(
                    requireContext(),
                    viewBinding.signUpButton,
                    requireContext().getText(
                        if (state is SignUpViewModel.Event.NetworkError)
                            R.string.auth_network_error_toast
                        else
                            R.string.auth_error_toast
                    ),
                    Snackbar.LENGTH_SHORT
                )
                snackBar.show()
            }
        }
    }

    private fun applyState(event: SignUpViewModel.State) {
        with(viewBinding) {
            loaderView.isGone = true
            loaderView.stopLoader()
            signUpButtonTextView.isVisible = true
            signUpFirstNameHintTextView.isVisible = event.isValidFirstName
            signUpLastNameHintTextView.isVisible = event.isValidLastName
            signUpPasswordHintTextView.isVisible = event.isValidPassword

            if (event.isValidEmail == null) {
                signUpEmailHintTextView.isGone = true
            } else {
                signUpEmailHintTextView.setText(event.isValidEmail)
                signUpEmailHintTextView.isVisible = true
            }

            if (event.isValidUserName == null) {
                signUpUserNameHintTextView.isGone = true
            } else {
                signUpUserNameHintTextView.setText(event.isValidUserName)
                signUpUserNameHintTextView.isVisible = true
            }

            signUpButton.isEnabled = true
        }
    }

    override fun observeData() {
        viewModel.event.collectWithLifecycle(
            viewLifecycleOwner
        ) {
            applyEvent(it)
        }

        viewModel.state.collectWithLifecycle(
            viewLifecycleOwner
        ) {
            applyState(it)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        with(viewBinding) {
            outState.putString(FIRST_NAME, signUpFirstNameEditText.text.toString().trim())
            outState.putString(LAST_NAME, signUpLastNameEditText.text.toString().trim())
            outState.putString(USER_NAME, signUpUserNameEditText.text.toString().trim())
            outState.putString(EMAIL, signUpEmailEditText.text.toString().trim())
            outState.putString(PASSWORD, signUpPasswordEditText.text.toString().trim())
        }
        super.onSaveInstanceState(outState)
    }

    companion object {
        private const val FIRST_NAME = "first name"
        private const val LAST_NAME = "last name"
        private const val USER_NAME = "user_name"
        private const val EMAIL = "email"
        private const val PASSWORD = "password"
    }
}