package id.tisnahadiana.storyapp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.tisnahadiana.storyapp.data.local.datastore.LoginPreferences
import id.tisnahadiana.storyapp.data.remote.responses.LoginResponse
import id.tisnahadiana.storyapp.data.repository.UserRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel@Inject constructor(
    private val userRepository: UserRepository,
    private val loginPreferences: LoginPreferences
): ViewModel() {
    suspend fun userLogin(email: String, password: String): LiveData<Result<LoginResponse>> =
        userRepository.userLogin(email, password)

    fun storeAuthToken(token: String) {
        viewModelScope.launch {
            userRepository.saveToken(token)
        }
    }

    fun checkIfFirstTime(): LiveData<Boolean> {
        return loginPreferences.isFirstTime().asLiveData()
    }
}