package com.example.chit_chat.ui.home.chat_list.create_chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chit_chat.domain.interactor.ContactsInteractor
import com.example.chit_chat.ui.model.ContactItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateChatViewModel @Inject constructor(
    private val contactsInteractor: ContactsInteractor
) : ViewModel() {
    private val _contacts = MutableSharedFlow<List<ContactItem>>(1)
    val contacts = _contacts.asSharedFlow()

    fun subscribeContactsUpdate() {
        viewModelScope.launch {

        }
    }
}