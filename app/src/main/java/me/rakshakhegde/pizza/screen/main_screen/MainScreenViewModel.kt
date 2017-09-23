package me.rakshakhegde.pizza.screen.main_screen

import android.databinding.ObservableArrayList
import android.databinding.ObservableBoolean
import io.reactivex.ObservableEmitter
import io.reactivex.schedulers.Schedulers
import me.rakshakhegde.pizza.network_dao.PizzaApi
import me.rakshakhegde.pizza.network_dao.VariantGroup
import me.rakshakhegde.rxdatabinding.toField
import javax.inject.Inject

/**
 * Created by rakshak on 20/09/17.
 */
class MainScreenViewModel @Inject constructor(pizzaApi: PizzaApi) {

	val pizzaVariantsLoading = ObservableBoolean(true)
	val pizzaVariantsError = ObservableBoolean(false)

	var retryClick: ObservableEmitter<Int>? = null

	val selectedPositions = ObservableArrayList<Int>()

	val pizzaVariants = pizzaApi.getPizzaVariants()
			.subscribeOn(Schedulers.io())
			.doOnSubscribe {
				pizzaVariantsLoading.set(true)
				pizzaVariantsError.set(false)
			}
			.doOnError { pizzaVariantsError.set(true) }
			.doFinally { pizzaVariantsLoading.set(false) }
			.doOnSuccess { pizzaVariants ->
				selectedPositions.clear()
				selectedPositions.addAll(Array(pizzaVariants.variants.variant_groups.size) { 0 })
			}
			.toObservable()
			.toField()

	fun variantsChosen(variantGroups: List<VariantGroup>?, selectedPositions: List<Int>): String? =
			variantGroups?.zip(selectedPositions)?.joinToString { (group, position) ->
				group.variations[position].name
			}

	fun chosenVariantsTotalPrice(variantGroups: List<VariantGroup>?, selectedPositions: List<Int>): String? =
			if (variantGroups == null || variantGroups.isEmpty())
				null
			else "₹ " + variantGroups.zip(selectedPositions).sumBy { (group, position) ->
				group.variations[position].price
			}
}
