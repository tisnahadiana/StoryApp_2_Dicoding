package id.tisnahadiana.storyapp.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import id.tisnahadiana.storyapp.data.remote.responses.RegisterResponse
import id.tisnahadiana.storyapp.data.repository.UserRepository
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    suspend fun registerUser(
        name: String, email: String, password: String
    ): LiveData<Result<RegisterResponse>> = userRepository.userRegister(name, email, password)
}