package me.rakshakhegde.pizza.network_dao

/**
 * Created by rakshak on 20/09/17.
 */
data class PizzaVariants(
		val variants: Variants
)

data class Variants(
		val variant_groups: List<VariantGroup>,
		val exclude_list: List<List<ExcludeList>>
)

data class ExcludeList(
		val group_id: String, //1
		val variation_id: String //3
)

data class VariantGroup(
		val group_id: String, //1
		val name: String, //Crust
		val variations: List<Variation>
)

data class Variation(
		val name: String, //Thin
		val price: Int, //0
		val default: Int, //1
		val id: String, //1
		val inStock: Int //1
)