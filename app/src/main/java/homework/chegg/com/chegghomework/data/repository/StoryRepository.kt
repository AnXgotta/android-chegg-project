package homework.chegg.com.chegghomework.data.repository

import homework.chegg.com.chegghomework.data.entities.StoryItem
import homework.chegg.com.chegghomework.data.local.StoryDao
import homework.chegg.com.chegghomework.data.network.StoryDataSource
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.core.Single.zip
import io.reactivex.rxjava3.internal.operators.single.SingleJust
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class StoryRepository @Inject constructor(
    private val storyDataSource: StoryDataSource,
    private val localDataSource: StoryDao
) {
    /**
     *  - Could add "per source" cache fetching using id from source
     *  - Could add different loading indicator for network vs cache so user doesn't get a jarring data update
     *    - Split calls into two: Cache and Network -> then the view would know more
     *    - This is more of a SSOT implementation
     *    - Could wrap in a "DataResource" like object to pass status data from here
     */
    fun getStories(): Single<List<StoryItem>> {
        // get remote calls with stale cache
        val remoteCalls =
            storyDataSource.getStories()
                .filter { storyDataSource.cacheMap[it.first]!!.isStale() }

        // if no remote calls, use cache
        return if (remoteCalls.isEmpty()) {
            localDataSource.getAllStories()
                .firstOrError()
                .onErrorResumeNext { SingleJust(emptyList()) }
        } else {
            // zip remote calls, update local cache, update cache status in storyDataSource
            val zRemoteCall =
                zip(remoteCalls.map { it.second }.asIterable()) { arr: Array<out Any> ->
                    arr.map { it as List<StoryItem> }.reduce { acc, list ->
                        acc + list
                    }
                }.doAfterSuccess { remoteData ->
                    localDataSource.insertAllStories(remoteData).subscribe()
                    remoteCalls.forEach { storyDataSource.cacheMap[it.first]!!.setFetched() }
                }

            return localDataSource.getAllStories()
                .firstOrError()
                .onErrorResumeNext { SingleJust(emptyList()) }
                .concatMap {
                    zRemoteCall
                }
        }.delay(1, TimeUnit.SECONDS)
    }
}
