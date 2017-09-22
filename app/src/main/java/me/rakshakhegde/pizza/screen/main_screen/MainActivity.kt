package me.rakshakhegde.pizza.screen.main_screen

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import dagger.android.AndroidInjection
import me.rakshakhegde.pizza.R
import me.rakshakhegde.pizza.databinding.ActivityMainBinding
import org.jetbrains.anko.act
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

	@Inject
	lateinit var VM: MainScreenViewModel

	override fun onCreate(savedInstanceState: Bundle?) {
		AndroidInjection.inject(act)
		super.onCreate(savedInstanceState)

		val binding: ActivityMainBinding = DataBindingUtil.setContentView(act, R.layout.activity_main)
		binding.v = this@MainActivity
	}
}
