package id.tisnahadiana.storyapp.ui.register

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import id.tisnahadiana.storyapp.R
import id.tisnahadiana.storyapp.databinding.ActivityRegisterBinding
import id.tisnahadiana.storyapp.ui.login.LoginActivity
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private val binding: ActivityRegisterBinding by lazy {
        ActivityRegisterBinding.inflate(layoutInflater)
    }

    private val registerViewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupView()
    }

    private fun setupView() {
        supportActionBar?.title = getString(R.string.register)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.btnRegister.setOnClickListener {

            val name = binding.edRegisterName.text.toString().trim()
            val email = binding.edRegisterEmail.text.toString().trim()
            val password = binding.edRegisterPassword.text.toString().trim()

            when {
                name.isEmpty() -> {
                    binding.edRegisterName.error = getString(R.string.empty_field, getString(R.string.name))
                }
                email.isEmpty() -> {
                    binding.edRegisterEmail.error = getString(R.string.empty_field, getString(R.string.email))
                }
                !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    binding.edRegisterEmail.error = getString(R.string.error_email)
                }
                password.isEmpty() -> {
                    binding.edRegisterPassword.error = getString(R.string.empty_field, getString(R.string.password))
                }
                password.length < 8 -> {
                    binding.edRegisterPassword.error = getString(R.string.error_password)
                }
                else -> {
                    showLoading(true)
                    lifecycleScope.launchWhenResumed {
                        launch {
                            registerViewModel.registerUser(name, email, password)
                                .observe(this@RegisterActivity) { result ->
                                    result.onSuccess {
                                        showMessage(
                                            this@RegisterActivity,
                                            getString(R.string.register_success)
                                        )
                                        val intent =
                                            Intent(this@RegisterActivity, LoginActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    }
                                    result.onFailure {
                                        showMessage(
                                            this@RegisterActivity,
                                            getString(R.string.register_failed)
                                        )
                                    }
                                    showLoading(false)
                                }
                        }
                    }
                }
            }
        }
        binding.btnLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding?.apply {
            edRegisterName.isEnabled = !isLoading
            edRegisterEmail.isEnabled = !isLoading
            edRegisterPassword.isEnabled = !isLoading
            btnRegister.isEnabled = !isLoading
            btnLogin.isEnabled = !isLoading

            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun showMessage(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}