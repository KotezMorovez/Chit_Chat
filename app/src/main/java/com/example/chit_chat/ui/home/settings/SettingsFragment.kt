package com.example.chit_chat.ui.home.settings

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.chit_chat.databinding.FragmentChatListBinding
import com.example.chit_chat.databinding.FragmentSettingsBinding
import com.example.chit_chat.di.AppComponentHolder
import com.example.chit_chat.di.ViewModelFactory
import com.example.chit_chat.ui.home.chat_list.adapter.ChatListAdapter
import com.example.chit_chat.ui.common.BaseFragment
import javax.inject.Inject

class SettingsFragment : BaseFragment<FragmentSettingsBinding>() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory<SettingsViewModel>
    private val viewModel: SettingsViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[SettingsViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AppComponentHolder.get().inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun createViewBinding(): FragmentSettingsBinding {
        return FragmentSettingsBinding.inflate(layoutInflater)
    }

    override fun initUi() {
    }

    override fun observeData() {

    }
}