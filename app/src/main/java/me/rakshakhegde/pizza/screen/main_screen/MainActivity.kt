package me.rakshakhegde.pizza.screen.main_screen

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
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

	@Inject
	lateinit var VM: MainScreenViewModel

	override fun onCreate(savedInstanceState: Bundle?) {
		AndroidInjection.inject(act)
		super.onCreate(savedInstanceState)

		val binding: ActivityMainBinding = DataBindingUtil.setContentView(act, R.layout.activity_main)
		binding.v = this@MainActivity

		binding.retryButton.setOnClickListener {
			VM.retryClick!!.onNext(0)
		}
	}
}
