package id.tisnahadiana.storyapp.ui.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import id.tisnahadiana.storyapp.R
import id.tisnahadiana.storyapp.databinding.ActivityLoginBinding
import id.tisnahadiana.storyapp.ui.main.MainActivity
import id.tisnahadiana.storyapp.ui.main.MainActivity.Companion.TOKEN
import id.tisnahadiana.storyapp.ui.register.RegisterActivity
import id.tisnahadiana.storyapp.ui.welcome.WelcomeActivity
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    private val viewModel: LoginViewModel by viewModels()
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

            if (!email.isNullOrEmpty() && !password.isNullOrEmpty()) {
                lifecycleScope.launchWhenResumed {
                    launch {
                        viewModel.userLogin(email, password)
                            .observe(this@LoginActivity) { result ->
                                result.onSuccess { credential ->
                                    credential.loginResult?.token?.let { token ->
                                        viewModel.storeAuthToken(token)
                                        showMessage(
                                            this@LoginActivity,
                                            getString(R.string.login_success)
                                        )
                                        Intent(this@LoginActivity, MainActivity::class.java).also {
                                            it.putExtra(TOKEN, token)
                                            startActivity(it)
                                            finish()
                                        }
                                    }
                                }
                                result.onFailure {
                                    showMessage(
                                        this@LoginActivity,
                                        getString(R.string.login_failed)
                                    )
                                    showLoading(false)
                                }
                            }
                    }
                }

            } else {
                showLoading(false)
                if (email.isNullOrEmpty()) binding.edLoginEmail.error =
                    getString(R.string.email_cannot_empty)
                if (password.isNullOrEmpty()) binding.edLoginPassword.error =
                    getString(R.string.password_minimum)
            }
        }

        binding.btnRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        initialCheck()
    }

    private fun initialCheck() {
        viewModel.checkIfFirstTime().observe(this) {
            if (it) {
                val intent = Intent(this, WelcomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding?.apply {
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