package me.rakshakhegde.pizza.screen.main_screen

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.animation.AnimationUtils
import dagger.android.AndroidInjection
import me.rakshakhegde.pizza.BR
import me.rakshakhegde.pizza.R
import me.rakshakhegde.pizza.databinding.ActivityMainBinding
import me.rakshakhegde.pizza.network_dao.VariantGroup
import me.rakshakhegde.pizza.network_dao.Variation
import me.rakshakhegde.pizza.network_dao.hashCodeId
import me.tatarka.bindingcollectionadapter2.BindingListViewAdapter
import me.tatarka.bindingcollectionadapter2.ItemBinding
import me.tatarka.bindingcollectionadapter2.OnItemBind
import org.jetbrains.anko.act
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

	val pizzaListItemBinding = OnItemBind<VariantGroup> { itemBinding, position, item ->
		itemBinding.set(BR.variantGroup, R.layout.pizza_variant_row)
				.bindExtra(BR.V, this@MainActivity)
				.bindExtra(BR.position, position)
	}

	val spinnerVariantItemBinding = ItemBinding.of<Variation>(BR.variation, R.layout.variation_spinner_dropdown_item)
	val spinnerItemIds = BindingListViewAdapter.ItemIds<Variation> { position, variation ->
		variation.hashCodeId
	}

	val binding by lazy {
		DataBindingUtil.setContentView<ActivityMainBinding>(act, R.layout.activity_main)
	}

	val bounceAnim by lazy {
		AnimationUtils.loadAnimation(applicationContext, R.anim.notify_bounce)
	}

	@Inject
	lateinit var VM: MainScreenViewModel

	override fun onCreate(savedInstanceState: Bundle?) {
		AndroidInjection.inject(act)
		super.onCreate(savedInstanceState)

		binding.v = this@MainActivity

		binding.retryButton.setOnClickListener {
			VM.retryClick!!.onNext(0)
		}
	}

	fun bounceAddItemBar(vararg objs: Any?): Int {
		binding.addItemBar.startAnimation(bounceAnim)
		return 0
	}
}
