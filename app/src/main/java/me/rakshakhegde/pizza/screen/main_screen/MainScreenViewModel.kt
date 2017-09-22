package me.rakshakhegde.pizza.screen.main_screen

import android.databinding.ObservableBoolean
import io.reactivex.schedulers.Schedulers
import me.rakshakhegde.pizza.dao.PizzaApi
import me.rakshakhegde.rxdatabinding.toField
import javax.inject.Inject

/**
 * Created by rakshak on 20/09/17.
 */
class MainScreenViewModel @Inject constructor(pizzaApi: PizzaApi) {

	val pizzaVariantsLoading = ObservableBoolean(true)
	val pizzaVariantsError = ObservableBoolean(false)

	val pizzaVariants = pizzaApi.getPizzaVariants()
			.subscribeOn(Schedulers.io())
			.doOnError { throwable ->
				pizzaVariantsError.set(true)
			}
			.doFinally { pizzaVariantsLoading.set(false) }
			.toObservable()
			.toField()
}
