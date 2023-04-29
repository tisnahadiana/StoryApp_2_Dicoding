package id.tisnahadiana.storyapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import id.tisnahadiana.storyapp.data.local.datastore.LoginPreferences
import id.tisnahadiana.storyapp.data.local.room.StoryEntity
import id.tisnahadiana.storyapp.data.repository.StoryRepository
import id.tisnahadiana.storyapp.data.repository.UserRepository
import javax.inject.Inject

@HiltViewModel
@ExperimentalPagingApi
class HomeViewModel @Inject constructor(
    private val storyRepository: StoryRepository,
    private val userRepository: UserRepository,
    private val loginPreferences: LoginPreferences,
): ViewModel() {

    fun getStory(token: String): LiveData<PagingData<StoryEntity>> =
        storyRepository.getStory(token).cachedIn(viewModelScope)
    fun checkIfTokenAvailable(): LiveData<String?> = userRepository.getToken()

    fun checkIfFirstTime(): LiveData<Boolean> {
        return loginPreferences.isFirstTime().asLiveData()
    }


}