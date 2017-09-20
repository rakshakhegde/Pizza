package me.rakshakhegde.pizza.dependencies

import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import me.rakshakhegde.pizza.App
import javax.inject.Singleton

/**
 * Created by rakshak on 20/09/17.
 */

@Singleton
@Component(modules = arrayOf(
		AndroidSupportInjectionModule::class,
		InjectorsModule::class,
		AppModule::class,
		NetworkModule::class
))
interface AppComponent : AndroidInjector<App> {

	@Component.Builder
	abstract class Builder : AndroidInjector.Builder<App>()
}