package com.example.chit_chat.ui.features.create_chat.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chit_chat.R
import com.example.chit_chat.databinding.ItemCreateChatContactBinding
import com.example.chit_chat.ui.features.create_chat.dto.ContactItem

class CreateChatAdapter(
    private val onItemClickListener: (item: ContactItem) -> Unit
) : RecyclerView.Adapter<CreateChatAdapter.ViewHolder>() {
    private var items: List<ContactItem> = listOf()

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(list: List<ContactItem>) {
        items = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ItemCreateChatContactBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ViewHolder(private val binding: ItemCreateChatContactBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ContactItem) {
            with(binding) {
                Glide.with(userAvatarImageView)
                    .load(item.avatar)
                    .placeholder(R.drawable.ic_avatar_placeholder)
                    .fitCenter()
                    .circleCrop()
                    .into(userAvatarImageView)

                val contactName = "${item.firstName} ${item.lastName}"

                userNameTextView.text = contactName

                userNameTextView.invalidate()
                userNameTextView.requestLayout()

                contactItemLayout.setOnClickListener {
                    onItemClickListener.invoke(item)
                }
            }
        }
    }
}