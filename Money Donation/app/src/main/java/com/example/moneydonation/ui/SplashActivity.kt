package com.example.moneydonation.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import com.example.moneydonation.R

class SplashActivity : ComponentActivity() {

	private val splashTimeOut: Long = 3000 // 3 seconds

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_splash)

		Handler(Looper.getMainLooper()).postDelayed({
			val intent = Intent(this, LoginActivity::class.java)
			startActivity(intent)
			finish()
		}, splashTimeOut)
	}
}