package id.tisnahadiana.storyapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import id.tisnahadiana.storyapp.data.local.datastore.LoginPreferences
import id.tisnahadiana.storyapp.data.remote.responses.LoginResponse
import id.tisnahadiana.storyapp.data.remote.responses.RegisterResponse
import id.tisnahadiana.storyapp.data.remote.retrofit.ApiService
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val apiService: ApiService,
    private val loginPreferences: LoginPreferences
) {
    suspend fun userLogin(
        email: String,
        password: String
    ): LiveData<Result<LoginResponse>> = liveData {
        try {
            val response = apiService.login(email, password)
            emit(Result.success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.failure(e))
        }
    }

    suspend fun userRegister(
        name: String,
        email: String,
        password: String
    ): LiveData<Result<RegisterResponse>> = liveData {
        try {
            val response = apiService.register(name, email, password)
            emit(Result.success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.failure(e))
        }
    }

    fun getToken(): LiveData<String?> = loginPreferences.getToken()
}