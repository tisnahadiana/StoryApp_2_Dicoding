package id.tisnahadiana.storyapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.*
import id.tisnahadiana.storyapp.data.local.room.StoryDatabase
import id.tisnahadiana.storyapp.data.local.room.StoryEntity
import id.tisnahadiana.storyapp.data.local.room.StoryRemoteMediator
import id.tisnahadiana.storyapp.data.remote.responses.StoriesResponse
import id.tisnahadiana.storyapp.data.remote.retrofit.ApiService
import javax.inject.Inject

@ExperimentalPagingApi
class StoryRepository @Inject constructor(
    private val apiService: ApiService,
    private val storyDatabase: StoryDatabase
){
    fun getStory(token: String): LiveData<PagingData<StoryEntity>> =
        Pager(
            config = PagingConfig(
                pageSize = 10
            ),
            remoteMediator = StoryRemoteMediator(
                generateToken(token),
                apiService,
                storyDatabase
            ),
            pagingSourceFactory = {
                storyDatabase.storyDao().getStories()
            }
        ).liveData

    fun getStoryLocation(token: String): LiveData<Result<StoriesResponse>> = liveData {
        try {
            val bearerToken = generateToken(token)
            val response = apiService.getStories(bearerToken, size = 30, location = 1)
            emit(Result.success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.failure(e))
        }
    }
    private fun generateToken(token: String): String = "Bearer $token"
}