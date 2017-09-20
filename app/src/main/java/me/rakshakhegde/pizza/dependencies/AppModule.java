package me.rakshakhegde.pizza.dependencies;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import me.rakshakhegde.pizza.App;

/**
 * Created by rakshakhegde on 16/05/17.
 */

@Module
public abstract class AppModule {

	@Singleton
	@Binds
	abstract Context appContext(App app);
}
