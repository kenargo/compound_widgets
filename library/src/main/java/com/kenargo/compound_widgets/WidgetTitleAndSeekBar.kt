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

    init {
        initSubView(context, attrs!!, defStyleAttr)
    }

    private val defaultMaximum = 100
    private val defaultMinimum = 0

    // Match the callback from SeekBar so I can maintain code compatibility.
    // Information: SeekBar also has a onStartTrackingTouch(SeekBar seekBar) and onStopTrackingTouch(SeekBar seekBar) but I don't need these yet
    private var seekBarChangeListener: OnSeekBarChangeListener? = null

    fun setOnSeekBarChangeListener(cellSeekBarListener: OnSeekBarChangeListener?) {
        seekBarChangeListener = cellSeekBarListener
    }

    private var valueUpdatedListener: CompoundWidgetInterfaces.OnValueUpdatedListener? = null

    fun setOnValueUpdatedListener(valueChangeLister: CompoundWidgetInterfaces.OnValueUpdatedListener?) {
        valueUpdatedListener = valueChangeLister
    }

    private fun initSubView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        LayoutInflater.from(context).inflate(R.layout.widget_title_and_seekbar, this, true)

        applyAttributes(context, attrs, defStyleAttr)

        // In edit mode I don't need handlers and callbacks
        if (isInEditMode) {
            return
        }

        seekBarWidgetTitleAndSeekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                updateValueText(getProgress())
                seekBarChangeListener?.onProgressChanged(seekBar, progress, fromUser)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                updateValueText(getProgress())
                seekBarChangeListener?.onStartTrackingTouch(seekBar)
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                updateValueText(getProgress())
                seekBarChangeListener?.onStopTrackingTouch(seekBar)
            }
        })
    }

    private fun updateValueText(value: Int) {
        if (valueUpdatedListener != null) {
            textViewWidgetTitleAndSeekBarSeekBarValue.text = valueUpdatedListener?.onValueUpdated(value)
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
                    R.styleable.WidgetTitleAndSeekBar_android_text -> {
                        textViewWidgetTitleAndSeekBarTitle.text = typedArray.getText(R.styleable.WidgetTitleAndSeekBar_android_text)
                    }
                    R.styleable.WidgetTitleAndSeekBar_widgetTitleAndSeekBarUnits -> {
                        textViewWidgetTitleAndSeekBarUnits.text = typedArray.getText(R.styleable.WidgetTitleAndSeekBar_widgetTitleAndSeekBarUnits)
                    }
                    R.styleable.WidgetTitleAndSeekBar_widgetTitleAndSeekBarEnableAnimation -> {
                        seekBarWidgetTitleAndSeekBar.animateChanges = typedArray.getBoolean(
                            R.styleable.WidgetTitleAndSeekBar_widgetTitleAndSeekBarEnableAnimation, true
                        )
                    }
                    R.styleable.WidgetTitleAndSeekBar_widgetTitleAndSeekBarMinValue -> {
                        min = typedArray.getInt(
                            R.styleable.WidgetTitleAndSeekBar_widgetTitleAndSeekBarMinValue, 0
                        )
                    }
                    R.styleable.WidgetTitleAndSeekBar_widgetTitleAndSeekBarMaxValue -> {
                        max = typedArray.getInt(
                            R.styleable.WidgetTitleAndSeekBar_widgetTitleAndSeekBarMaxValue, 100
                        )
                    }
                    R.styleable.WidgetTitleAndSeekBar_android_progress -> {
                        setProgressValue = typedArray.getInt(R.styleable.WidgetTitleAndSeekBar_android_progress, 0)
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

            // Initial text view update
            updateValueText(setProgressValue)

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


    fun setSeekBarRange(min: Int, max: Int) {
        seekBarWidgetTitleAndSeekBar.setSeekBarRange(min, max)
    }

    fun getProgress(): Int {
        return seekBarWidgetTitleAndSeekBar.getProgress()
    }

    fun setProgress(value: Int) {
        seekBarWidgetTitleAndSeekBar.setProgress(value)
    }
}