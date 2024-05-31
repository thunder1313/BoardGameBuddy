package com.example.boardgamebuddy.UI

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.boardgamebuddy.R
import com.example.boardgamebuddy.ui.MainActivity

class StartActivity : AppCompatActivity() {

    // Define times for animation and starting main activity
    companion object {
        private const val ANIMATION_DELAY = 1000L
        private const val ANIMATION_DURATION = 2000L
        private const val TOTAL_DELAY = 3200L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        // create a Handler to start animation and go to main activity with specific delays
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            animation()
        }, ANIMATION_DELAY)

        handler.postDelayed({
            startMainActivity()
        }, TOTAL_DELAY)
    }

    private fun animation() {
        val animationView = findViewById<ImageView>(R.id.start_animation)
        animationView.visibility = View.VISIBLE

        val spinAnimator = ObjectAnimator.ofFloat(animationView, "rotation", 0f, 360f).apply {
            duration = ANIMATION_DURATION
            repeatCount = 1
            interpolator = AccelerateInterpolator()
        }

        val scaleAnimatorX = ObjectAnimator.ofFloat(animationView, "scaleX", 1f, 0f).apply {
            duration = ANIMATION_DURATION
            interpolator = AccelerateInterpolator()
        }

        val scaleAnimatorY = ObjectAnimator.ofFloat(animationView, "scaleY", 1f, 0f).apply {
            duration = ANIMATION_DURATION
            interpolator = AccelerateInterpolator()
        }

        AnimatorSet().apply {
            playTogether(spinAnimator, scaleAnimatorX, scaleAnimatorY)
            start()
        }
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
