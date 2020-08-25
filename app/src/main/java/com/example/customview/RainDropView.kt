package com.example.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 * https://medium.com/mobile-app-development-publication/custom-touchable-animated-view-in-kotlin-3ad599f85dbc
 */

class RainDropView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    private val paint = Paint()
        .apply { color = Color.BLACK }
        .apply { style = Paint.Style.STROKE }
        .apply { isAntiAlias = true }
        .apply { strokeWidth = 2.0f }

    var rainDropList: MutableList<RainDrop> = mutableListOf()
    private val maxRadius by lazy {
        context.resources.getDimension(R.dimen.max_radius)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val desiredWidth = suggestedMinimumWidth + paddingLeft + paddingRight
        val desiredHeight = suggestedMinimumHeight + paddingTop + paddingBottom
        val resolvedWidth = resolveSize(desiredWidth, widthMeasureSpec)
        val resolvedHeight = resolveSize(desiredHeight, heightMeasureSpec)
        setMeasuredDimension(resolvedWidth, resolvedHeight)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        rainDropList.forEach { rainDrop ->
            canvas?.let { mCanvas -> rainDrop.drawRainDrop(mCanvas, paint) }
        }
        rainDropList = rainDropList.filter { it.isValid() }.toMutableList()
        if (rainDropList.isNotEmpty() && isAttachedToWindow) invalidate()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val pointerIndex = event?.actionIndex
        when (event?.actionMasked) {
            MotionEvent.ACTION_DOWN,
            MotionEvent.ACTION_POINTER_DOWN -> return true
            MotionEvent.ACTION_UP,
            MotionEvent.ACTION_POINTER_UP -> {
                rainDropList.add(
                    RainDrop(
                        event.getX(pointerIndex!!),
                        event.getY(pointerIndex),
                        maxRadius
                    )
                )
                invalidate()
                return true
            }
        }
        return super.onTouchEvent(event)

    }
}

data class RainDrop(
    private val centerX: Float,
    private val centerY: Float,
    private val maxRadius: Float
) {
    private var currentRadius = 1f
    private val MAX_ALPHA = 255

    fun drawRainDrop(canvas: Canvas, paint: Paint) {
        paint.apply {
            alpha = ((maxRadius - currentRadius) / maxRadius * MAX_ALPHA).toInt()
        }
        canvas.drawCircle(centerX, centerY, currentRadius++, paint)
    }

    fun isValid() = currentRadius < maxRadius
}