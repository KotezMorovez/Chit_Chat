package com.example.chit_chat.ui.home.chat_list.create_chat

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chit_chat.R
import com.example.chit_chat.utils.collectWithLifecycle
import com.example.chit_chat.databinding.FragmentCreateChatBinding
import com.example.chit_chat.di.AppComponentHolder
import com.example.chit_chat.di.ViewModelFactory
import com.example.chit_chat.ui.common.BaseFragment
import com.example.chit_chat.ui.home.chat_list.create_chat.adapter.CreateChatAdapter
import javax.inject.Inject

class CreateChatFragment : BaseFragment<FragmentCreateChatBinding>() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory<CreateChatViewModel>

    private val createChatAdapter: CreateChatAdapter
    private val viewModel: CreateChatViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[CreateChatViewModel::class.java]
    }

    init {
        createChatAdapter = CreateChatAdapter(onItemClickListener = { item ->
            // navigate to dialog screen
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AppComponentHolder.get().inject(this)
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