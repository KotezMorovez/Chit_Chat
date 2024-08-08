package com.example.chit_chat.ui.auth.signup

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
import com.example.chit_chat.common.collectWithLifecycle
import com.example.chit_chat.databinding.FragmentSignupBinding
import com.example.chit_chat.di.AppComponentHolder
import com.example.chit_chat.di.ViewModelFactory
import com.example.chit_chat.ui.common.BaseFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class SignUpFragment : BaseFragment<FragmentSignupBinding>() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory<SignUpViewModel>
    private val viewModel: SignUpViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[SignUpViewModel::class.java]
    }

    override fun createViewBinding(): FragmentSignupBinding {
        return FragmentSignupBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AppComponentHolder.get().inject(this)
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
//            TODO: values for testing, delete before release
            if (savedInstanceState == null) {
                signUpFirstNameEditText.setText("Test")
                signUpLastNameEditText.setText("Pesp")
                signUpEmailEditText.setText("test@test.rr")
                signUpPasswordEditText.setText("123123Test!")
            } else {
                signUpFirstNameEditText.setText(savedInstanceState?.getString(FIRST_NAME, null))
                signUpLastNameEditText.setText(savedInstanceState?.getString(LAST_NAME, null))
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
                    signUpEmailEditText.text.toString().trim(),
                    signUpPasswordEditText.text.toString().trim()
                )
            }

            signUpLoginLinkTextView.setOnClickListener {
                this@SignUpFragment.findNavController()
                    .navigate(R.id.action_signUpFragment_to_loginFragment)
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
            }

            if (state is SignUpViewModel.Event.NetworkError) {
                val snackBar = Snackbar.make(
                    requireContext(),
                    viewBinding.signUpButton,
                    requireContext().getText(R.string.auth_error_toast),
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
            signUpEmailHintTextView.isVisible = event.isValidEmail
            signUpPasswordHintTextView.isVisible = event.isValidPassword
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
            outState.putString(EMAIL, signUpEmailEditText.text.toString().trim())
            outState.putString(PASSWORD, signUpPasswordEditText.text.toString().trim())
        }
        super.onSaveInstanceState(outState)
    }

    companion object {
        private const val FIRST_NAME = "first name"
        private const val LAST_NAME = "last name"
        private const val EMAIL = "email"
        private const val PASSWORD = "password"
    }
}