package me.rakshakhegde.pizza.dependencies

import dagger.Module
import dagger.android.ContributesAndroidInjector
import me.rakshakhegde.pizza.screen.main_screen.MainActivity

/**
 * Created by rakshak on 20/09/17.
 */

/**
 * This module contains all the binding to the sub component builders in the app
 */
@Module
abstract class InjectorsModule {

	@ContributesAndroidInjector
	abstract fun mainActivity(): MainActivity
}