package me.rakshakhegde.pizza.screen.main_screen

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.rakshakhegde.pizza.dao.PizzaApi
import javax.inject.Inject

/**
 * Created by rakshak on 20/09/17.
 */
class MainScreenViewModel @Inject constructor(pizzaApi: PizzaApi) {

	init {
		pizzaApi.getPizzaVariants()
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe { pizzaVariants ->
					println("Pizza Varaiants: **** " + pizzaVariants.raw().body())
				}
	}
}