package id.tisnahadiana.storyapp.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LoginPreferences @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    private val token = stringPreferencesKey("token")
    private val firstTime = booleanPreferencesKey("first_time")

    fun getToken(): LiveData<String?> =
        dataStore.data.map {
            it[token] ?: "null"
        }.asLiveData()

    fun isFirstTime(): Flow<Boolean> {
        return dataStore.data.map {
            it[firstTime] ?: true
        }
    }

    suspend fun saveToken(token: String) {
        dataStore.edit { it[this.token] = token }
    }

    suspend fun setFirstTime(firstTime: Boolean) {
        dataStore.edit {
            it[this.firstTime] = firstTime
        }
    }

    suspend fun deleteToken() {
        dataStore.edit {
            it[token] = "null"
        }
    }

}