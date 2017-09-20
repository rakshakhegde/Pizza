package me.rakshakhegde.pizza.dependencies

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import me.rakshakhegde.pizza.dao.PizzaApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
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
	fun pizzaApi(): PizzaApi = Retrofit.Builder()
			.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
			.addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().build()))
			.baseUrl("https://api.myjson.com")
			.client(OkHttpClient.Builder().build())
			.build()
			.create(PizzaApi::class.java)
}