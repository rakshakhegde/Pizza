package me.rakshakhegde.pizza.screen.main_screen

import android.databinding.ObservableBoolean
import io.reactivex.ObservableEmitter
import io.reactivex.schedulers.Schedulers
import me.rakshakhegde.pizza.network_dao.PizzaApi
import me.rakshakhegde.rxdatabinding.toField
import javax.inject.Inject

/**
 * Created by rakshak on 20/09/17.
 */
class MainScreenViewModel @Inject constructor(pizzaApi: PizzaApi) {

	val pizzaVariantsLoading = ObservableBoolean(true)
	val pizzaVariantsError = ObservableBoolean(false)

	var retryClick: ObservableEmitter<Int>? = null

	val pizzaVariants = pizzaApi.getPizzaVariants()
			.subscribeOn(Schedulers.io())
			.doOnSubscribe {
				pizzaVariantsLoading.set(true)
				pizzaVariantsError.set(false)
			}
			.doOnError { pizzaVariantsError.set(true) }
			.doFinally { pizzaVariantsLoading.set(false) }
			.toObservable()
			.toField()
}
