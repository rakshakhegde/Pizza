package me.rakshakhegde.pizza.dao

import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET

/**
 * Created by rakshak on 20/09/17.
 */
interface PizzaApi {

	@GET("bins/3b0u2")
	fun getPizzaVariants(): Single<Response<PizzaVariants>>
}