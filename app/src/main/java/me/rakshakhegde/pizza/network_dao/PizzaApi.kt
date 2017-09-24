package me.rakshakhegde.pizza.network_dao

import io.reactivex.Single
import retrofit2.http.GET

/**
 * Created by rakshak on 20/09/17.
 */
interface PizzaApi {

	// bins/l9295
	@GET("bins/3b0u2")
	fun getPizzaVariants(): Single<PizzaVariants>
}