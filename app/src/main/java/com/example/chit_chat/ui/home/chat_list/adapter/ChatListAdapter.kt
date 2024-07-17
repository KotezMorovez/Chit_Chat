package com.example.chit_chat.ui.home.chat_list.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chit_chat.R
import com.example.chit_chat.databinding.ItemChatListBinding


class ChatListAdapter(
    private val onItemClickListener: (item: ChatItem) -> Unit
) : RecyclerView.Adapter<ChatListAdapter.ViewHolder>() {
    private var items: List<ChatItem> = listOf()

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(list: List<ChatItem>) {
        items = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ItemChatListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ViewHolder(private val binding: ItemChatListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ChatItem) {
            with(binding) {
                Glide.with(userAvatarImageView)
                    .load(item.chatUserAvatar)
                    .placeholder(R.drawable.ic_avatar_placeholder)
                    .fitCenter()
                    .circleCrop()
                    .into(userAvatarImageView)

                userNameTextView.text = item.chatUserName
                userMessageTextView.text = item.chatLastMessage
                messageDateTextView.text = item.chatLastMessageDate
                messageCountBadgeItem.messageCountTextView.text = item.chatMessagesCount

                userNameTextView.invalidate()
                userNameTextView.requestLayout()

                userMessageTextView.invalidate()
                userMessageTextView.requestLayout()

                root.setOnClickListener {
                    onItemClickListener.invoke(item)
                }
            }
        }
    }
}