package id.tisnahadiana.storyapp.data.local.room

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.io.Serializable
@Parcelize
@Entity(tableName = "story")
data class StoryEntity(
    @field:ColumnInfo(name = "photoUrl")
    val photoUrl: String,

    @field:ColumnInfo(name = "createdAt")
    val createdAt: String,

    val name: String,

    val description: String,

    val lon: Double?,

    val lat: Double?,

    @PrimaryKey
    val id: String,
) : Parcelable
