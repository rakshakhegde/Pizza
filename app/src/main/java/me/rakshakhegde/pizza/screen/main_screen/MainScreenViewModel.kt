package me.rakshakhegde.pizza.screen.main_screen

import android.databinding.ObservableArrayList
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import io.reactivex.ObservableEmitter
import io.reactivex.schedulers.Schedulers
import me.rakshakhegde.observableflow.SimpleOnObservableListChangedCallback
import me.rakshakhegde.observableflow.onPropertyChanged
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

	val filteredVariationsList = ObservableArrayList<List<Variation>>()

	val pizzaVariants = pizzaApi.getPizzaVariants()
			.subscribeOn(Schedulers.io())
			.doOnSubscribe {
				pizzaVariantsLoading.set(true)
				pizzaVariantsError.set(false)
			}
			.doFinally { pizzaVariantsLoading.set(false) }
			.doOnError {
				it.printStackTrace()
				pizzaVariantsError.set(true)
			}
			.toObservable()
			.toField()

	val variantsChosenText = ObservableField<String>()
	val variantsTotalPrice = ObservableField<String>()

	init {
		selectedPositions.addOnListChangedCallback(object : SimpleOnObservableListChangedCallback<Int, ObservableArrayList<Int>>() {

			override fun onItemRangeInserted(sender: ObservableArrayList<Int>, positionStart: Int, itemCount: Int) {
				onItemRangeChanged(sender, positionStart, itemCount)
			}

			override fun onItemRangeChanged(sender: ObservableArrayList<Int>, positionStart: Int, itemCount: Int) {
				if (selectedPositions.isEmpty())
					return

				(positionStart until selectedPositions.size).forEach {
					filterVariations(it)
				}

				variantsChosenText.set(variantsChosen())
				variantsTotalPrice.set(chosenVariantsTotalPrice())
			}
		})
		pizzaVariants.onPropertyChanged {
			filteredVariationsList.clear()
			filteredVariationsList.addAll(Array<List<Variation>?>(get().variants.variant_groups.size) { null })

			selectedPositions.clear()
			selectedPositions.addAll(Array(get().variants.variant_groups.size) { 0 })
		}
	}

	fun variantsChosen(): String? =
			(0 until selectedPositions.size).map {
				filteredVariationsList[it] ?: return null
			}
					.filter { it.isNotEmpty() }
					.zip(selectedPositions).joinToString { (variations, position) ->
				variations[position].name
			}

	fun chosenVariantsTotalPrice(): String? =
			"₹ " + (0 until selectedPositions.size).map {
				filteredVariationsList[it] ?: return null
			}
					.filter { it.isNotEmpty() }
					.zip(selectedPositions).sumBy { (variations, position) ->
				variations[position].price
			}

	fun filterVariations(position: Int) {
		val pizzaVariants = pizzaVariants.get().variants
		val variantGroup = pizzaVariants.variant_groups[position]
		val currentVariations = ArrayList(variantGroup.variations) // to create a copy

		if (position > 0) {
			var removedAny = false
			val filteredListOfExcludeLists = pizzaVariants.exclude_list.filter {
				it.any { it.group_id == variantGroup.group_id }
			}
			filteredListOfExcludeLists.forEach { listOfExcludeLists ->
				val variationIdOfCurrentGroup = listOfExcludeLists.first {
					it.group_id == variantGroup.group_id
				}.variation_id
				listOfExcludeLists.forEach { (groupId, variationId) ->

					val indexOfGroup = pizzaVariants.variant_groups.indexOfFirst {
						it.group_id == groupId
					}
					if (indexOfGroup < position &&
							filteredVariationsList[indexOfGroup]!!.indexOfFirst { it.id == variationId } ==
									selectedPositions[indexOfGroup]) {
						removedAny = removedAny or currentVariations.removeAll { it.id == variationIdOfCurrentGroup }
					}
				}
			}
			// Checking if not zero to avoid List notifyChange()
			if (removedAny && selectedPositions[position] != 0)
				selectedPositions[position] = 0
		}

		filteredVariationsList[position] = currentVariations
	}
}
