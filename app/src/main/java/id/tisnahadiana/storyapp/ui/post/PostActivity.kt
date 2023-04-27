package id.tisnahadiana.storyapp.ui.post

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import id.tisnahadiana.storyapp.R
import id.tisnahadiana.storyapp.databinding.ActivityPostBinding
import id.tisnahadiana.storyapp.ui.main.MainActivity
import id.tisnahadiana.storyapp.utils.AppExecutors
import id.tisnahadiana.storyapp.utils.reduceFileImage
import id.tisnahadiana.storyapp.utils.rotateBitmap
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream

@AndroidEntryPoint
@ExperimentalPagingApi
class PostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPostBinding

    private val postViewModel: PostViewModel by viewModels()

    private val appExecutor: AppExecutors by lazy {
        AppExecutors()
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        when {
            it[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> getLocation()
            else -> binding.switchLocation.isChecked = false
        }
    }


    private var  file: File? = null
    private var isBack: Boolean = true
    private var reducingDone: Boolean = false
    private var location: Location? = null
    private var token: String = "Token Tidak Ada"
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.post)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        postViewModel.checkIfTokenAvailable().observe(this) { token ->
            this.token = token ?: "Token Tidak Ada"
            binding.tvToken.text = this.token
        }
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        bindResult()
        setupButtons()

    }

    private fun setupButtons() {
        binding.btnPost.setOnClickListener {
            postViewModel.checkIfTokenAvailable().observe(this) {
                if (reducingDone) {
                    uploadImage()
                } else {
                    Toast.makeText(
                        this,
                        getString(R.string.wait_for_processing),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        binding.switchLocation.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) getLocation()
            else location = null
        }
    }

    private fun uploadImage() {
        showLoading(true)
        if (binding.edAddDescription.text.isNullOrEmpty()) {
            Toast.makeText(this, getString(R.string.description_cannot_empty), Toast.LENGTH_SHORT)
                .show()
        } else {
            lifecycleScope.launchWhenStarted {
                launch {
                    if (file != null) {

                        var lat: RequestBody? = null
                        var lon: RequestBody? = null

                        if (location != null) {
                            lat = location?.latitude.toString()
                                .toRequestBody("text/plain".toMediaType())
                            lon = location?.longitude.toString()
                                .toRequestBody("text/plain".toMediaType())
                        }

                        binding.progressBar.visibility = View.VISIBLE
                        val description = binding.edAddDescription.text.toString()
                        postViewModel.postStory(token, file as File, description, lat, lon)
                            .observe(this@PostActivity) { result ->
                                result.onSuccess {
                                    showToast(this@PostActivity, getString(R.string.upload_success))
                                    showLoading(false)
                                    val intent = Intent(this@PostActivity, MainActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                                result.onFailure {
                                    showToast(this@PostActivity, getString(R.string.upload_failed))
                                    showLoading(false)
                                }
                            }
                    }
                }
            }
        }
    }

    private fun bindResult() {
        file = intent.getSerializableExtra(PHOTO_RESULT_EXTRA) as File
        isBack = intent.getBooleanExtra(IS_CAMERA_BACK_EXTRA, true)

        val result = rotateBitmap(BitmapFactory.decodeFile((file as File).path), isBack)
        result.compress(Bitmap.CompressFormat.JPEG, 100, FileOutputStream(file))

        appExecutor.diskIO.execute {
            file = reduceFileImage(file as File)
            reducingDone = true
        }


        binding.imgStory.setImageBitmap(result)
    }

    private fun getLocation() {
        if (
            ContextCompat.checkSelfPermission(
                this@PostActivity, Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                if (it != null) location = it
                else {
                    showToast(this@PostActivity, getString(R.string.please_active_location))
                    binding.switchLocation.isChecked = false
                }
            }
        } else requestPermissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION))
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.setVisible(isLoading)

    }

    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun View.setVisible(visible: Boolean) {
        this.visibility = if (visible) View.VISIBLE else View.GONE
    }

    companion object {
        const val PHOTO_RESULT_EXTRA = "photo_result_extra"
        const val IS_CAMERA_BACK_EXTRA = "is_camera_back_extra"
    }
}