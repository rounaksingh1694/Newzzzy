package me.rounak.newzzzy.ui.main.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import me.rounak.newzzzy.R
import me.rounak.newzzzy.databinding.AdapterLayoutBinding
import me.rounak.newzzzy.utils.model.NewsArticle


class NewsAdapter(private val clickListener: (NewsArticle) -> Unit,
                  private val longClickListener: (NewsArticle, View) -> Unit
) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    val articles = ArrayList<NewsArticle>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<AdapterLayoutBinding>(inflater, R.layout.adapter_layout, parent, false)
        val holder = NewsViewHolder(binding, clickListener, longClickListener)
        return holder
    }

    override fun getItemCount(): Int = articles.size

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(articles[position])
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
    }

    fun addToArticles(mArticles: ArrayList<NewsArticle>) {
        articles.clear()
        articles.addAll(mArticles)
        notifyDataSetChanged()
    }

    fun clear() {
        articles.clear()
        notifyDataSetChanged()
    }

    class NewsViewHolder(private val binder: AdapterLayoutBinding, private val clickListener: (NewsArticle) -> Unit, private val longClickListener: (NewsArticle, View) -> Unit) : RecyclerView.ViewHolder(binder.root) {

        fun bind(article: NewsArticle) {

            if(article.urlToImage != null) {
                Picasso.get()
                    .load(article.urlToImage)
                    .into(binder.imageView)
            }

            binder.textViewTitle.text = article.title
            binder.textViewDescription.text = article.description
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
