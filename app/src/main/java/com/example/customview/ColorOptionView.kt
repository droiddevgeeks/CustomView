package com.example.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat


class ColorOptionView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attributeSet, defStyleAttr, defStyleRes) {

    private val paint = Paint()
    private var defaultColor = Color.CYAN
    private var titleText: String? = null

    init {
        setAttribute(attributeSet, defStyleAttr, defStyleRes)
    }

    /**
     * This typed array will provide access to the attribute values.
     * Notice that we use the resource name we specified in the "attrs.xml" file as ColorOptionView.
     */
    private fun setAttribute(attributeSet: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        val typeArray = context.obtainStyledAttributes(
            attributeSet,
            R.styleable.ColorOptionView, defStyleAttr, defStyleRes
        )
        titleText = typeArray.getString(R.styleable.ColorOptionView_titleText)
        defaultColor = typeArray.getColor(
            R.styleable.ColorOptionView_valueColor,
            ContextCompat.getColor(context, R.color.colorPrimary)
        )
        typeArray.recycle()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawText(canvas)
    }

    private fun drawText(canvas: Canvas?) {
        paint.color = defaultColor
        paint.textSize = 48f
        paint.style = Paint.Style.FILL_AND_STROKE
        paint.letterSpacing = 0.2f
        paint.isStrikeThruText = true
        paint.isUnderlineText = true
        canvas?.drawText(titleText!!, 16f, 50f, paint)
    }
}