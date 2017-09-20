package me.rakshakhegde.pizza

import com.squareup.leakcanary.LeakCanary
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import me.rakshakhegde.pizza.dependencies.DaggerAppComponent


/**
 * Created by rakshak on 20/09/17.
 */
class App : DaggerApplication() {

	override fun applicationInjector(): AndroidInjector<App> = DaggerAppComponent
			.builder()
			.create(this@App)

	override fun onCreate() {
		super.onCreate()

		if (LeakCanary.isInAnalyzerProcess(applicationContext)) {
			// This process is dedicated to LeakCanary for heap analysis.
			// You should not init your app in this process.
			return
		}
		LeakCanary.install(this@App)
	}
}