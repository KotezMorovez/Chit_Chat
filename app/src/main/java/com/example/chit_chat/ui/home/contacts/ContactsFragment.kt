package com.example.chit_chat.ui.home.contacts

import androidx.fragment.app.viewModels
import com.example.chit_chat.databinding.FragmentContactsBinding
import com.example.chit_chat.ui.common.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactsFragment : BaseFragment<FragmentContactsBinding>() {
    private val viewModel by viewModels<ContactsViewModel>()

    override fun createViewBinding(): FragmentContactsBinding {
        return FragmentContactsBinding.inflate(layoutInflater)
    }

    override fun initUi() {
    }

    override fun observeData() {

    }
}