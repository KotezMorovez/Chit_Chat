package com.example.chit_chat.ui.home.contacts

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.chit_chat.databinding.FragmentChatListBinding
import com.example.chit_chat.databinding.FragmentContactsBinding
import com.example.chit_chat.di.AppComponentHolder
import com.example.chit_chat.di.ViewModelFactory
import com.example.chit_chat.ui.home.chat_list.adapter.ChatListAdapter
import com.example.chit_chat.ui.common.BaseFragment
import javax.inject.Inject

class ContactsFragment : BaseFragment<FragmentContactsBinding>() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory<ContactsViewModel>

    private val viewModel: ContactsViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[ContactsViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AppComponentHolder.get().inject(this)
        super.onCreate(savedInstanceState)
    }


    override fun createViewBinding(): FragmentContactsBinding {
        return FragmentContactsBinding.inflate(layoutInflater)
    }

    override fun initUi() {
    }

    override fun observeData() {

    }
}