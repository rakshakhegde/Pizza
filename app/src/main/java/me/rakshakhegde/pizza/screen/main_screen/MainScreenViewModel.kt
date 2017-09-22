package me.rakshakhegde.pizza.screen.main_screen

import io.reactivex.schedulers.Schedulers
import me.rakshakhegde.pizza.dao.PizzaApi
import me.rakshakhegde.rxdatabinding.toField
import javax.inject.Inject

/**
 * Created by rakshak on 20/09/17.
 */
class MainScreenViewModel @Inject constructor(pizzaApi: PizzaApi) {

	val pizzaVariants = pizzaApi.getPizzaVariants()
			.subscribeOn(Schedulers.io())
			.toObservable()
			.toField()
}
