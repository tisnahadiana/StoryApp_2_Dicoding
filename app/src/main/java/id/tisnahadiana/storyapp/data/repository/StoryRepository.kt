package id.tisnahadiana.storyapp.data.repository

import androidx.paging.ExperimentalPagingApi
import id.tisnahadiana.storyapp.data.local.room.StoryDatabase
import id.tisnahadiana.storyapp.data.remote.retrofit.ApiService
import javax.inject.Inject

@ExperimentalPagingApi
class StoryRepository @Inject constructor(
    private val apiService: ApiService,
    private val storyDatabase: StoryDatabase
){

    private fun generateToken(token: String): String = "Bearer $token"
}