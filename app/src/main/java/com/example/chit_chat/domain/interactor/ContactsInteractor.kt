package com.example.chit_chat.domain.interactor

import com.example.chit_chat.domain.model.Contact
import com.example.chit_chat.domain.repository.ProfileRepository
import javax.inject.Inject

interface ContactsInteractor {
    suspend fun getContactsFromList(contactsProfileList: List<String>): Result<List<Contact>>
}

class ContactsInteractorImpl @Inject constructor(
    private val profileRepository: ProfileRepository
) : ContactsInteractor {
    override suspend fun getContactsFromList(
        contactsProfileList: List<String>
    ): Result<List<Contact>> {
        return profileRepository.getContactsFromList(contactsProfileList)
    }
}
