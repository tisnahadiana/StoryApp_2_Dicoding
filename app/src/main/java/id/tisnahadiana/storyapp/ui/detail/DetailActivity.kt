package id.tisnahadiana.storyapp.ui.detail

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import id.tisnahadiana.storyapp.data.local.room.StoryEntity
import id.tisnahadiana.storyapp.databinding.ActivityDetailBinding
import java.io.Serializable

class DetailActivity : AppCompatActivity() {

    private val binding: ActivityDetailBinding by lazy {
        ActivityDetailBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupView()
    }

    private fun setupView() {
        val story = intent.getParcelableExtra<StoryEntity>(EXTRA_DETAIL)!!
        val name = story.name
        val description = story.description
        val imgUrl = story.photoUrl

        supportActionBar?.title = name
        supportActionBar?.setDisplayShowHomeEnabled(true)

        binding.tvDetailDescription.text = description

        Glide.with(this)
            .load(imgUrl)
            .into(binding.ivDetailPhoto)
    }



    companion object {
        const val EXTRA_DETAIL = "extra_detail"
    }
}

inline fun <reified T : Serializable> Intent.serializable(key: String): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializableExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") getSerializableExtra(key) as? T
}