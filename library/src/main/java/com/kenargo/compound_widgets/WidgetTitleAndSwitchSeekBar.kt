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
import kotlinx.android.synthetic.main.widget_title_and_switch_seekbar.view.*

class WidgetTitleAndSwitchSeekBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val defaultMaximum = 100
    private val defaultMinimum = 0

    // Match the callback from SeekBar so I can maintain code compatibility.
    // Information: SeekBar also has a onStartTrackingTouch(SeekBar seekBar) and onStopTrackingTouch(SeekBar seekBar) but I don't need these yet
    private var onSeekBarChangeListener: OnSeekBarChangeListener? = null

    fun setOnSeekBarChangeListener(listener: OnSeekBarChangeListener?) {
        this.onSeekBarChangeListener = listener
    }

    private var onCheckedChangeListener: CompoundWidgetInterfaces.OnCheckedChangeListener? = null

    fun setOnCheckedChangedListener(listener: CompoundWidgetInterfaces.OnCheckedChangeListener?) {
        this.onCheckedChangeListener = listener
    }

    private var onValueUpdatedListener: CompoundWidgetInterfaces.OnValueUpdatedListener? = null

    fun setOnValueUpdatedListener(listener: CompoundWidgetInterfaces.OnValueUpdatedListener?) {
        onValueUpdatedListener = listener
    }

    private fun initSubView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        LayoutInflater.from(context).inflate(R.layout.widget_title_and_switch_seekbar, this, true)

        if (!isInEditMode) {

            // com.suke.widget.SwitchButton causes the entire control to not be shown in the visual editor
            seekBarSwitchWidgetTitleAndSwitchSeekBar.setOnCheckedChangeListener { view: View?, isChecked: Boolean ->
                seekBarGroupWidgetTitleAndSwitchSeekBar.visibility = if (isChecked) View.VISIBLE else View.GONE

                onCheckedChangeListener?.onCheckedChanged(view, isChecked)
            }
        }

        seekBarWidgetTitleAndSwitchSeekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
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
        if (onValueUpdatedListener != null) {
            textViewWidgetTitleAndSwitchSeekBarSeekBarValue.text = onValueUpdatedListener?.onProgressValueUpdated(value)
        } else {
            textViewWidgetTitleAndSwitchSeekBarSeekBarValue.text = value.toString()
        }
    }

    private fun applyAttributes(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {

        if (attrs == null) {
            return
        }

        val typedArray = context.theme.obtainStyledAttributes(
            attrs, R.styleable.WidgetTitleAndSwitchSeekBar, defStyleAttr, 0
        )

        var setProgressValue = defaultMinimum

        var min = defaultMinimum
        var max = defaultMaximum

        var isSwitchChecked = false

        try {

            for (index in 0 until typedArray.length()) {

                val attribute: Int = try {
                    typedArray.getIndex(index)
                } catch (ignore: Exception) {
                    continue
                }

                when (attribute) {
                    R.styleable.WidgetTitleAndSwitchSeekBar_android_text -> {
                        textViewWidgetTitleAndSwitchSeekBarTitle.text = typedArray.getText(R.styleable.WidgetTitleAndSwitchSeekBar_android_text)
                    }
                    R.styleable.WidgetTitleAndSwitchSeekBar_android_checked -> {
                        isSwitchChecked = typedArray.getBoolean(
                            R.styleable.WidgetTitleAndSwitchSeekBar_android_checked, false
                        )
                    }
                    R.styleable.WidgetTitleAndSwitchSeekBar_android_subtitle -> {
                        textViewWidgetTitleAndSwitchSeekBarSubtitle.text =
                            typedArray.getText(R.styleable.WidgetTitleAndSwitchSeekBar_android_subtitle)
                    }
                    R.styleable.WidgetTitleAndSwitchSeekBar_widgetTitleAndSwitchSeekBarUnits -> {
                        textViewWidgetTitleAndSwitchSeekBarUnits.text =
                            typedArray.getText(R.styleable.WidgetTitleAndSwitchSeekBar_widgetTitleAndSwitchSeekBarUnits)
                    }
                    R.styleable.WidgetTitleAndSeekBar_widgetTitleAndSeekBarEnableAnimation -> {
                        seekBarWidgetTitleAndSwitchSeekBar.animateChanges = typedArray.getBoolean(
                            R.styleable.WidgetTitleAndSeekBar_widgetTitleAndSeekBarEnableAnimation, false
                        )
                    }
                    R.styleable.WidgetTitleAndSwitchSeekBar_widgetTitleAndSwitchSeekBarMinValue -> {
                        min = typedArray.getInt(
                            R.styleable.WidgetTitleAndSwitchSeekBar_widgetTitleAndSwitchSeekBarMinValue, defaultMinimum
                        )
                    }
                    R.styleable.WidgetTitleAndSwitchSeekBar_widgetTitleAndSwitchSeekBarMaxValue -> {
                        max = typedArray.getInt(
                            R.styleable.WidgetTitleAndSwitchSeekBar_widgetTitleAndSwitchSeekBarMaxValue, defaultMaximum
                        )
                    }
                    R.styleable.WidgetTitleAndSwitchSeekBar_android_progress -> {
                        setProgressValue = typedArray.getInt(
                            R.styleable.WidgetTitleAndSwitchSeekBar_android_progress, defaultMinimum
                        )
                    }
                }
            }
        } finally {

            textViewWidgetTitleAndSwitchSeekBarSubtitle.visibility =
                if (textViewWidgetTitleAndSwitchSeekBarSubtitle.text.isNullOrEmpty()) {
                    View.GONE
                } else {
                    View.VISIBLE
                }

            textViewWidgetTitleAndSwitchSeekBarUnits.visibility =
                if (textViewWidgetTitleAndSwitchSeekBarUnits.text.isNullOrEmpty()) {
                    View.GONE
                } else {
                    View.VISIBLE
                }

            setSeekBarRange(min, max)

            setProgress(setProgressValue)

            seekBarSwitchWidgetTitleAndSwitchSeekBar.isChecked = isSwitchChecked

            seekBarGroupWidgetTitleAndSwitchSeekBar.visibility = if (isSwitchChecked) View.VISIBLE else View.GONE

            typedArray.recycle()
        }
    }

    fun setTitle(title: String?) {

        if (title.isNullOrEmpty()) {
            textViewWidgetTitleAndSwitchSeekBarTitle.visibility = View.GONE
        } else {
            textViewWidgetTitleAndSwitchSeekBarTitle.text = title
            textViewWidgetTitleAndSwitchSeekBarTitle.visibility = View.VISIBLE
        }
    }

    fun setSubTitle(subTitle: String?) {

        if (TextUtils.isEmpty(textViewWidgetTitleAndSwitchSeekBarSubtitle.text)) {
            textViewWidgetTitleAndSwitchSeekBarSubtitle.visibility = View.GONE
        } else {
            textViewWidgetTitleAndSwitchSeekBarSubtitle.text = subTitle
            textViewWidgetTitleAndSwitchSeekBarSubtitle.visibility = View.VISIBLE
        }
    }

    fun setUnitsText(subTitle: String?) {

        if (TextUtils.isEmpty(subTitle)) {
            textViewWidgetTitleAndSwitchSeekBarUnits.visibility = View.GONE
        } else {
            textViewWidgetTitleAndSwitchSeekBarUnits.text = subTitle
            textViewWidgetTitleAndSwitchSeekBarUnits.visibility = View.VISIBLE
        }
    }

    fun setSeekBarRange(minimumValue: Int, maximumValue: Int) {
        seekBarWidgetTitleAndSwitchSeekBar.setSeekBarRange(minimumValue, maximumValue)
    }

    fun getProgress(): Int {
        return seekBarWidgetTitleAndSwitchSeekBar.getProgress()
    }

    @JvmOverloads fun setProgress(value: Int, immediate: Boolean = true) {
        seekBarWidgetTitleAndSwitchSeekBar.setProgress(value, immediate)
    }

    fun setChecked(isChecked: Boolean) {
        seekBarSwitchWidgetTitleAndSwitchSeekBar.isChecked = isChecked
        seekBarGroupWidgetTitleAndSwitchSeekBar.visibility = if (isChecked) View.VISIBLE else View.GONE
    }

    init {
        initSubView(context, attrs!!, defStyleAttr)
    }
}