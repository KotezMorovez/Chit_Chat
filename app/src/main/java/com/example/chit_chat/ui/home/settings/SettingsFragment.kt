package com.example.chit_chat.ui.home.settings

import android.os.Bundle
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isGone
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.chit_chat.R
import com.example.chit_chat.common.collectWithLifecycle
import com.example.chit_chat.databinding.FragmentSettingsBinding
import com.example.chit_chat.databinding.ItemSettingsBinding
import com.example.chit_chat.di.AppComponentHolder
import com.example.chit_chat.di.ViewModelFactory
import com.example.chit_chat.ui.common.BaseFragment
import com.example.chit_chat.ui.common.GalleryHandler
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsFragment : BaseFragment<FragmentSettingsBinding>() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory<SettingsViewModel>
    private val viewModel: SettingsViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[SettingsViewModel::class.java]
    }
    private lateinit var galleryHandler: GalleryHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        galleryHandler = GalleryHandler(this) {
            viewModel.uploadImage(it, requireContext().contentResolver)
        }
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
                val bundle = Bundle()
                bundle.putString(URL_KEY, viewModel.getImage())

                this@SettingsFragment.findNavController()
                    .navigate(R.id.action_settingsFragment_to_showImageFragment, bundle)
            }

            newAvatarButton.setOnClickListener {
                galleryHandler.selectImage()
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
        viewModel.profile.collectWithLifecycle(
            viewLifecycleOwner
        ) {
            with(viewBinding) {
                Glide.with(avatarImageView)
                    .load(it.avatar)
                    .placeholder(R.drawable.ic_round_avatar_placeholder)
                    .fitCenter()
                    .circleCrop()
                    .into(avatarImageView)
            }
        }

        viewModel.event.collectWithLifecycle(
            viewLifecycleOwner
        ) {
            val snackBar = Snackbar.make(
                requireContext(),
                viewBinding.root,
                requireContext().getText(it),
                Snackbar.LENGTH_SHORT
            )
            snackBar.show()
        }
    }

    companion object {
        private const val URL_KEY = "imageUrl"
    }
}