package id.tisnahadiana.storyapp.customview

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.LightingColorFilter
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import id.tisnahadiana.storyapp.R

class ButtonCustom @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatButton(context, attrs, defStyleAttr) {

    private var pressedColor: Int = Color.parseColor("#0D47A1") // Dark blue
    private var defaultColor: Int = Color.parseColor("#2196F3") // Blue
    private var cornerRadius: Float = resources.getDimension(R.dimen.button_radius)

    init {
        // Set background color and text color
        setBackgroundColor(defaultColor)
        setTextColor(Color.WHITE)

        // Set rounded corners for the button
        val backgroundDrawable = ColorDrawable(defaultColor)
        background = backgroundDrawable
        setPadding(
            cornerRadius.toInt(),
            cornerRadius.toInt(),
            cornerRadius.toInt(),
            cornerRadius.toInt()
        )

        // Set listener to animate color change on press
        setOnTouchListener { _, event ->
            when (event.action) {
                android.view.MotionEvent.ACTION_DOWN -> {
                    animateColor(pressedColor)
                }
                android.view.MotionEvent.ACTION_UP -> {
                    animateColor(defaultColor)
                }
                android.view.MotionEvent.ACTION_CANCEL -> {
                    animateColor(defaultColor)
                }
            }
            false
        }
    }

    private fun animateColor(toColor: Int) {
        val anim = ValueAnimator()
        anim.setIntValues((background as ColorDrawable).color, toColor)
        anim.setEvaluator(ArgbEvaluator())
        anim.addUpdateListener { valueAnimator ->
            val value = valueAnimator.animatedValue as Int
            val drawable: Drawable = background
            drawable.colorFilter = LightingColorFilter(value, 1)
        }
        anim.duration = 200
        anim.start()
    }
}