package id.tisnahadiana.storyapp.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class LoginPreferences @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    suspend fun saveToken(token: String) {
        dataStore.edit {
            it[TOKEN_KEY] = token
        }
    }

    suspend fun deleteToken() {
        dataStore.edit {
            it[TOKEN_KEY] = "null"
        }
    }

    fun getToken(): LiveData<String?> = dataStore.data.map { it[TOKEN_KEY] ?: "null"}.asLiveData()

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
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val FIRST_TIME_KEY = booleanPreferencesKey("first_time")
    }
}