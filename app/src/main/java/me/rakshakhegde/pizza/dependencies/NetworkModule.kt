package me.rakshakhegde.pizza.dependencies

import dagger.Module
import dagger.Provides
import me.rakshakhegde.pizza.dao.PizzaApi
import okhttp3.OkHttpClient
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

/**
 * Created by rakshak on 20/09/17.
 */

@Module
class NetworkModule {

	@Provides
	@Singleton
	fun pizzaApi(

			rxJava2CallAdapterFactory: RxJava2CallAdapterFactory,
			moshiConverterFactory: MoshiConverterFactory,
			okHttpClient: OkHttpClient

	): PizzaApi = TODO()
}