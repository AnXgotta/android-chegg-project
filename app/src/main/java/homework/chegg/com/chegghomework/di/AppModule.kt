package homework.chegg.com.chegghomework.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.reflect.TypeToken
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import homework.chegg.com.chegghomework.Consts
import homework.chegg.com.chegghomework.data.entities.StoryItem
import homework.chegg.com.chegghomework.data.local.AppDatabase
import homework.chegg.com.chegghomework.data.local.StoryDao
import homework.chegg.com.chegghomework.data.network.*
import homework.chegg.com.chegghomework.data.remote.SourceADeserializer
import homework.chegg.com.chegghomework.data.remote.SourceBDeserializer
import homework.chegg.com.chegghomework.data.remote.SourceCDeserializer
import homework.chegg.com.chegghomework.data.repository.StoryRepository
import homework.chegg.com.chegghomework.util.MockRequestInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideOkHttpClient(@ApplicationContext appContext: Context): OkHttpClient =
        OkHttpClient.Builder().addInterceptor(MockRequestInterceptor(appContext)).build()

    /*
    All datasources have the same "Base Url" but unique deserializers
    if we are fetching data from different sources we would need separate
    retrofit providers for (possibly) base urls and deserializers
    */
    @Singleton
    @Provides
    @Named("SourceA")
    fun provideRetrofitDatasourceA(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(
                GsonConverterFactory.create(
                    getGsonWithDeserializer(object : TypeToken<MutableList<StoryItem>>() {}.type, SourceADeserializer())
                )
            )
            .baseUrl(Consts.BASE_URL).client(okHttpClient)
            .build()

    @Singleton
    @Provides
    @Named("SourceB")
    fun provideRetrofitDatasourceB(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(
                GsonConverterFactory.create(
                    getGsonWithDeserializer(object : TypeToken<MutableList<StoryItem>>() {}.type, SourceBDeserializer())
                )
            )
            .baseUrl(Consts.BASE_URL).client(okHttpClient)
            .build()

    @Singleton
    @Provides
    @Named("SourceC")
    fun provideRetrofitDatasourceC(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(
                GsonConverterFactory.create(
                    getGsonWithDeserializer(object : TypeToken<MutableList<StoryItem>>() {}.type, SourceCDeserializer())
                )
            )
            .baseUrl(Consts.BASE_URL).client(okHttpClient)
            .build()

    @Singleton
    @Provides
    fun provideSourceAService(@Named("SourceA") retrofit: Retrofit): SourceAApi =
        retrofit.create(SourceAApi::class.java)

    @Singleton
    @Provides
    fun provideSourceBService(@Named("SourceB") retrofit: Retrofit): SourceBApi =
        retrofit.create(SourceBApi::class.java)

    @Singleton
    @Provides
    fun provideSourceCService(@Named("SourceC") retrofit: Retrofit): SourceCApi =
        retrofit.create(SourceCApi::class.java)

    @Singleton
    @Provides
    fun provideStoryDataSource(
        sourceAApi: SourceAApi,
        sourceBApi: SourceBApi,
        sourceCApi: SourceCApi
    ): StoryDataSource {
        // not a huge fan of this solution, but it could definitely be iterated on to improve
        // come back if time - magic numbers (id and cache time)
        val sourceMap = mapOf<Int, Pair<IStorySource, StoryDataSource.CacheTimestamp>>(
            0 to (sourceAApi to StoryDataSource.CacheTimestamp(5L)),
            1 to (sourceBApi to StoryDataSource.CacheTimestamp(30L)),
            2 to (sourceCApi to StoryDataSource.CacheTimestamp(60L)),
        )

        return StoryDataSource(sourceMap)
    }

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) =
        AppDatabase.getDatabase(appContext)

    @Singleton
    @Provides
    fun provideStoryDao(db: AppDatabase) = db.storyDao()

    @Singleton
    @Provides
    fun provideStoryRepository(
        storyDataSource: StoryDataSource,
        localDataSource: StoryDao
    ) = StoryRepository(storyDataSource, localDataSource)

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    private fun <T : Any> getGsonWithDeserializer(type: Type, deserializer: JsonDeserializer<T>) =
        GsonBuilder().registerTypeAdapter(type, deserializer).create()
}
