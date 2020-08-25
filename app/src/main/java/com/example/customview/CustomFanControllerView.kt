package com.example.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

/**
 * https://codelabs.developers.google.com/codelabs/advanced-android-training-custom-view-from-scratch/index.html?index=..%2F..advanced-android-training#2
 */

class CustomFanControllerView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attributeSet, defStyleAttr, defStyleRes) {

    private val SELECTION_COUNT = 4
    private var mFanOnColor = Color.CYAN
    private var mFanOffColor = Color.GRAY

    private var mTextPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
        .apply { color = Color.BLACK }
        .apply { style = Paint.Style.FILL_AND_STROKE }
        .apply { textAlign = Paint.Align.CENTER }
        .apply { textSize = 40f }

    private var mDialPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        .apply { color = mFanOffColor }

    private var mWidth = 0
    private var mHeight = 0
    private var mRadius = 0f
    private var mActiveSelection = 0

    // String buffer for dial labels and float for ComputeXY result.
    private val mTempLabel = StringBuffer(8)
    private val mTempResult = FloatArray(2)

    init {
        setOnClickListener {
            mActiveSelection = (mActiveSelection + 1) % SELECTION_COUNT
            if (mActiveSelection >= 1) {
                mDialPaint.color = mFanOnColor
            } else {
                mDialPaint.color = mFanOffColor
            }
            invalidate()
        }
        setAttribute(attributeSet)
    }

    private fun setAttribute(attributeSet: AttributeSet?) {
        val typeArray = context.obtainStyledAttributes(
            attributeSet,
            R.styleable.CustomFanControllerView, 0, 0
        )
        mFanOffColor =
            typeArray.getColor(R.styleable.CustomFanControllerView_fanOffColor, mFanOffColor)
        mFanOnColor =
            typeArray.getColor(R.styleable.CustomFanControllerView_fanOnColor, mFanOnColor)
        typeArray.recycle()
    }

    /**
     * The onSizeChanged() method is called when the layout is inflated and when the view has changed.
     * Its parameters are the current width and height of the view, and the "old" (previous) width and height.
     */
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h
        mRadius = (min(mWidth, mHeight) / 2 * 0.8).toFloat()
    }

    /**
     * We must calculate the indicator position before rendering the view.
     * After adding the code to calculate the position,
     * override the onDraw() method to render the view.
     */
    private fun computeXYForPosition(pos: Int, radius: Float): FloatArray? {
        val result = mTempResult
        val startAngle = Math.PI * (9 / 8.0) // Angles are in radians.
        val angle = startAngle + pos * (Math.PI / 4)
        result[0] = (radius * cos(angle)).toFloat() + mWidth / 2
        result[1] = (radius * sin(angle)).toFloat() + mHeight / 2
        return result
    }

    /**
     *  It uses drawCircle() to draw a circle for the dial, and to draw the indicator mark.
     *  It uses drawText() to place text for labels, using a StringBuffer for the label text.
     */
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let { mCanvas ->
            mCanvas.drawCircle(mWidth.toFloat() / 2, mHeight.toFloat() / 2, mRadius, mDialPaint)
            val labelRadius = mRadius.plus(20)
            val label = mTempLabel
            for (i in 0 until SELECTION_COUNT) {
                val xyData = computeXYForPosition(i, labelRadius)
                val x = xyData!![0]
                val y = xyData[1]
                label.setLength(0)
                label.append(i)
                mCanvas.drawText(label, 0, label.length, x, y, mTextPaint)
            }
            // Draw the indicator mark.
            val markerRadius = mRadius - 35
            val xyData = computeXYForPosition(mActiveSelection, markerRadius)
            val x = xyData!![0]
            val y = xyData[1]
            mCanvas.drawCircle(x, y, 20.toFloat(), mTextPaint)
        }
    }
}