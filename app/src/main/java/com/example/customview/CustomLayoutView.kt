package com.example.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.custom_view_layout.view.*

/**
 * This is kind of compound view.
 * Compound views (also known as Compound Components ) are pre-configured ViewGroups
 * based on existing views with some predefined view interaction.
 * Compound views also allow you to add custom API to update and query the state of the compound view.
 * For such a control you define a layout file and assign it to your compound view.
 * In the implementation of your compound view you predefine the view interaction.
 * You would define a layout file and extend the corresponding ViewGroup class
 * In this class you inflate the layout file and implement the View connection logic
 * https://medium.com/mobile-app-development-publication/building-custom-component-with-kotlin-fc082678b080
 */
class CustomLayoutView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
    defStyleRes: Int = 0
) : LinearLayout(context, attrs, defStyle, defStyleRes) {

    init {
        LayoutInflater.from(context).inflate(R.layout.custom_view_layout, this, true)
        orientation = VERTICAL
        setAttribute(attrs)
    }

    private fun setAttribute(attrs: AttributeSet?) {
        val typedArray =
            context.theme.obtainStyledAttributes(attrs, R.styleable.CustomLayoutView, 0, 0)
        val title = resources.getText(
            typedArray.getResourceId(
                R.styleable.CustomLayoutView_custom_component_title,
                R.string.component_one
            )
        )

        my_title.text = title
        my_edit.hint = "${resources.getString(R.string.hint_text)} $title"

        typedArray.recycle()
    }
}