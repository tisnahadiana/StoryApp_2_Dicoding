package id.tisnahadiana.storyapp.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import id.tisnahadiana.storyapp.R
import id.tisnahadiana.storyapp.data.local.room.StoryEntity
import id.tisnahadiana.storyapp.databinding.ActivityDetailBinding

@AndroidEntryPoint
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
        val story = intent.getParcelableExtra<StoryEntity>(EXTRA_DETAIL)
        val name = story?.name
        val description = story?.description
        val imgUrl = story?.photoUrl

        supportActionBar?.title = name
        supportActionBar?.setDisplayShowHomeEnabled(true)

        binding.tvDetailDescription.text = description

        Glide.with(this)
            .load(imgUrl)
            .placeholder(R.drawable.image)
            .error(R.drawable.image_error)
            .into(binding.ivDetailPhoto)
    }

    companion object {
        const val EXTRA_DETAIL = "extra_detail"
    }
}