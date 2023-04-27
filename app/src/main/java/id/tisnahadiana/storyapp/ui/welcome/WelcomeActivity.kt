package id.tisnahadiana.storyapp.ui.welcome

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import id.tisnahadiana.storyapp.databinding.ActivityWelcomeBinding
import id.tisnahadiana.storyapp.ui.login.LoginActivity
import id.tisnahadiana.storyapp.ui.main.MainActivity

@AndroidEntryPoint
class WelcomeActivity : AppCompatActivity() {
    private val binding: ActivityWelcomeBinding by lazy {
        ActivityWelcomeBinding.inflate(layoutInflater)
    }

    private val welcomeViewModel: WelcomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        initialCheck()
        hideSystemUI()
        binding.btnGetStarted.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            welcomeViewModel.setFirstTime(false)
            startActivity(intent)
            finish()


        }
    }

    private fun initialCheck() {
        welcomeViewModel.checkIfFirstTime().observe(this) {
            if (it) {
                // Still in Welcome Activity
            } else{
                checkIfSessionValid()
            }
        }
    }

    private fun checkIfSessionValid() {
        welcomeViewModel.checkIfTokenAvailable().observe(this@WelcomeActivity) { token ->
            val intent = if (token.isNullOrEmpty()) {
                Intent(this@WelcomeActivity, LoginActivity::class.java)
            } else {
                Intent(this@WelcomeActivity, MainActivity::class.java)
            }
            startActivity(intent)
            finish()
        }
    }

    private fun hideSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            @Suppress("DEPRECATION")
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }
}