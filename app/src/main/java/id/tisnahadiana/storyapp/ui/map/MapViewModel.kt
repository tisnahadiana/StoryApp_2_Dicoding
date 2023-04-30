package id.tisnahadiana.storyapp.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.ExperimentalPagingApi
import dagger.hilt.android.lifecycle.HiltViewModel
import id.tisnahadiana.storyapp.data.remote.responses.StoriesResponse
import id.tisnahadiana.storyapp.data.repository.StoryRepository
import id.tisnahadiana.storyapp.data.repository.UserRepository
import javax.inject.Inject

@HiltViewModel
@ExperimentalPagingApi
class MapViewModel @Inject constructor(
    private val storyRepository: StoryRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    fun getAllStoriesLocation(token: String): LiveData<Result<StoriesResponse>> =
        storyRepository.getStoryLocation(token)

    fun checkIfTokenAvailable(): LiveData<String?> = userRepository.getToken()
}