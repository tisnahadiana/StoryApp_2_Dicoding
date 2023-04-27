package id.tisnahadiana.storyapp.ui.welcome

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.tisnahadiana.storyapp.data.local.datastore.LoginPreferences
import id.tisnahadiana.storyapp.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val loginPreferences: LoginPreferences,
    private val userRepository: UserRepository
) : ViewModel() {
    fun setFirstTime(firstTime: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            loginPreferences.setFirstTime(firstTime)
        }
    }

    fun checkIfFirstTime(): LiveData<Boolean> {
        return loginPreferences.isFirstTime().asLiveData()
    }

    fun checkIfTokenAvailable(): LiveData<String?> = userRepository.getToken()

    fun getAuthToken(): LiveData<String?> = userRepository.getToken()
}