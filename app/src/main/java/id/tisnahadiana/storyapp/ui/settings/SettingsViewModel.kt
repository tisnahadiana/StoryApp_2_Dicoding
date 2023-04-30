package id.tisnahadiana.storyapp.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.tisnahadiana.storyapp.data.local.datastore.LoginPreferences
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val loginPreferences: LoginPreferences
) : ViewModel() {
    fun logout() {
        viewModelScope.launch { loginPreferences.deleteToken() }
    }
}