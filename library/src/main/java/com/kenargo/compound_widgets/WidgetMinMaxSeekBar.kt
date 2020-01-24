package com.kenargo.compound_widgets

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import android.widget.SeekBar
import com.kenargo.myapplicationlibrary.R
import kotlinx.android.synthetic.main.widget_min_max_seek_bar.view.*
import java.lang.Integer.MIN_VALUE
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class WidgetMinMaxSeekBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val defaultMinimum = 0
    private val defaultMaximum = 100

    private val animationDuration = 250

    private val defaultInitialTouchTime = 100
    private val defaultRepeatDelayTime = 400

    var animateChanges = false

    private var minValue = defaultMinimum
    private var maxValue = defaultMaximum

    private var progress = defaultMinimum

    fun getProgress(): Int {
        return progress
    }

    @JvmOverloads fun setProgress(value: Int, immediate: Boolean = false) {

        if (value == MIN_VALUE) {
            return
        }

        var newValue = value

        if (!isInRange(newValue)) {
            if (newValue < minValue) {
                newValue = minValue
            }

            if (newValue > maxValue) {
                newValue = maxValue
            }
        }

        progress = newValue

        setRealSeekBarValue(progressToUserValue(newValue), immediate)
    }

    fun setMinValue(value: Int) {
        setSeekBarRange(value, maxValue)
    }

    fun getMinValue(): Int {
        return minValue
    }

    fun setMaxValue(value: Int) {
        setSeekBarRange(minValue, value)
    }

    fun getMaxValue(): Int {
        return maxValue
    }

    // Match the callback from SeekBar so I can maintain code compatibility.
    // Information: SeekBar also has a onStartTrackingTouch(SeekBar seekBar) and onStopTrackingTouch(SeekBar seekBar) but I don't need these yet
    private var onSeekBarChangeListener: SeekBar.OnSeekBarChangeListener? = null

    fun setOnSeekBarChangeListener(listener: SeekBar.OnSeekBarChangeListener?) {
        onSeekBarChangeListener = listener
    }

    var overrideFromUser = false

    private fun initSubView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        LayoutInflater.from(context).inflate(R.layout.widget_min_max_seek_bar, this, true)

        seekBarWidgetMinMaxSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

                this@WidgetMinMaxSeekBar.progress = userValueToProgress(progress)

                Log.d("ASDF", "seekBarWidgetMinMaxSeekBar.setOnSeekBarChangeListener: value=${this@WidgetMinMaxSeekBar.progress}, fromUser=$fromUser")

                onSeekBarChangeListener?.onProgressChanged(
                    seekBar, this@WidgetMinMaxSeekBar.progress, (fromUser or overrideFromUser)
                )

                overrideFromUser = false

                updateIncreaseDecreaseButtons()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                progress = userValueToProgress(seekBar!!.progress)
                onSeekBarChangeListener?.onStartTrackingTouch(seekBar)
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                progress = userValueToProgress(seekBar!!.progress)
                onSeekBarChangeListener?.onStopTrackingTouch(seekBar)
            }
        })

        applyAttributes(context, attrs, defStyleAttr)

        if (isInEditMode) {
            return
        }

        imageViewWidgetMinMaxSeekBarDecrease.setOnTouchListener(
            RepeatListener(defaultInitialTouchTime, defaultRepeatDelayTime, OnClickListener {
                // Using the decrease is the same as from user
                overrideFromUser = true
                setProgress(getProgress() - 1, false)
            })
        )

        imageViewWidgetMinMaxSeekBarIncrease.setOnTouchListener(
            RepeatListener(defaultInitialTouchTime, defaultRepeatDelayTime, OnClickListener {
                // Using the increase is the same as from user
                overrideFromUser = true
                setProgress(getProgress() + 1, false)
            })
        )
    }

    private fun updateIncreaseDecreaseButtons() {
        imageViewWidgetMinMaxSeekBarIncrease.isEnabled = (getProgress() != getMaxValue())
        imageViewWidgetMinMaxSeekBarDecrease.isEnabled = (getProgress() != getMinValue())
    }

    private fun applyAttributes(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {

        if (attrs == null) {
            return
        }

        val typedArray = context.theme.obtainStyledAttributes(
            attrs, R.styleable.WidgetMinMaxSeekBar, defStyleAttr, 0
        )

        var showControls = true
        var setProgressValue = defaultMinimum

        var min = defaultMinimum
        var max = defaultMaximum

        try {

            for (index in 0 until typedArray.length()) {

                val attribute = try {
                    typedArray.getIndex(index)
                } catch (ignore: Exception) {
                    continue
                }

                when (attribute) {
                    R.styleable.WidgetMinMaxSeekBar_widgetMinMaxSeekBarEnableAnimation -> {
                        animateChanges = typedArray.getBoolean(
                            R.styleable.WidgetMinMaxSeekBar_widgetMinMaxSeekBarEnableAnimation, false
                        )
                    }
                    R.styleable.WidgetMinMaxSeekBar_widgetMinMaxSeekBarShowControls -> {
                        showControls = typedArray.getBoolean(
                            R.styleable.WidgetMinMaxSeekBar_widgetMinMaxSeekBarShowControls, true
                        )
                    }
                    R.styleable.WidgetMinMaxSeekBar_widgetMinMaxSeekBarMinValue -> {
                        min = typedArray.getInt(
                            R.styleable.WidgetMinMaxSeekBar_widgetMinMaxSeekBarMinValue, defaultMinimum
                        )
                    }
                    R.styleable.WidgetMinMaxSeekBar_widgetMinMaxSeekBarMaxValue -> {
                        max = typedArray.getInt(
                            R.styleable.WidgetMinMaxSeekBar_widgetMinMaxSeekBarMaxValue, defaultMaximum
                        )
                    }
                    R.styleable.WidgetMinMaxSeekBar_android_progress -> {
                        setProgressValue = typedArray.getInt(R.styleable.WidgetMinMaxSeekBar_android_progress, defaultMinimum)
                    }
                }
            }
        } finally {

            setShowControls(showControls)

            setSeekBarRange(min, max)

            setProgress(setProgressValue, true)

            typedArray.recycle()
        }
    }

    fun setShowControls(showControls: Boolean) {

        val visibilityFlag = if (showControls) {
            View.VISIBLE
        } else {
            View.GONE
        }

        imageViewWidgetMinMaxSeekBarIncrease.visibility = visibilityFlag
        imageViewWidgetMinMaxSeekBarDecrease.visibility = visibilityFlag
    }

    private fun setRealSeekBarValue(value: Int, immediate: Boolean) {
        if (animateChanges && !immediate) {
            animateSeekBar(seekBarWidgetMinMaxSeekBar.progress, value)
        } else {
            seekBarWidgetMinMaxSeekBar.progress = value
            updateIncreaseDecreaseButtons()
        }
    }

    private fun progressToUserValue(value: Int): Int {
        return if (value < 0) {
            abs(value - minValue)
        } else {
            (value - minValue)
        }
    }

    private fun userValueToProgress(value: Int): Int {
        return (minValue + value)
    }

    fun setSeekBarRange(minimumValue: Int, maximumValue: Int) {

        minValue = min(maximumValue, minimumValue)
        maxValue = max(maximumValue, minimumValue)

        seekBarWidgetMinMaxSeekBar.max = getAdjustedValueRange()

        if (!isInRange(progress)) {

            if (progress < minValue) {
                progress = minValue
            }

            if (progress > maxValue) {
                progress = maxValue
            }

            setProgress(progress)
        }
    }

    private fun getAdjustedValueRange(): Int {
        return abs(maxValue - minValue)
    }

    private fun isInRange(value: Int): Boolean {
        return !(value < minValue || value > maxValue)
    }

    private lateinit var seekBarAnimator: ValueAnimator

    private fun animateSeekBar(startValue: Int, endValue: Int) {

        // TODO: fix things to the user doesn't see interim value changes
        if (!::seekBarAnimator.isInitialized) {

            seekBarAnimator = ValueAnimator.ofInt(startValue, endValue)
            seekBarAnimator.interpolator = DecelerateInterpolator()
            seekBarAnimator.duration = animationDuration.toLong()

            seekBarAnimator.addUpdateListener {
                seekBarWidgetMinMaxSeekBar.progress = it.animatedValue as Int
                updateIncreaseDecreaseButtons()
            }

        } else {
            if (seekBarAnimator.isRunning) {
                seekBarAnimator.cancel()
            }

            seekBarAnimator.setIntValues(startValue, endValue)
        }

        seekBarAnimator.start()
    }

    init {
        initSubView(context, attrs!!, defStyleAttr)
    }
}