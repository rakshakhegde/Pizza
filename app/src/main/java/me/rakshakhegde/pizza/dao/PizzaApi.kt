package me.rakshakhegde.pizza.dao

import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET

/**
 * Created by rakshak on 20/09/17.
 */
interface PizzaApi {

	@GET("bins/wfqe9")
	fun getPizzaVariants(): Observable<Response<PizzaVariants>>
}