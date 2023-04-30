package id.tisnahadiana.storyapp.utils

import id.tisnahadiana.storyapp.data.local.room.StoryEntity
import id.tisnahadiana.storyapp.data.remote.responses.*
import id.tisnahadiana.storyapp.data.remote.responses.StoryEntity as StoryEntityResponse

object DataDummy {
    const val DUMMY_TOKEN = "auth_token"

    fun generateDummyStories(): List<StoryEntity> {
        val stories = arrayListOf<StoryEntity>()

        for (i in 0..9) {
            val story = StoryEntity(
                id = "story-bT982PN1buH0XP1H",
                name = "hadiana",
                description = "aaaaa",
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1682838273628__QKk3Btr.jpg",
                createdAt = "2023-04-30T07:04:33.632Z",
                lat = -6.828828828828828,
                lon = 107.17473270506558
            )

            stories.add(story)
        }

        return stories
    }
}