package me.rakshakhegde.pizza.screen.main_screen

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import dagger.android.AndroidInjection
import me.rakshakhegde.pizza.R
import org.jetbrains.anko.act

class MainActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		AndroidInjection.inject(act)

		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
	}
}
