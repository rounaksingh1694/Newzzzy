package me.rounak.newzzzy.ui.bookmark.activities

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_clear_bookmarks.*
import me.rounak.newzzzy.R
import me.rounak.newzzzy.databinding.ActivityBookmarkBinding
import me.rounak.newzzzy.ui.base.BaseActivity
import me.rounak.newzzzy.ui.bookmark.adapter.BookmarkAdapter
import me.rounak.newzzzy.ui.bookmark.viewmodel.BookmarkViewModel
import me.rounak.newzzzy.ui.bookmark.viewmodel.BookmarkViewModelFactory
import me.rounak.newzzzy.ui.webview.WebViewActivity
import me.rounak.newzzzy.utils.helper.NetworkObserver
import me.rounak.newzzzy.utils.model.Bookmark

class BookmarkActivity : BaseActivity<BookmarkViewModel, ActivityBookmarkBinding>() {

    private lateinit var adapter: BookmarkAdapter
    lateinit var bookmarkViewModel: BookmarkViewModel

    private fun setUpViewPager() {

        adapter = BookmarkAdapter({ article: Bookmark -> bookmarkArticleClickListener(article) }, { article: Bookmark, view: View -> bookmarkArticleLongClickListener(article, view) })
        viewPager.adapter = adapter
        viewPager.setClipToPadding(false)
        viewPager.setClipChildren(false)
        viewPager.setOffscreenPageLimit(3)
        viewPager.setPadding(100, 0, 100, 0)
        viewPager.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_ALWAYS)
        val pagerContext = viewPager.context
        val layoutAnimationController = AnimationUtils.loadLayoutAnimation(pagerContext, R.anim.second_anim)
        viewPager.layoutAnimation = layoutAnimationController
        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer(40))
        compositePageTransformer.addTransformer { page, position ->
            val r = 1 - Math.abs(position)
            page.scaleY = 0.85f + r * 0.15f
        }
        viewPager.setPageTransformer(compositePageTransformer)

        bookmarkViewModel.bookmarks.observe(this, Observer { bookmarks ->
            adapter.addToArticles(bookmarks as ArrayList<Bookmark>)
            displayNoBookmarksMessage()
            Log.i("Bookmarks", bookmarks.toString())
        })

    }

    fun bookmarkArticleClickListener(bookmark: Bookmark) {

        val intent = Intent(applicationContext, WebViewActivity::class.java)
        intent.putExtra("url", bookmark.url)
        startActivity(intent)

    }

    fun bookmarkArticleLongClickListener(bookmark: Bookmark, view: View) {

        val popup = PopupMenu(applicationContext, view)
        val menuInflater = MenuInflater(applicationContext)

        menuInflater.inflate(R.menu.menu_bookmark, popup.menu)
        popup.show()

        popup.setOnMenuItemClickListener { item ->

            when(item.itemId) {

                R.id.share -> {

                    val intent = Intent(Intent.ACTION_SEND)
                    intent.setType("text/url")

                    intent.putExtra(Intent.EXTRA_SUBJECT, "News link")
                    intent.putExtra(Intent.EXTRA_TEXT, bookmark.url)

                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

                    startActivity(Intent.createChooser(intent, "Share article link"))

                    return@setOnMenuItemClickListener true

                }

                R.id.copy -> {

                    val clipboard: ClipboardManager? =
                        getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
                    val clip = ClipData.newPlainText("News link", bookmark.url)
                    clipboard?.setPrimaryClip(clip)

                    return@setOnMenuItemClickListener true

                }

                R.id.deleteBookmark -> {

                    bookmarkViewModel.deleteBookmark(bookmark)

                    displayNoBookmarksMessage()

                    return@setOnMenuItemClickListener true

                }

                else -> return@setOnMenuItemClickListener false

            }

        }

    }

    private fun openOrCloseNavDrawer() {

        if(binding.drawerLayout.isDrawerOpen(GravityCompat.START))
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        else
            binding.drawerLayout.openDrawer(GravityCompat.START)

    }

    private fun checkNetworkState() {

        NetworkObserver.getNetworkLiveData(applicationContext).observe(this, Observer { isConnected ->

            if(!isConnected) {

                binding.layoutNetworkState.visibility = View.VISIBLE
                binding.layoutNetworkState.setBackgroundColor(ContextCompat.getColor(this, R.color.no_connection))
                binding.textViewConnectionStatus.text = getString(R.string.no_connection)
                binding.textViewConnectionStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_no_connection, 0, 0, 0)

            } else {

                binding.layoutNetworkState.setBackgroundColor(ContextCompat.getColor(this, R.color.connection_back))
                binding.textViewConnectionStatus.text = getString(R.string.connection_back)
                binding.textViewConnectionStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_connection_back, 0, 0, 0)

                binding.layoutNetworkState.apply {

                    animate()
                        .alpha(1f)
                        .setStartDelay(1000)
                        .setDuration(1000)
                        .setListener(object : AnimatorListenerAdapter(){
                            override fun onAnimationEnd(animation: Animator?) {
                                binding.layoutNetworkState.visibility = View.GONE
                            }
                        })

                }

            }

        })

    }

    private fun setUpNavigationDrawer() {

        val view = sideNavDrawer.menu.getItem(1).subMenu.getItem(0).actionView

        val modeSwitch = view.findViewById<SwitchCompat>(R.id.switchCompatMode)

        modeSwitch.setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }

        }

        binding.imageViewSideNav.setOnClickListener { openOrCloseNavDrawer() }

        binding.sideNavDrawer.inflateHeaderView(R.layout.side_nav_header)
        binding.sideNavDrawer.setNavigationItemSelectedListener {

            when (it.itemId) {

                R.id.modeSwitch -> Toast.makeText(
                    applicationContext,
                    "It works!",
                    Toast.LENGTH_SHORT
                ).show()

                R.id.twitter -> {

                    val twitterUrl = "https://twitter.com/RounakSingh_16"

                    val twitterIntent = Intent(Intent.ACTION_VIEW, Uri.parse(twitterUrl))
                    twitterIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)
                    startActivity(twitterIntent)

                }

                R.id.instagram -> {

                    val instagramUrl = "https://www.instagram.com/nothing_on_papers/"

                    val instagramIntent = Intent(Intent.ACTION_VIEW, Uri.parse(instagramUrl))
                    instagramIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)
                    startActivity(instagramIntent)

                }

                R.id.gitHub -> {

                    val gitHubUrl = "https://github.com/rounaksingh1694"

                    val gitHubIntent = Intent(Intent.ACTION_VIEW, Uri.parse(gitHubUrl))
                    gitHubIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)
                    startActivity(gitHubIntent)

                }

                R.id.website -> {

                    val websiteUrl = "https://wenull.netlify.app/ourteam"

                    val websiteIntent = Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl))
                    websiteIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)
                    startActivity(websiteIntent)

                }

            }

            return@setNavigationItemSelectedListener true

        }

    }

    fun displayNoBookmarksMessage() {

        if(adapter.articles.isEmpty()) {
            binding.textViewNoBookmarks.visibility = View.VISIBLE
            binding.textViewExplore.visibility = View.VISIBLE

            binding.textViewExplore.setOnClickListener { finish() }
        } else {
            binding.textViewNoBookmarks.visibility = View.INVISIBLE
            binding.textViewExplore.visibility = View.INVISIBLE
        }

    }

    private fun setUpClearBookmarks() {

        binding.imageViewClearBookmarks.setOnClickListener {
            
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.dialog_clear_bookmarks)

            dialog.textViewClearNo.setOnClickListener { dialog.dismiss() }

            dialog.textViewClearYes.setOnClickListener {
                bookmarkViewModel.deleteAllBookmarks()
                displayNoBookmarksMessage()
                dialog.dismiss()
            }

            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()

        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bookmark)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_bookmark)

        val factory =
            BookmarkViewModelFactory(repository)
        bookmarkViewModel = ViewModelProvider(this, factory).get(BookmarkViewModel::class.java)

        setUpNavigationDrawer()
        setUpViewPager()
        checkNetworkState()
        setUpClearBookmarks()
        displayNoBookmarksMessage()

    }

    override fun getLayout(): Int = R.layout.activity_bookmark

}
