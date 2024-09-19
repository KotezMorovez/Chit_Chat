package com.example.chit_chat.ui.home.chat_list.create_chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chit_chat.domain.interactor.ContactsInteractor
import com.example.chit_chat.domain.interactor.ProfileInteractor
import com.example.chit_chat.ui.mapper.toUI
import com.example.chit_chat.ui.model.ContactItem
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class CreateChatViewModel @Inject constructor(
    private val contactsInteractor: ContactsInteractor,
    private val profileInteractor: ProfileInteractor
) : ViewModel() {
    private val _contacts = MutableSharedFlow<List<ContactItem>>(1)
    val contacts = _contacts.asSharedFlow()

    fun updateContacts() {
        viewModelScope.launch {
            val contactsProfileList = profileInteractor.getProfileContactsList()
            if (contactsProfileList.isNotEmpty()) {
                val contactsListResult = contactsInteractor.getContactsFromList(contactsProfileList)
                if (contactsListResult.isSuccess) {
                    val list = contactsListResult.getOrNull()
                    if (list != null) {
                        _contacts.emit(list.map { it.toUI() })
                    }
                }
            }
        }
    }
}