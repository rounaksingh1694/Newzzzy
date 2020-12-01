package me.rounak.newzzzy.ui.main.activities

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*
import me.rounak.newzzzy.R
import me.rounak.newzzzy.data.local.BookmarkDatabase
import me.rounak.newzzzy.data.repository.NewsRepository
import me.rounak.newzzzy.databinding.ActivityMainBinding
import me.rounak.newzzzy.ui.main.adapter.NewsAdapter
import me.rounak.newzzzy.ui.main.viewmodel.MainViewModel
import me.rounak.newzzzy.ui.main.viewmodel.MainViewModelFactory
import me.rounak.newzzzy.ui.webview.WebViewActivity
import me.rounak.newzzzy.utils.helper.NetworkObserver
import me.rounak.newzzzy.utils.model.NewsArticle

class MainActivity : AppCompatActivity() {

    private val country = "in"

    private lateinit var adapter: NewsAdapter
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var repository: NewsRepository
    private lateinit var tabLayoutListener: TabLayout.OnTabSelectedListener

    private fun setUpViewPager() {

        adapter = NewsAdapter({ article: NewsArticle -> newsArticleClickListener(article) }, { article: NewsArticle, view: View -> newsArticleLongClickListener(article, view) })
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

    }

    private fun getNews(country: String, category: String) {

        setUpViewPager()
        adapter.clear()
        viewModel.getNewsByCountryAndCategory(country, category)

        viewModel.newsResponse.observe(this, Observer { result ->

            if(result.body()?.totalResults != 0) {

                adapter.addToArticles(result.body()?.articles as ArrayList<NewsArticle>)
                Log.i("Articles", result.body()?.articles.toString())

            }

        })

    }

    private fun setUpTabLayout() {

        tabLayoutListener = object : TabLayout.OnTabSelectedListener {

            override fun onTabReselected(tab: TabLayout.Tab?) {}

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabSelected(tab: TabLayout.Tab?) {

                val position = tab?.position

                Log.i("TAB POSITION", "$position ")

                getNews(country, getCategoryFromPosition(position!!))

            }

        }

    }

    private fun getCategoryFromPosition(position: Int): String =
         when(position) {
             0 -> "science"
             1 -> "technology"
             2 -> "health"
             3 -> "sports"
             4 -> "business"
             5 -> "politics"
             else -> "science"
         }

    fun newsArticleClickListener(article: NewsArticle) {

        val intent = Intent(applicationContext, WebViewActivity::class.java)
        intent.putExtra("url", article.url)
        startActivity(intent)

    }

    fun newsArticleLongClickListener(article: NewsArticle, view: View) {

        val popup = PopupMenu(applicationContext, view)
        val menuInflater = MenuInflater(applicationContext)

        menuInflater.inflate(R.menu.menu_news, popup.menu)
        popup.show()

        popup.setOnMenuItemClickListener { item ->

            when(item.itemId) {

                R.id.share -> {

                    val intent = Intent(Intent.ACTION_SEND)
                    intent.setType("text/url")

                    intent.putExtra(Intent.EXTRA_SUBJECT, "News link")
                    intent.putExtra(Intent.EXTRA_TEXT, article.url)

                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

                    startActivity(Intent.createChooser(intent, "Share article link"))

                    return@setOnMenuItemClickListener true

                }

                R.id.copy -> {

                    val clipboard: ClipboardManager? =
                        getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
                    val clip = ClipData.newPlainText("News link", article.url)
                    clipboard?.setPrimaryClip(clip)

                    return@setOnMenuItemClickListener true

                }

                else -> return@setOnMenuItemClickListener false

            }

        }

    }

    private fun setUpRefreshLayout() {

        binding.refreshLayout.setOnRefreshListener {

            adapter.clear()
            binding.refreshLayout.isRefreshing = viewModel.refreshStatus.value!!
            getNews(country, getCategoryFromPosition(binding.tabLayout.selectedTabPosition))

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

                /*R.id.logOut -> {

                    auth.signOut()
                    val intent = Intent(applicationContext, SplashAndLogInActivity::class.java)
                    intent.putExtra("hasLoggedOut", true)
                    startActivity(intent)
                    finish()

                    return@setNavigationItemSelectedListener true

                }*/

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val bookmarkDAO = BookmarkDatabase.getInstance(applicationContext).bookmarkDAO

        repository = NewsRepository(bookmarkDAO)
        val factory =
            MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(MainViewModel::class.java)

        binding.refreshLayout.isRefreshing = true

        setUpTabLayout()
        setUpRefreshLayout()
        setUpNavigationDrawer()
        checkNetworkState()

        binding.tabLayout.addOnTabSelectedListener(tabLayoutListener)

        getNews(country, getCategoryFromPosition(binding.tabLayout.selectedTabPosition))

    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START))
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        else
            super.onBackPressed()
    }

}
