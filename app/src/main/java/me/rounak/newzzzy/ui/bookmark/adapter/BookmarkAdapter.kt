package me.rounak.newzzzy.ui.bookmark.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import me.rounak.newzzzy.R
import me.rounak.newzzzy.databinding.AdapterBookmarkBinding
import me.rounak.newzzzy.databinding.AdapterLayoutBinding
import me.rounak.newzzzy.utils.model.Bookmark
import me.rounak.newzzzy.utils.model.NewsArticle

class BookmarkAdapter(private val clickListener: (Bookmark) -> Unit,
                  private val longClickListener: (Bookmark, View) -> Unit
) : RecyclerView.Adapter<BookmarkAdapter.BookmarkViewHolder>() {

    val articles = ArrayList<Bookmark>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<AdapterBookmarkBinding>(inflater, R.layout.adapter_layout, parent, false)
        val holder = BookmarkViewHolder(binding, clickListener, longClickListener)
        return holder
    }

    override fun getItemCount(): Int = articles.size

    override fun onBindViewHolder(holder: BookmarkViewHolder, position: Int) {
        holder.bind(articles[position])
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
    }

    fun addToArticles(mArticles: ArrayList<Bookmark>) {
        articles.clear()
        articles.addAll(mArticles)
        notifyDataSetChanged()
    }

    fun clear() {
        articles.clear()
        notifyDataSetChanged()
    }

    class BookmarkViewHolder(private val binder: AdapterBookmarkBinding, private val clickListener: (Bookmark) -> Unit, private val longClickListener: (Bookmark, View) -> Unit) : RecyclerView.ViewHolder(binder.root) {

        fun bind(article: Bookmark) {

            if(article.urlToImage != null) {
                Picasso.get()
                    .load(article.urlToImage)
                    .into(binder.imageView)
            }

            binder.textViewTitle.text = article.title
            binder.textViewDescription.text = article.description
            binder.textViewCategory.text = article.category
            binder.textViewSource.text = article.source.name
            binder.textViewDate.text = article.publishedAt.substring(0, article.publishedAt.indexOf("T"))
            Log.i("Added", "TO RV")

            binder.cardView.setOnClickListener {
                clickListener(article)
            }

            binder.cardView.setOnLongClickListener {
                longClickListener(article, binder.textViewTitle)
                return@setOnLongClickListener false
            }

            binder.imageViewNewsMenu.setOnClickListener { longClickListener(article, binder.imageViewNewsMenu) }

        }

    }

}
