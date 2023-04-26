package id.tisnahadiana.storyapp.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import id.tisnahadiana.storyapp.R

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
    }



    companion object {
        const val EXTRA_DETAIL = "extra_detail"
    }
}