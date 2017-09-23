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
import me.tatarka.bindingcollectionadapter2.BindingListViewAdapter
import me.tatarka.bindingcollectionadapter2.ItemBinding
import org.jetbrains.anko.act
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

	val pizzaListItemBinding = ItemBinding.of<VariantGroup>(BR.variantGroup, R.layout.pizza_variant_row)
			.bindExtra(BR.V, this@MainActivity)

	val spinnerVariantItemBinding = ItemBinding.of<Variation>(BR.variation, R.layout.variation_spinner_dropdown_item)
	val spinnerItemIds = BindingListViewAdapter.ItemIds<Variation> { position, item ->
		item.name.hashCode().toLong()
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
