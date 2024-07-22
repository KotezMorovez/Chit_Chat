package com.example.chit_chat.di

import android.content.Context
import com.example.chit_chat.di.common.DIComponent
import com.example.chit_chat.ui.auth.login.LoginFragment
import com.example.chit_chat.ui.auth.signup.SignUpFragment
import com.example.chit_chat.ui.home.chat_list.ChatListFragment
import com.example.chit_chat.ui.home.contacts.ContactsFragment
import com.example.chit_chat.ui.home.settings.SettingsFragment
import com.example.chit_chat.ui.main.HomeFragment
import com.example.chit_chat.ui.main.MainActivity
import com.example.chit_chat.ui.splash.SplashFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(
    modules = [
        OriginalModule::class,
        ProviderModule::class,
        ApiModule :: class
    ]
)

@Singleton
interface AppComponent : DIComponent {

    fun inject(activity: MainActivity)
    fun inject(loginFragment: LoginFragment)
    fun inject(signUpFragment: SignUpFragment)
    fun inject(splashFragment: SplashFragment)
    fun inject(chatListFragment: ChatListFragment)
    fun inject(settingsFragment: SettingsFragment)
    fun inject(contactsFragment: ContactsFragment)
    fun inject(homeFragment: HomeFragment)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context
        ): AppComponent
    }
}

object AppComponentHolder {
    private var component: AppComponent? = null

    fun get(): AppComponent {
        return component ?: throw IllegalStateException("Component must be set")
    }

    fun set(component: AppComponent?) {
        this.component = component
    }

    fun build(context: Context): AppComponent {
        return DaggerAppComponent.factory().create(context)
    }
}