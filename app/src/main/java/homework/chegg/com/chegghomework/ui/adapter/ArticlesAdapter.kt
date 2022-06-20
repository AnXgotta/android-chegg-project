package homework.chegg.com.chegghomework.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import homework.chegg.com.chegghomework.R
import homework.chegg.com.chegghomework.data.modules.NewsFeed
import homework.chegg.com.chegghomework.databinding.CardItemBinding

class ArticlesAdapter : ListAdapter<NewsFeed, ArticlesAdapter.NewsFeedHolder>(Companion) {

    companion object : DiffUtil.ItemCallback<NewsFeed>() {
        override fun areItemsTheSame(oldItem: NewsFeed, newItem: NewsFeed) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: NewsFeed, newItem: NewsFeed) =
            oldItem == newItem
    }

    class NewsFeedHolder(val parent: ViewGroup, val binding: CardItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsFeedHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CardItemBinding.inflate(layoutInflater, parent, false)
        return NewsFeedHolder(parent, binding)
    }

    override fun onBindViewHolder(holder: NewsFeedHolder, position: Int) {
        val newsFeed = getItem(position)
        holder.binding.newsFeed = newsFeed
        holder.binding.executePendingBindings()
        holder.itemView.animation = AnimationUtils.loadAnimation(holder.parent.context, R.anim.item_news_feed)
    }
}
