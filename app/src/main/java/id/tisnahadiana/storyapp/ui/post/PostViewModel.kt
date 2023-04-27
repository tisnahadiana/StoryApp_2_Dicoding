package id.tisnahadiana.storyapp.ui.post

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.ExperimentalPagingApi
import dagger.hilt.android.lifecycle.HiltViewModel
import id.tisnahadiana.storyapp.data.local.datastore.LoginPreferences
import id.tisnahadiana.storyapp.data.remote.responses.AddResponse
import id.tisnahadiana.storyapp.data.repository.StoryRepository
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
@ExperimentalPagingApi
class PostViewModel @Inject constructor(
    private val storyRepository: StoryRepository,
    private val loginPreferences: LoginPreferences
) : ViewModel() {
    suspend fun postStory(
        token: String,
        imageFile: File,
        description: String,
        lat: RequestBody?,
        lon: RequestBody?
    ) : LiveData<Result<AddResponse>> =
        storyRepository.postStory(token, imageFile, description, lat, lon)

    fun checkIfTokenAvailable(): LiveData<String?> {
        return loginPreferences.getToken()
    }

}