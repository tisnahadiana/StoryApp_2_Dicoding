package id.tisnahadiana.storyapp.data.remote.responses

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class StoriesResponse(
    @field:SerializedName("listStory")
    val listStory: List<StoryEntity>,

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)

data class StoryEntity(

    @field:SerializedName("photoUrl")
    val photoUrl: String,

    @field:SerializedName("createdAt")
    val createdAt: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("lon")
    val lon: Double?,

    @field:SerializedName("lat")
    val lat: Double?,

    @field:SerializedName("id")
    val id: String,

    ) : Serializable