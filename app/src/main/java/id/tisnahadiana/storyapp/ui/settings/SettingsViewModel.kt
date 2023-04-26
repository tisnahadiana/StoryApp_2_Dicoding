package id.tisnahadiana.storyapp.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.tisnahadiana.storyapp.data.local.datastore.LoginPreferences
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    private val loginPreferences: LoginPreferences
): ViewModel() {
    fun logout() {
        viewModelScope.launch { loginPreferences.deleteToken() }
    }
}