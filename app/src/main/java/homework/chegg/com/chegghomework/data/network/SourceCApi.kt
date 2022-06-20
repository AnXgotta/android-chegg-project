package homework.chegg.com.chegghomework.data.network

import homework.chegg.com.chegghomework.Consts
import homework.chegg.com.chegghomework.data.entities.StoryItem
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Headers

interface SourceCApi : IStorySource {
    @Headers("mock: true")
    @GET(Consts.DATA_SOURCE_C_URL)
    override fun getStories(): Single<List<StoryItem>>
}
