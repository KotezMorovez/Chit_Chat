package com.example.chit_chat.ui.home.chat_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.WindowCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chit_chat.R
import com.example.chit_chat.common.collectWithLifecycle
import com.example.chit_chat.databinding.FragmentChatListBinding
import com.example.chit_chat.di.AppComponentHolder
import com.example.chit_chat.di.ViewModelFactory
import com.example.chit_chat.ui.home.chat_list.adapter.ChatListAdapter
import com.example.chit_chat.ui.common.BaseFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChatListFragment : BaseFragment<FragmentChatListBinding>() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory<ChatListViewModel>

    private val viewModel: ChatListViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[ChatListViewModel::class.java]
    }
    private val chatListAdapter: ChatListAdapter
    private var toolbarState: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        AppComponentHolder.get().inject(this)
        super.onCreate(savedInstanceState)
    }

    init {
        chatListAdapter = ChatListAdapter(
            onItemClickListener = { item ->
//            val bundle = Bundle()
//            bundle.putString(USER_ID, item.userId)
//            this@ChatListFragment.findNavController().navigate(
//                R.id.action_chatListFragment_to_chatFragment, bundle
//            )
            },

//            onItemSwipeListener = { item ->
//            },
//
//            onDeleteItemClickListener = {
//                viewModel.deleteChat(it.chatId)
//            }
        )
    }

    override fun createViewBinding(): FragmentChatListBinding {
        return FragmentChatListBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        toolbarState = savedInstanceState?.getBoolean(TOOLBAR_STATE, true) ?: true
        viewBinding.chatListToolbar.searchEditText.setText(
            savedInstanceState?.getString(
                SEARCH,
                EMPTY_STRING
            )
        )
        applyToolbarState(toolbarState)
        return view
    }

    override fun onResume() {
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        super.onResume()
    }

    override fun onPause() {
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        super.onPause()
    }

    override fun initUi() {
        setStatusBar()
        val imm = requireContext().getSystemService(InputMethodManager::class.java)
        viewModel.subscribeProfile()
        viewModel.subscribeChatList()

        with(viewBinding) {
            chatListToolbar.loaderView.isVisible = true
            chatListToolbar.defaultToolbar.isGone = true
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                chatListToolbar.loaderView.startLoader()
            }

            chatListToolbar.searchEditText.setOnEditorActionListener { _, actionId, _ ->
                return@setOnEditorActionListener if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    imm.hideSoftInputFromWindow(chatListToolbar.searchEditText.windowToken, 0)
                    true
                } else false
            }

            chatListToolbar.toolbarSearchIcon.setOnClickListener {
                toolbarState = !toolbarState
                applyToolbarState(toolbarState)
                chatListToolbar.searchEditText.requestFocus()
                chatListToolbar.searchEditText

                imm.showSoftInput(chatListToolbar.searchEditText, InputMethodManager.SHOW_IMPLICIT)
            }

            chatListToolbar.toolbarCloseIcon.setOnClickListener {
                toolbarState = !toolbarState
                chatListToolbar.searchEditText.clearFocus()
                imm.hideSoftInputFromWindow(chatListToolbar.searchEditText.windowToken, 0)
                applyToolbarState(toolbarState)
            }

            chatListToolbar.createChatIcon.setOnClickListener {
//                this@ChatListFragment.findNavController()
//                    .navigate(
//                        R.id.action_chatListFragment_to_createChatFragment
//                    )
            }

            chatsRecyclerView.adapter = chatListAdapter
            chatsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun applyToolbarState(isDefaultToolbar: Boolean) {
        with(viewBinding) {
            chatListToolbar.defaultToolbar.isGone = true
            chatListToolbar.searchToolbar.isGone = true
            if (isDefaultToolbar) {
                hideSearchField()
            } else {
                showSearchField()
            }
        }
    }

    private fun showSearchField() {
        with(viewBinding) {
            chatListToolbar.defaultToolbar.animate().alpha(0.0f).duration = 300L
            chatListToolbar.searchToolbar.animate().alpha(1.0f).duration = 300L
            chatListToolbar.searchToolbar.isVisible = true
        }
    }

    private fun hideSearchField() {
        with(viewBinding) {
            chatListToolbar.defaultToolbar.animate().alpha(1.0f).duration = 300L
            chatListToolbar.searchToolbar.animate().alpha(0.0f).duration = 300L
            chatListToolbar.defaultToolbar.isVisible = true
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(TOOLBAR_STATE, toolbarState)
        outState.putString(
            SEARCH,
            viewBinding.chatListToolbar.searchEditText.text.toString().trim()
        )
        super.onSaveInstanceState(outState)
    }

    private fun setStatusBar() {
        val window = requireActivity().window
        window.statusBarColor = resources.getColor(R.color.white, null)
        WindowCompat.setDecorFitsSystemWindows(window, true)
    }

    private fun setToolbarTitle(title: String) {
        with(viewBinding) {
            chatListToolbar.loaderView.isGone = true
            chatListToolbar.loaderView.stopLoader()
            chatListToolbar.defaultToolbar.isVisible = true
            chatListToolbar.toolbarTitle.text = title
        }
    }

    override fun observeData() {
        with(viewBinding) {
            viewModel.profile.collectWithLifecycle(
                viewLifecycleOwner
            ) {
                val title = "${it.firstName} ${it.lastName}"
                setToolbarTitle(title)
            }

            viewModel.chatList.collectWithLifecycle(
                viewLifecycleOwner
            ) {
                chatListAdapter.setItems(it)
            }

            viewModel.eventError.collectWithLifecycle(
                viewLifecycleOwner
            ) {
                val snackBar = Snackbar.make(
                    requireContext(),
                    viewBinding.root,
                    resources.getString(it),
                    Snackbar.LENGTH_SHORT
                )
                snackBar.show()
            }
        }
    }

    companion object {
        private const val TOOLBAR_STATE = "toolbar state"
        private const val SEARCH = "search"
        private const val EMPTY_STRING = ""
    }
}