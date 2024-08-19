package com.example.chit_chat.ui.home.showImage

import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isGone
import com.example.chit_chat.R
import com.example.chit_chat.databinding.FragmentShowImageBinding
import com.example.chit_chat.ui.common.BaseFragment
import com.example.chit_chat.utils.loadImage

class ShowImageFragment : BaseFragment<FragmentShowImageBinding>() {

    override fun createViewBinding(): FragmentShowImageBinding {
        return FragmentShowImageBinding.inflate(layoutInflater)
    }

    override fun initUi() {
        val imageUrl = arguments?.getString("imageUrl")

        with(viewBinding) {
            if (imageUrl.isNullOrBlank()) {
                showImageImageView.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.ic_round_avatar_placeholder,
                        null
                    )
                )
            } else {
                showImageImageView.loadImage(imageUrl, R.drawable.ic_round_avatar_placeholder){}
            }
            profileImageProgress.isGone = true

            (activity as AppCompatActivity).setSupportActionBar(showImageToolbar)
            showImageToolbar.title = null
            showImageToolbar.setNavigationOnClickListener {
                (activity as AppCompatActivity).onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    override fun observeData() {}
}