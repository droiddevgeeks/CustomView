package com.example.customview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.math.min

/**
 * https://www.raywenderlich.com/142-android-custom-view-tutorial
 */
class EmotionalFaceView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attributeSet, defStyleAttr, defStyleRes) {


    companion object {
        private const val DEFAULT_FACE_COLOR = Color.YELLOW
        private const val DEFAULT_EYES_COLOR = Color.BLACK
        private const val DEFAULT_MOUTH_COLOR = Color.BLACK
        private const val DEFAULT_BORDER_COLOR = Color.BLACK
        private const val DEFAULT_BORDER_WIDTH = 4.0f

        const val HAPPY = 0L
        const val SAD = 1L
    }

    // Some colors for the face background, eyes and mouth.
    private var faceColor = DEFAULT_FACE_COLOR
    private var eyesColor = DEFAULT_EYES_COLOR
    private var mouthColor = DEFAULT_MOUTH_COLOR
    private var borderColor = DEFAULT_BORDER_COLOR
    // Face border width in pixels
    private var borderWidth = DEFAULT_BORDER_WIDTH
    // View size in pixels
    private var size = 0

    private val paint = Paint()
    private val mouthPath = Path()

    var happinessState = HAPPY
        set(state) {
            field = state
            invalidate()
        }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawFaceBg(canvas)
        drawEyes(canvas)
        drawMouth(canvas)
    }

    init {
        paint.isAntiAlias = true
        setUpAttribute(attributeSet)
    }

    private fun setUpAttribute(attributeSet: AttributeSet?) {
        val typedArray =
            context.theme.obtainStyledAttributes(attributeSet, R.styleable.EmotionalFaceView, 0, 0)

        happinessState =
            typedArray.getInt(R.styleable.EmotionalFaceView_state, HAPPY.toInt()).toLong()
        faceColor = typedArray.getColor(R.styleable.EmotionalFaceView_faceColor, DEFAULT_FACE_COLOR)
        eyesColor = typedArray.getColor(R.styleable.EmotionalFaceView_eyesColor, DEFAULT_EYES_COLOR)
        mouthColor =
            typedArray.getColor(R.styleable.EmotionalFaceView_mouthColor, DEFAULT_MOUTH_COLOR)
        borderColor =
            typedArray.getColor(R.styleable.EmotionalFaceView_borderColor, DEFAULT_BORDER_COLOR)
        borderWidth =
            typedArray.getDimension(R.styleable.EmotionalFaceView_borderWidth, DEFAULT_BORDER_WIDTH)
        typedArray.recycle()
    }

    private fun drawFaceBg(canvas: Canvas?) {
        paint.color = faceColor
        paint.style = Paint.Style.FILL
        val radius = size / 2f
        canvas?.let {
            it.drawCircle(size / 2f, size / 2f, radius, paint)
            paint.color = borderColor
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = borderWidth
            it.drawCircle(size / 2f, size / 2f, radius - borderWidth / 2f, paint)
        }
    }

    private fun drawEyes(canvas: Canvas?) {
        paint.color = eyesColor
        paint.style = Paint.Style.FILL
        canvas?.let {
            val leftEye = RectF(size * .32f, size * 0.23f, size * 0.43f, size * 0.50f)
            it.drawOval(leftEye, paint)
            val rightEyeRect = RectF(size * 0.57f, size * 0.23f, size * 0.68f, size * 0.50f)
            it.drawOval(rightEyeRect, paint)
        }
    }


    private fun drawMouth(canvas: Canvas?) {
        mouthPath.reset()

        mouthPath.moveTo(size * 0.22f, size * 0.7f)

        if (happinessState == HAPPY) {
            // Happy mouth path
            mouthPath.quadTo(size * 0.5f, size * 0.80f, size * 0.78f, size * 0.7f)
            mouthPath.quadTo(size * 0.5f, size * 0.90f, size * 0.22f, size * 0.7f)
        } else {
            // Sad mouth path
            mouthPath.quadTo(size * 0.5f, size * 0.50f, size * 0.78f, size * 0.7f)
            mouthPath.quadTo(size * 0.5f, size * 0.60f, size * 0.22f, size * 0.7f)
        }

        paint.color = mouthColor
        paint.style = Paint.Style.FILL
        canvas?.let {
            it.drawPath(mouthPath, paint)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        size = min(measuredWidth, measuredHeight)
        setMeasuredDimension(size, size)
    }

}