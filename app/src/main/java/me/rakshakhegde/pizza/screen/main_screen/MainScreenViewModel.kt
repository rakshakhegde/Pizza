package me.rakshakhegde.pizza.screen.main_screen

import android.databinding.ObservableArrayList
import android.databinding.ObservableBoolean
import android.support.v4.util.SparseArrayCompat
import io.reactivex.ObservableEmitter
import io.reactivex.schedulers.Schedulers
import me.rakshakhegde.pizza.network_dao.PizzaApi
import me.rakshakhegde.pizza.network_dao.Variation
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

	val filteredVariationsMap = SparseArrayCompat<List<Variation>>()

	val pizzaVariants = pizzaApi.getPizzaVariants()
			.subscribeOn(Schedulers.io())
			.doOnSubscribe {
				pizzaVariantsLoading.set(true)
				pizzaVariantsError.set(false)
			}
			.doOnSuccess { pizzaVariants ->
				val variantGroupsSize = pizzaVariants.variants.variant_groups.size
				selectedPositions.clear()
				selectedPositions.addAll(Array(variantGroupsSize) { 0 })

				filteredVariationsMap.clear()
			}
			.doFinally { pizzaVariantsLoading.set(false) }
			.doOnError {
				it.printStackTrace()
				pizzaVariantsError.set(true)
			}
			.toObservable()
			.toField()

	fun variantsChosen(selectedPositions: List<Int>): String? =
			(0 until selectedPositions.size).map {
				filteredVariationsMap[it] ?: return null
			}.zip(selectedPositions).joinToString { (variations, position) ->
				variations[position].name
			}

	fun chosenVariantsTotalPrice(selectedPositions: List<Int>): String? =
			"₹ " + (0 until selectedPositions.size).map {
				filteredVariationsMap[it] ?: return null
			}.zip(selectedPositions).sumBy { (variations, position) ->
				variations[position].price
			}

	fun filterVariations(position: Int, selectedIndices: ObservableArrayList<Int>): List<Variation> {
		val pizzaVariants = pizzaVariants.get().variants
		val variantGroup = pizzaVariants.variant_groups[position]
		val currentVariations = ArrayList(variantGroup.variations) // to create a copy

		if (position == 0) {
			filteredVariationsMap.put(0, currentVariations)
			return currentVariations
		}

		val allExcludeListForThisGroup = pizzaVariants.exclude_list.filter { excludeList ->
			excludeList[1].group_id == variantGroup.group_id
		}
		allExcludeListForThisGroup.forEach { excludeList ->
			val indexOfGroup = pizzaVariants.variant_groups.indexOfFirst {
				it.group_id == excludeList[0].group_id
			}
			val variations = filteredVariationsMap[indexOfGroup]
			val selectedIndex = selectedIndices[indexOfGroup]

			if (variations[selectedIndex].id == excludeList[0].variation_id) {
				currentVariations.removeAll { it.id == excludeList[1].variation_id }
			}
		}

		filteredVariationsMap.put(position, currentVariations)
		return currentVariations
	}
}
