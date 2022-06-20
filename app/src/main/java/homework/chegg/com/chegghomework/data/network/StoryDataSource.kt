package homework.chegg.com.chegghomework.data.network

import homework.chegg.com.chegghomework.data.entities.StoryItem
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class StoryDataSource @Inject constructor(
    private val storyDataSources: Map<Int, Pair<IStorySource, CacheTimestamp>>
) : BaseDataSource() {

    class CacheTimestamp(private val timeoutMins: Long, private var lastFetchedMillis: Long = 0L) {
        fun setFetched() {
            lastFetchedMillis = System.currentTimeMillis()
        }

        fun isStale() = System.currentTimeMillis() - lastFetchedMillis > (timeoutMins * 60 * 1000)
    }

    val cacheMap: Map<Int, CacheTimestamp> = storyDataSources.mapValues { it.value.second }

    fun getStories(): List<Pair<Int, Single<List<StoryItem>>>> =
        storyDataSources.map { it.key to it.value.first.getStories() }
}
