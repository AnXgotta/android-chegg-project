package homework.chegg.com.chegghomework.data.network

import homework.chegg.com.chegghomework.data.entities.StoryItem
import io.reactivex.rxjava3.core.Single

interface IStorySource {
    fun getStories(): Single<List<StoryItem>>
}
