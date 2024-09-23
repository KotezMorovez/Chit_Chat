package com.example.chit_chat.ui.features.chat_list.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chit_chat.R
import com.example.chit_chat.databinding.ItemChatListBinding
import com.example.chit_chat.ui.features.chat_list.dto.chat.ChatItem

class ChatListAdapter(
    private val onItemClickListener: (item: ChatItem) -> Unit,
    private val onLongPressListener: (
        item: ChatItem,
        itemView: View,
        coordinates: Pair<Float, Float>
    ) -> Unit
) : RecyclerView.Adapter<ChatListAdapter.ViewHolder>() {
    private var items: List<ChatItem> = listOf()

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
        @SuppressLint("ClickableViewAccessibility")
        fun bind(item: ChatItem) {
            var lastX = 0f
            var lastY = 0f
            with(binding) {
                Glide.with(userAvatarImageView)
                    .load(item.userAvatar)
                    .placeholder(R.drawable.ic_avatar_placeholder)
                    .fitCenter()
                    .circleCrop()
                    .into(userAvatarImageView)

                userNameTextView.text = item.userName
                userMessageTextView.text = item.lastMessage
                messageDateTextView.text = item.lastMessageDate

                userNameTextView.invalidate()
                userNameTextView.requestLayout()

                userMessageTextView.invalidate()
                userMessageTextView.requestLayout()

                itemLayout.setOnClickListener {
                    onItemClickListener.invoke(item)
                }

                itemLayout.setOnTouchListener { view, event ->
                    lastX = event.x
                    lastY = event.y
                    false
                }

                itemLayout.setOnLongClickListener {
                    onLongPressListener.invoke(item, it, Pair(lastX, lastY))
                    true
                }
            }
        }
    }
}