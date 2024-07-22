package com.example.chit_chat.ui.home.settings

import android.os.Bundle
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isGone
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.chit_chat.R
import com.example.chit_chat.databinding.FragmentSettingsBinding
import com.example.chit_chat.databinding.ItemSettingsBinding
import com.example.chit_chat.di.AppComponentHolder
import com.example.chit_chat.di.ViewModelFactory
import com.example.chit_chat.ui.common.BaseFragment
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
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
        viewModel.getProfile()
        with(viewBinding) {
            avatarImageView.setOnClickListener {
//                val bundle = Bundle()
//                bundle.putString("imageUrl", ____) // TODO: Add image url to bundle

//                this@SettingsFragment.findNavController()
//                    .navigate(R.id.action_settingsFragment_to_showImageFragment, bundle)
            }

            newAvatarButton.setOnClickListener {

//                viewModel.updateAvatar(avatarUrl)
            }

            setUpSettingsItem(
                item = accountSettingsItem,
                image = R.drawable.ic_avatar_placeholder,
                string = R.string.settings_account,
                listener = {
//                this@SettingsFragment.findNavController()
//                    .navigate(R.id.action_settingsFragment_to_accountSettingsFragment)
                }
            )

            setUpSettingsItem(
                item = notificationsItem,
                image = R.drawable.ic_notifications,
                string = R.string.settings_notifications,
                listener = {
//                this@SettingsFragment.findNavController()
//                    .navigate(R.id.action_settingsFragment_to_notificationsFragment)
                }
            )

            setUpSettingsItem(
                item = switchAccountItem,
                image = R.drawable.ic_switch,
                string = R.string.settings_switch_account,
                listener = {
//                this@SettingsFragment.findNavController()
//                    .navigate(R.id.action_settingsFragment_to_chooseAccountFragment)
                }
            )

            setUpSettingsItem(
                item = reportProblemItem,
                image = R.drawable.ic_report,
                string = R.string.settings_report,
                listener = {
//                this@SettingsFragment.findNavController()
//                    .navigate(R.id.action_settingsFragment_to_dialogFragment)
                }
            )

            setUpSettingsItem(
                item = logoutItem,
                image = R.drawable.ic_logout,
                string = R.string.settings_logout,
                listener = {
//                this@SettingsFragment.findNavController()
//                    .navigate(R.id.action_settingsFragment_to_loginFragment)
                }
            )

            logoutItem.itemTextView.setTextColor(
                ResourcesCompat.getColor(
                    resources,
                    R.color.product_red,
                    null
                )
            )
            logoutItem.itemButton.isGone = true
        }
    }

    private fun setUpSettingsItem(
        item: ItemSettingsBinding,
        @DrawableRes image: Int,
        @StringRes string: Int,
        listener: View.OnClickListener
    ) {
        item.itemImageView.setImageDrawable(
            ResourcesCompat.getDrawable(resources, image, null)
        )
        item.itemTextView.setText(string)
        item.itemButton.setOnClickListener(listener)
    }

    override fun observeData() {
        with(viewBinding) {
            viewLifecycleOwner.lifecycleScope.launch {
                launch {
                    viewModel.profile.collect {
                        Glide.with(avatarImageView)
                            .load(it.avatar)
                            .placeholder(R.drawable.ic_round_avatar_placeholder)
                            .fitCenter()
                            .circleCrop()
                            .into(avatarImageView)
                    }
                }
            }
        }
    }
}