package homework.chegg.com.chegghomework.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import homework.chegg.com.chegghomework.data.entities.StoryItem
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

@Dao
interface StoryDao {
    @Query("SELECT * FROM stories")
    fun getAllStories(): Observable<List<StoryItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStory(story: StoryItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllStories(stories: List<StoryItem>): Completable
}
