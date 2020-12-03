package me.rounak.newzzzy.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import me.rounak.newzzzy.R
import me.rounak.newzzzy.data.local.BookmarkDatabase
import me.rounak.newzzzy.data.repository.NewsRepository
import me.rounak.newzzzy.databinding.ActivityMainBinding
import me.rounak.newzzzy.ui.main.adapter.NewsAdapter
import me.rounak.newzzzy.ui.main.viewmodel.MainViewModel
import me.rounak.newzzzy.ui.main.viewmodel.MainViewModelFactory

abstract class BaseActivity<mViewModel: ViewModel, mBinding: ViewDataBinding> : AppCompatActivity() {

    protected lateinit var binding: mBinding
//    protected lateinit var viewModel: mViewModel
    protected lateinit var repository: NewsRepository

    abstract fun getLayout(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, getLayout())

        val bookmarkDAO = BookmarkDatabase.getInstance(applicationContext).bookmarkDAO

        repository = NewsRepository(bookmarkDAO)

    }

}
