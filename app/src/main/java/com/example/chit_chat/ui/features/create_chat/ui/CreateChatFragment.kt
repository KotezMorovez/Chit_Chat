package com.example.chit_chat.ui.features.create_chat.ui

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chit_chat.R
import com.example.chit_chat.utils.collectWithLifecycle
import com.example.chit_chat.databinding.FragmentCreateChatBinding
import com.example.chit_chat.ui.common.BaseFragment
import com.example.chit_chat.ui.features.create_chat.ui.adapter.CreateChatAdapter
import com.example.chit_chat.ui.features.create_chat.view_model.CreateChatViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateChatFragment : BaseFragment<FragmentCreateChatBinding>() {
    private val createChatAdapter: CreateChatAdapter
    private val viewModel by viewModels<CreateChatViewModel>()

    init {
        createChatAdapter = CreateChatAdapter(onItemClickListener = { item ->
            // navigate to dialog screen
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun createViewBinding(): FragmentCreateChatBinding {
        return FragmentCreateChatBinding.inflate(layoutInflater)
    }

    override fun initUi() {
        val imm = requireContext().getSystemService(InputMethodManager::class.java)
        viewModel.updateContacts()

        with(viewBinding) {
            createChatCanselButton.setOnClickListener {
                activity?.onBackPressedDispatcher?.onBackPressed()
            }

            createChatGroupItem.iconImageView.setImageDrawable(
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.ic_create_group,
                    null
                )
            )
            createChatGroupItem.titleTextView.text =
                resources.getText(R.string.create_chat_create_group)
            createChatGroupItem.root.setOnClickListener {
                // navigate to create group screen
            }

            createChatSearchEditText.setOnEditorActionListener { _, actionId, _ ->
                return@setOnEditorActionListener if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    imm.hideSoftInputFromWindow(createChatSearchEditText.windowToken, 0)
                    true
                } else false
            }

            createChatContactsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            createChatContactsRecyclerView.adapter = createChatAdapter
        }
    }

    override fun observeData() {
        viewModel.contacts.collectWithLifecycle(
            viewLifecycleOwner
        ) {
            createChatAdapter.setItems(it)
        }
    }
}