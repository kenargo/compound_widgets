package com.kenargo.compound_widgets

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import com.kenargo.myapplicationlibrary.R
import kotlinx.android.synthetic.main.widget_title_and_seekbar.view.*

class WidgetTitleAndSeekBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val defaultMaximum = 100
    private val defaultMinimum = 0

    // Match the callback from SeekBar so I can maintain code compatibility.
    // Information: SeekBar also has a onStartTrackingTouch(SeekBar seekBar) and onStopTrackingTouch(SeekBar seekBar) but I don't need these yet
    private var onSeekBarChangeListener: OnSeekBarChangeListener? = null

    fun setOnSeekBarChangeListener(listener: OnSeekBarChangeListener?) {
        onSeekBarChangeListener = listener
    }

    private var onProgressValueUpdatedListener: CompoundWidgetInterfaces.OnProgressValueUpdatedListener? = null

    fun setOnValueUpdatedListener(listener: CompoundWidgetInterfaces.OnProgressValueUpdatedListener?) {
        onProgressValueUpdatedListener = listener
    }

    private fun initSubView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        LayoutInflater.from(context).inflate(R.layout.widget_title_and_seekbar, this, true)

        seekBarWidgetTitleAndSeekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                updateValueText(getProgress())
                onSeekBarChangeListener?.onProgressChanged(seekBar, progress, fromUser)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                onSeekBarChangeListener?.onStartTrackingTouch(seekBar)
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                onSeekBarChangeListener?.onStopTrackingTouch(seekBar)
            }
        })

        applyAttributes(context, attrs, defStyleAttr)
    }

    private fun updateValueText(value: Int) {
        if (onProgressValueUpdatedListener != null) {
            textViewWidgetTitleAndSeekBarSeekBarValue.text = onProgressValueUpdatedListener?.onProgressValueUpdated(value)
        } else {
            textViewWidgetTitleAndSeekBarSeekBarValue.text = value.toString()
        }
    }

    private fun applyAttributes(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {

        if (attrs == null) {
            return
        }

        val typedArray = context.theme.obtainStyledAttributes(
            attrs, R.styleable.WidgetTitleAndSeekBar, defStyleAttr, 0
        )

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
                    R.styleable.WidgetTitleAndSeekBar_android_text -> {
                        textViewWidgetTitleAndSeekBarTitle.text = typedArray.getText(R.styleable.WidgetTitleAndSeekBar_android_text)
                    }
                    R.styleable.WidgetTitleAndSeekBar_widgetTitleAndSeekBarUnits -> {
                        textViewWidgetTitleAndSeekBarUnits.text = typedArray.getText(R.styleable.WidgetTitleAndSeekBar_widgetTitleAndSeekBarUnits)
                    }
                    R.styleable.WidgetTitleAndSeekBar_widgetTitleAndSeekBarEnableAnimation -> {
                        seekBarWidgetTitleAndSeekBar.animateChanges = typedArray.getBoolean(
                            R.styleable.WidgetTitleAndSeekBar_widgetTitleAndSeekBarEnableAnimation, false
                        )
                    }
                    R.styleable.WidgetTitleAndSeekBar_widgetTitleAndSeekBarMinValue -> {
                        min = typedArray.getInt(
                            R.styleable.WidgetTitleAndSeekBar_widgetTitleAndSeekBarMinValue, defaultMinimum
                        )
                    }
                    R.styleable.WidgetTitleAndSeekBar_widgetTitleAndSeekBarMaxValue -> {
                        max = typedArray.getInt(
                            R.styleable.WidgetTitleAndSeekBar_widgetTitleAndSeekBarMaxValue, defaultMaximum
                        )
                    }
                    R.styleable.WidgetTitleAndSeekBar_android_progress -> {
                        setProgressValue = typedArray.getInt(R.styleable.WidgetTitleAndSeekBar_android_progress, defaultMinimum)
                    }
                }
            }
        } finally {

            textViewWidgetTitleAndSeekBarUnits.visibility =
                if (textViewWidgetTitleAndSeekBarUnits.text.isNullOrEmpty()) {
                    View.GONE
                } else {
                    View.VISIBLE
                }

            setSeekBarRange(min, max)

            setProgress(setProgressValue)

            typedArray.recycle()
        }
    }

    fun setTitle(title: String?) {

        if (title.isNullOrEmpty()) {
            textViewWidgetTitleAndSeekBarTitle.visibility = View.GONE
        } else {
            textViewWidgetTitleAndSeekBarTitle.text = title
            textViewWidgetTitleAndSeekBarTitle.visibility = View.VISIBLE
        }
    }

    fun setUnitsText(subTitle: String?) {

        if (TextUtils.isEmpty(subTitle)) {
            textViewWidgetTitleAndSeekBarUnits.visibility = View.GONE
        } else {
            textViewWidgetTitleAndSeekBarUnits.text = subTitle
            textViewWidgetTitleAndSeekBarUnits.visibility = View.VISIBLE
        }
    }

    fun setSeekBarRange(minimumValue: Int, maximumValue: Int) {
        seekBarWidgetTitleAndSeekBar.setSeekBarRange(minimumValue, maximumValue)
    }

    fun getProgress(): Int {
        return seekBarWidgetTitleAndSeekBar.getProgress()
    }

    @JvmOverloads fun setProgress(value: Int, immediate: Boolean = true) {
        seekBarWidgetTitleAndSeekBar.setProgress(value, immediate)
    }

    init {
        initSubView(context, attrs!!, defStyleAttr)
    }
}