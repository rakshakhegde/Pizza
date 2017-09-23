package me.rakshakhegde.pizza.screen.main_screen

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import io.kotlintest.matchers.shouldBe
import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import me.rakshakhegde.observableflow.onPropertyChanged
import me.rakshakhegde.pizza.network_dao.PizzaApi
import me.rakshakhegde.pizza.network_dao.PizzaVariants
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Created by Rakshak.R.Hegde on 23-Sep-17.
 */
class MainScreenViewModelTest {
	@Before
	fun setUp() {
		RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
	}

	@After
	fun tearDown() {
		RxJavaPlugins.reset()
	}

	@Test
	fun getPizzaVariants() {
		var emitter: SingleEmitter<PizzaVariants>? = null
		val pizzaApi: PizzaApi = mock {
			on { getPizzaVariants() } doReturn Single.create { emitter = it }
		}
		val VM = MainScreenViewModel(pizzaApi)

		VM.pizzaVariants.onPropertyChanged {}

		VM.pizzaVariantsLoading.get() shouldBe true

		emitter!!.onSuccess(mock())

		VM.pizzaVariantsLoading.get() shouldBe false
	}

}