package me.rakshakhegde.pizza.network_dao

import io.reactivex.Single
import retrofit2.http.GET

/**
 * Created by rakshak on 20/09/17.
 */
interface PizzaApi {

	@GET("bins/s58o1")
	fun getPizzaVariants(): Single<PizzaVariants>
}