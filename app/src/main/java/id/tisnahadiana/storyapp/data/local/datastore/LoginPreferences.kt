package id.tisnahadiana.storyapp.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LoginPreferences @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    fun getToken(): LiveData<String?> = dataStore.data.map { it[TOKEN_KEY] }.asLiveData()

    suspend fun saveToken(token: String) { dataStore.edit { it[TOKEN_KEY] = token } }

    suspend fun deleteToken() {
        dataStore.edit {
            it[TOKEN_KEY] = "null"
        }
    }

    fun isFirstTime(): Flow<Boolean> {
        return dataStore.data.map {
            it[FIRST_TIME_KEY] ?: true
        }
    }

    suspend fun setFirstTime(firstTime: Boolean) {
        dataStore.edit {
            it[FIRST_TIME_KEY] = firstTime
        }
    }

    companion object {
        private val TOKEN_KEY = stringPreferencesKey("user_token")
        private val FIRST_TIME_KEY = booleanPreferencesKey("first_time")
    }
}