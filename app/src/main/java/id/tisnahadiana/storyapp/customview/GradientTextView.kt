package id.tisnahadiana.storyapp.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Shader
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import id.tisnahadiana.storyapp.R

class GradientTextView : AppCompatTextView {
    private var colourStart: Int = 0
    private var colourEnd: Int = 0
    private var width: Float = 0f
    private lateinit var gradient: LinearGradient

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }

    private fun init() {
        colourStart = ContextCompat.getColor(context, R.color.blue_255)
        colourEnd = ContextCompat.getColor(context, R.color.purple_220)
        width = this.paint.measureText(text.toString())
        gradient = LinearGradient(
            0f,
            0f,
            width,
            0f,
            intArrayOf(colourStart, colourEnd),
            null,
            Shader.TileMode.CLAMP
        )
        this.paint.shader = gradient
    }
}