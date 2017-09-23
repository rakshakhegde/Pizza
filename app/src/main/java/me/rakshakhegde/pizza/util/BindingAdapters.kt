package me.rakshakhegde.pizza.util

import android.databinding.BindingAdapter
import android.view.View

/**
 * Created by Rakshak.R.Hegde on 22-Sep-17.
 */
@BindingAdapter("visible")
fun View.visible(isVisible: Boolean) {
	visibility = if (isVisible) View.VISIBLE else View.GONE
}

@BindingAdapter("invisible")
fun View.invisible(isInvisible: Boolean) {
	visibility = if (isInvisible) View.INVISIBLE else View.VISIBLE
}

@BindingAdapter("execute")
fun View.execute(f: Any?) {
}