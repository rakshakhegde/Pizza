package me.rakshakhegde.pizza.screen.main_screen

import android.databinding.ObservableArrayList
import android.databinding.ObservableArrayMap
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

	val filteredVariationsMap = ObservableArrayMap<Int, List<Variation>>()

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
				(positionStart until selectedPositions.size).forEach(this@MainScreenViewModel::filterVariations)
				variantsChosenText.set(variantsChosen())
				variantsTotalPrice.set(chosenVariantsTotalPrice())
			}
		})
		pizzaVariants.onPropertyChanged {
			filteredVariationsMap.clear()

			selectedPositions.clear()
			selectedPositions.addAll(Array(get().variants.variant_groups.size) { 0 })
		}
	}

	fun variantsChosen(): String? =
			(0 until selectedPositions.size).map {
				filteredVariationsMap[it] ?: return null
			}.zip(selectedPositions).joinToString { (variations, position) ->
				variations[position].name
			}

	fun chosenVariantsTotalPrice(): String? =
			"₹ " + (0 until selectedPositions.size).map {
				filteredVariationsMap[it] ?: return null
			}.zip(selectedPositions).sumBy { (variations, position) ->
				variations[position].price
			}

	fun filterVariations(position: Int) {
		val pizzaVariants = pizzaVariants.get().variants
		val variantGroup = pizzaVariants.variant_groups[position]
		val currentVariations = ArrayList(variantGroup.variations) // to create a copy

		if (position > 0) {
			val filteredListOfExcludeLists = pizzaVariants.exclude_list.filter {
				it.any { it.group_id == variantGroup.group_id }
			}
			filteredListOfExcludeLists.forEach { listOfExcludeLists ->
				val variationIdOfThisGroup = listOfExcludeLists.first {
					it.group_id == variantGroup.group_id
				}.variation_id
				listOfExcludeLists.forEach { (groupId, variationId) ->

					val indexOfGroup = pizzaVariants.variant_groups.indexOfFirst {
						it.group_id == groupId
					}
					if (indexOfGroup < position &&
							filteredVariationsMap[indexOfGroup]!!.indexOfFirst { it.id == variationId } ==
									selectedPositions[indexOfGroup]) {
						currentVariations.removeAll { it.id == variationIdOfThisGroup }
					}
				}
			}
		}

		filteredVariationsMap.put(position, currentVariations)
	}
}
