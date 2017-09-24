package me.rakshakhegde.pizza.screen.main_screen

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.squareup.moshi.Moshi
import io.kotlintest.matchers.shouldBe
import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import me.rakshakhegde.pizza.network_dao.PizzaApi
import me.rakshakhegde.pizza.network_dao.PizzaVariants
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Created by Rakshak.R.Hegde on 23-Sep-17.
 */
class MainScreenViewModelTest {

	val testJson = """
		{"variants":{"variant_groups":[{"group_id":"1","name":"Crust","variations":[{"name":"Thin","price":0,"default":1,"id":"1","inStock":1},{"name":"Thick","price":0,"default":0,"id":"2","inStock":1,"isVeg":1},{"name":"Cheese burst","price":100,"default":0,"id":"3","inStock":1,"isVeg":1}]},{"group_id":"2","name":"Size","variations":[{"name":"Small","price":0,"default":1,"id":"10","inStock":1,"isVeg":0},{"name":"Medium","price":100,"default":0,"id":"11","inStock":1,"isVeg":1},{"name":"Large","price":200,"default":0,"id":"12","inStock":1,"isVeg":0}]},{"group_id":"3","name":"Sauce","variations":[{"name":"Manchurian","price":20,"default":0,"id":"20","inStock":1,"isVeg":0},{"name":"Tomato","price":20,"default":0,"id":"21","inStock":1,"isVeg":1},{"name":"Mustard","price":20,"default":0,"id":"22","inStock":1,"isVeg":0}]}],"exclude_list":[[{"group_id":"1","variation_id":"3"},{"group_id":"2","variation_id":"10"}],[{"group_id":"2","variation_id":"10"},{"group_id":"3","variation_id":"20"}],[{"group_id":"1","variation_id":"2"},{"group_id":"2","variation_id":"11"},{"group_id":"3","variation_id":"21"}]]}}
		"""

	val pizzaVariants = Moshi.Builder().build().adapter(PizzaVariants::class.java).fromJson(testJson)

	@Before
	fun setUp() {
		RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
	}

	@After
	fun tearDown() {
		RxJavaPlugins.reset()
	}

	@Test
	fun verify_VM_states_pizza_variants() {
		var emitter: SingleEmitter<PizzaVariants>? = null
		val pizzaApi: PizzaApi = mock {
			on { getPizzaVariants() } doReturn Single.create { emitter = it }
		}
		val VM = MainScreenViewModel(pizzaApi)

		VM.selectedPositions.size shouldBe 0
		VM.pizzaVariants.get() shouldBe null
		VM.pizzaVariantsLoading.get() shouldBe true

		emitter!!.onSuccess(pizzaVariants)

		VM.pizzaVariants.get() shouldBe pizzaVariants
		VM.selectedPositions.size shouldBe 3
		VM.pizzaVariantsLoading.get() shouldBe false
	}

	@Test
	fun filterVariants() {
		val pizzaApi: PizzaApi = mock {
			on { getPizzaVariants() } doReturn Single.just(pizzaVariants)
		}
		val VM = MainScreenViewModel(pizzaApi)

		val variantGroups = pizzaVariants.variants.variant_groups
		val variationsOfGroup2 = variantGroups[2].variations

		VM.filteredVariationsList[2] shouldBe listOf(variationsOfGroup2[1], variationsOfGroup2[2])

		VM.selectedPositions[0] = 1

		val variationsOfGroup1 = variantGroups[1].variations
		VM.filteredVariationsList[1] shouldBe listOf(variationsOfGroup1[0], variationsOfGroup1[2])
		VM.filteredVariationsList[2] shouldBe listOf(variationsOfGroup2[2])
	}

	@Test
	fun verify_variants_chosen_text_formed_correctly() {
		val pizzaApi: PizzaApi = mock {
			on { getPizzaVariants() } doReturn Single.just(pizzaVariants)
		}
		val VM = MainScreenViewModel(pizzaApi)

		VM.variantsChosenText.get() shouldBe "Thin, Small, Tomato"
		VM.variantsTotalPrice.get() shouldBe "₹ 20"

		VM.selectedPositions[1] = 2
		VM.selectedPositions[0] = 2

		VM.variantsChosenText.get() shouldBe "Cheese burst, Medium, Manchurian"
		VM.variantsTotalPrice.get() shouldBe "₹ 220"
	}

}