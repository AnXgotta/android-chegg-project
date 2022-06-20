package homework.chegg.com.chegghomework.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stories")
data class StoryItem(
    @PrimaryKey var id: Int,
    val title: String,
    val subtitle: String,
    val imageUrl: String
)
