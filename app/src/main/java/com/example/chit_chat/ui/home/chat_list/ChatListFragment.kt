package com.example.chit_chat.ui.home.chat_list

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chit_chat.R
import com.example.chit_chat.data.service.chat_list.ChatListMock
import com.example.chit_chat.databinding.FragmentChatListBinding
import com.example.chit_chat.di.AppComponentHolder
import com.example.chit_chat.di.ViewModelFactory
import com.example.chit_chat.ui.home.chat_list.adapter.ChatListAdapter
import com.example.chit_chat.ui.common.BaseFragment
import javax.inject.Inject

class ChatListFragment : BaseFragment<FragmentChatListBinding>() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory<ChatListViewModel>
    private val chatListAdapter: ChatListAdapter
    private var toolbarState: Boolean = true
    private val viewModel: ChatListViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[ChatListViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AppComponentHolder.get().inject(this)
        super.onCreate(savedInstanceState)
    }

    init {
        chatListAdapter = ChatListAdapter(onItemClickListener = { item ->

        })
    }

    override fun createViewBinding(): FragmentChatListBinding {
        return FragmentChatListBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        toolbarState = savedInstanceState?.getBoolean(TOOLBAR_STATE, true) ?: true
        viewBinding.searchEditText.setText(savedInstanceState?.getString(SEARCH, EMPTY_STRING))
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
        viewModel.getProfile()
        setStatusBar()
        val imm = requireContext().getSystemService(InputMethodManager::class.java)

        with(viewBinding) {
            toolbarTitle.text = "TestNameToolbar" // TODO: Replace string with received from profile

            searchEditText.setOnEditorActionListener { _, actionId, _ ->
                return@setOnEditorActionListener if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    imm.hideSoftInputFromWindow(searchEditText.windowToken, 0)
                    true
                } else false
            }

            toolbarSearchIcon.setOnClickListener {
                toolbarState = !toolbarState
                applyToolbarState(toolbarState)
                searchEditText.requestFocus()
                searchEditText

                imm.showSoftInput(searchEditText, InputMethodManager.SHOW_IMPLICIT)
            }

            toolbarCloseIcon.setOnClickListener {
                toolbarState = !toolbarState
                searchEditText.clearFocus()
                imm.hideSoftInputFromWindow(searchEditText.windowToken, 0)
                applyToolbarState(toolbarState)
            }

            chatListAdapter.setItems(ChatListMock.getChatList())
            chatsRecyclerView.adapter = chatListAdapter
            chatsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun applyToolbarState(isDefaultToolbar: Boolean) {
        with(viewBinding) {
            defaultToolbar.isGone = true
            searchToolbar.isGone = true
            if (isDefaultToolbar) {
                hideSearchField()
            } else {
                showSearchField()
            }
        }
    }

    private fun showSearchField() {
        with(viewBinding) {
            defaultToolbar.animate().alpha(0.0f).duration = 300L
            searchToolbar.animate().alpha(1.0f).duration = 300L
            searchToolbar.isVisible = true
        }
    }

    private fun hideSearchField() {
        with(viewBinding) {
            defaultToolbar.animate().alpha(1.0f).duration = 300L
            searchToolbar.animate().alpha(0.0f).duration = 300L
            defaultToolbar.isVisible = true
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(TOOLBAR_STATE, toolbarState)
        outState.putString(SEARCH, viewBinding.searchEditText.text.toString().trim())
        super.onSaveInstanceState(outState)
    }

    private fun setStatusBar() {
        val window = requireActivity().window
        window.statusBarColor = resources.getColor(R.color.white, null)
        WindowCompat.setDecorFitsSystemWindows(window, true)
    }

    override fun observeData() {}

    companion object {
        private const val TOOLBAR_STATE = "toolbar state"
        private const val SEARCH = "search"
        private const val EMPTY_STRING = ""
    }
}