package id.tisnahadiana.storyapp.ui.register

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

            showLoading(true)
            val name = binding.edRegisterName.text.toString().trim()
            val email = binding.edRegisterEmail.text.toString().trim()
            val password = binding.edRegisterPassword.text.toString().trim()

            if (!name.isNullOrEmpty() && !email.isNullOrEmpty() && !password.isNullOrEmpty()) {
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
                                }
                                result.onFailure {
                                    showMessage(
                                        this@RegisterActivity,
                                        getString(R.string.register_failed)
                                    )
                                    showLoading(false)
                                }
                            }
                    }
                }

            } else {
                showLoading(false)
                if (name.isNullOrEmpty()) binding.edRegisterName.error =
                    getString(R.string.name_cannot_empty)
                if (email.isNullOrEmpty()) binding.edRegisterEmail.error =
                    getString(R.string.email_cannot_empty)
                if (email.isNullOrEmpty()) binding.edRegisterPassword.error =
                    getString(R.string.password_minimum)
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