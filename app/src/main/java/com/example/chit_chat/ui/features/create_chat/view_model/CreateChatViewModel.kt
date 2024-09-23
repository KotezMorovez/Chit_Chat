package com.example.chit_chat.ui.features.create_chat.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chit_chat.domain.profile.interactor.ContactsInteractor
import com.example.chit_chat.domain.profile.interactor.ProfileInteractor
import com.example.chit_chat.ui.features.create_chat.dto.ContactItem
import com.example.chit_chat.ui.features.chat_list.dto.chat.toUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
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