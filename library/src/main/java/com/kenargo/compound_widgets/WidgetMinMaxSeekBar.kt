package com.kenargo.compound_widgets

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.*
import android.view.View.OnClickListener
import android.view.animation.DecelerateInterpolator
import android.widget.*
import com.kenargo.myapplicationlibrary.R
import kotlinx.android.synthetic.main.widget_min_max_seek_bar.view.*
import kotlin.math.abs

class WidgetMinMaxSeekBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val defaultMaximum = 100
    private val defaultMinimum = 0
    private val animationDuration = 250

    private val defaultInitialTouchTime = 100
    private val defaultRepeatDelayTime = 400
    private val secondRepeatDelay = 200

    init {
        initSubView(context, attrs!!, defStyleAttr)
    }

    var animateChanges = true

    private var minValue = 0
    private var maxValue = 100

    private var progress = 0

    fun getProgress(): Int {
        return progress
    }

    @JvmOverloads fun setProgress(value: Int, immediate: Boolean = false) {
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
        return maxValue
    }

    fun setMaxValue(value: Int) {
        setSeekBarRange(minValue, value)
    }

    fun getMaxValue(): Int {
        return minValue
    }

    // Match the callback from SeekBar so I can maintain code compatibility.
    // Information: SeekBar also has a onStartTrackingTouch(SeekBar seekBar) and onStopTrackingTouch(SeekBar seekBar) but I don't need these yet
    private var seekBarChangeListener: SeekBar.OnSeekBarChangeListener? = null

    fun setOnSeekBarChangeListener(cellSeekBarListener: SeekBar.OnSeekBarChangeListener?) {
        seekBarChangeListener = cellSeekBarListener
    }

    private fun initSubView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        LayoutInflater.from(context).inflate(R.layout.widget_min_max_seek_bar, this, true)

        applyAttributes(context, attrs, defStyleAttr)

        // In edit mode I don't need handlers and callbacks
        if (isInEditMode) {
            return
        }

        imageViewWidgetMinMaxSeekBarDecrease.setOnTouchListener(
            RepeatListener(defaultInitialTouchTime, defaultRepeatDelayTime, OnClickListener {
                setProgress(getProgress() - 1)
            })
        )

        imageViewWidgetMinMaxSeekBarIncrease.setOnTouchListener(
            RepeatListener(defaultInitialTouchTime, defaultRepeatDelayTime, OnClickListener {
                setProgress(getProgress() + 1)
            })
        )

        seekBarWidgetMinMaxSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

                this@WidgetMinMaxSeekBar.progress = userValueToProgress(progress)

                seekBarChangeListener?.onProgressChanged(
                    seekBar, this@WidgetMinMaxSeekBar.progress, fromUser
                )

                updateIncreaseDecreaseButtons()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                progress = userValueToProgress(seekBar!!.progress)
                seekBarChangeListener?.onStartTrackingTouch(seekBar)
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                progress = userValueToProgress(seekBar!!.progress)
                seekBarChangeListener?.onStopTrackingTouch(seekBar)
            }
        })
    }

    private fun updateIncreaseDecreaseButtons() {
        imageViewWidgetMinMaxSeekBarIncrease.isEnabled = (getProgress() != getMinValue())
        imageViewWidgetMinMaxSeekBarDecrease.isEnabled = (getProgress() != getMaxValue())
    }

    private fun applyAttributes(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        if (attrs == null) {
            return
        }

        val typedArray = context.theme.obtainStyledAttributes(
            attrs, R.styleable.WidgetMinMaxSeekBar, defStyleAttr, 0
        )

        var showControls = true
        var setProgressValue = 0

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
                            R.styleable.WidgetMinMaxSeekBar_widgetMinMaxSeekBarEnableAnimation, true
                        )
                    }
                    R.styleable.WidgetMinMaxSeekBar_widgetMinMaxSeekBarShowControls -> {
                        showControls = typedArray.getBoolean(
                            R.styleable.WidgetMinMaxSeekBar_widgetMinMaxSeekBarShowControls, true
                        )
                    }
                    R.styleable.WidgetMinMaxSeekBar_widgetMinMaxSeekBarMinValue -> {
                        min = typedArray.getInt(
                            R.styleable.WidgetMinMaxSeekBar_widgetMinMaxSeekBarMinValue, 0
                        )
                    }
                    R.styleable.WidgetMinMaxSeekBar_widgetMinMaxSeekBarMaxValue -> {
                        max = typedArray.getInt(
                            R.styleable.WidgetMinMaxSeekBar_widgetMinMaxSeekBarMaxValue, 100
                        )
                    }
                    R.styleable.WidgetMinMaxSeekBar_android_progress -> {
                        setProgressValue = typedArray.getInt(R.styleable.WidgetMinMaxSeekBar_android_progress, 0)
                    }
                }
            }
        } finally {

            setShowControls(showControls)

            setSeekBarRange(min, max)

            setProgress(setProgressValue)

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

        if (minimumValue > maximumValue) {
            minValue = defaultMinimum
            maxValue = defaultMaximum
        } else {
            minValue = minimumValue
            maxValue = maximumValue
        }

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
        return if (maxValue < 0) {
            abs(maxValue - minValue)
        } else {
            maxValue - minValue
        }
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
}