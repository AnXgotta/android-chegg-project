package homework.chegg.com.chegghomework.activitymain

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.distinctUntilChanged
import dagger.hilt.android.AndroidEntryPoint
import homework.chegg.com.chegghomework.R
import homework.chegg.com.chegghomework.data.modules.NewsFeed
import homework.chegg.com.chegghomework.databinding.ActivityMainBinding
import homework.chegg.com.chegghomework.ui.adapter.ArticlesAdapter
import homework.chegg.com.chegghomework.ui.viewmodel.StoryViewModel
import homework.chegg.com.chegghomework.util.DataResource

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val storyViewModel: StoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        buildUi()
        initializeLiveData()
        fetchData()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar.toolbar)
    }

    private fun buildUi() {
        binding.adapter = ArticlesAdapter()
    }

    private fun initializeLiveData() {
        storyViewModel.storiesLiveData
            .distinctUntilChanged()
            .observe(this) { dataResource ->
                when (dataResource.status) {
                    DataResource.Status.SUCCESS -> {
                        binding.adapter!!.submitList(
                            dataResource.data!!.map { NewsFeed(it.title, it.subtitle, it.imageUrl) }
                        )
                    }
                    DataResource.Status.ERROR -> {
                        // informative only for this view
                    }
                    DataResource.Status.LOADING -> {
                        // informative only for this view
                    }
                    DataResource.Status.EMPTY -> {
                        // informative only for this view
                    }
                }

                // this likely could be simplified - fix if time
                binding.showData = dataResource.status == DataResource.Status.SUCCESS
                binding.isLoading = dataResource.status == DataResource.Status.LOADING
                binding.isEmpty = dataResource.status == DataResource.Status.EMPTY
                binding.isError = dataResource.status == DataResource.Status.ERROR
            }
    }

    private fun fetchData() {
        storyViewModel.getStories()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main_activity, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_refresh -> {
                onRefreshData()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun onRefreshData() {
        fetchData()
    }
}
