package id.tisnahadiana.storyapp.ui.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import id.tisnahadiana.storyapp.R
import id.tisnahadiana.storyapp.databinding.ActivityLoginBinding
import id.tisnahadiana.storyapp.ui.main.MainActivity
import id.tisnahadiana.storyapp.ui.register.RegisterActivity
import id.tisnahadiana.storyapp.ui.welcome.WelcomeActivity
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    private val loginViewModel: LoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupView()
    }

    private fun setupView() {
        supportActionBar?.title = getString(R.string.login)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        binding.btnLogin.setOnClickListener {
            showLoading(true)

            val email = binding.edLoginEmail.text.toString().trim()
            val password = binding.edLoginPassword.text.toString().trim()

            when {
                email.isEmpty() ->
                    showInputError(binding.edLoginEmail, getString(R.string.empty_field, getString(R.string.email)))
                !Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
                    showInputError(binding.edLoginEmail, getString(R.string.error_email))
                password.isEmpty() ->
                    showInputError(binding.edLoginPassword, getString(R.string.empty_field, getString(R.string.password)))
                password.length < 6 ->
                    showInputError(binding.edLoginPassword, getString(R.string.error_password))
                else ->
                    lifecycleScope.launchWhenResumed {
                        launch {
                            loginViewModel.userLogin(email, password).observe(this@LoginActivity) { result ->
                                result.onSuccess { credential ->
                                    credential.loginResult?.token?.let { token ->
                                        loginViewModel.storeAuthToken(token)
                                        showLoading(false)
                                        Intent(this@LoginActivity, MainActivity::class.java).also {
                                            startActivity(it)
                                            finish()
                                        }
                                    }
                                }
                                result.onFailure {
                                    showToast(this@LoginActivity, getString(R.string.login_failed_message))
                                    showLoading(false)
                                }
                            }
                        }
                    }
            }
        }

        binding.btnRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showInputError(editText: EditText?, message: String) {
        showLoading(false)
        editText?.error = message
        showToast(this@LoginActivity, message)
    }
    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        initialCheck()
    }

    private fun initialCheck() {
        loginViewModel.checkIfFirstTime().observe(this) {
            if (it) {
                val intent = Intent(this, WelcomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.apply {
            edLoginEmail.isEnabled = !isLoading
            edLoginPassword.isEnabled = !isLoading
            btnRegister.isEnabled = !isLoading
            btnLogin.isEnabled = !isLoading

            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun showMessage(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}