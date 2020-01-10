package com.kenargo.compound_widgets

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.*
import android.widget.*
import android.widget.SeekBar.OnSeekBarChangeListener
import com.kenargo.myapplicationlibrary.R
import kotlinx.android.synthetic.main.widget_title_and_switch_seekbar.view.*

class WidgetTitleAndSwitchSeekBar @JvmOverloads constructor(
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

    fun setOnSeekBarChangeListener(seekBarChangeListener: OnSeekBarChangeListener?) {
        this.seekBarChangeListener = seekBarChangeListener
    }

    private var onCheckedChangeListener: CompoundWidgetInterfaces.OnCheckedChangeListener? = null

    fun setOnCheckedChangedListener(onCheckedChangeListener: CompoundWidgetInterfaces.OnCheckedChangeListener?) {
        this.onCheckedChangeListener = onCheckedChangeListener
    }

    private fun initSubView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        LayoutInflater.from(context).inflate(R.layout.widget_title_and_switch_seekbar, this, true)

        applyAttributes(context, attrs, defStyleAttr)

        // In edit mode I don't need handlers and callbacks
        if (isInEditMode) {
            return
        }

        seekBarSwitchWidgetTitleAndSwitchSeekBar.setOnCheckedChangeListener { view: View?, isChecked: Boolean ->
            seekBarGroupWidgetTitleAndSwitchSeekBar.visibility = if (isChecked) View.VISIBLE else View.GONE

            onCheckedChangeListener?.onCheckedChanged(view, isChecked)
        }

        seekBarWidgetTitleAndSwitchSeekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                updateTextView(getProgress())
                seekBarChangeListener?.onProgressChanged(seekBar, progress, fromUser)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                updateTextView(getProgress())
                seekBarChangeListener?.onStartTrackingTouch(seekBar)
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                updateTextView(getProgress())
                seekBarChangeListener?.onStopTrackingTouch(seekBar)
            }
        })
    }

    private fun updateTextView(value: Int) {
        textViewWidgetTitleAndSwitchSeekBarSeekBarValue.text = value.toString()
    }

    private fun applyAttributes(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        if (attrs == null) {
            return
        }

        val typedArray = context.theme.obtainStyledAttributes(
            attrs, R.styleable.WidgetTitleAndSwitchSeekBar, defStyleAttr, 0
        )

        var attributeProgress = 0

        // The control will be shown if there is a value
        textViewWidgetTitleAndSwitchSeekBarSubtitle.visibility = View.GONE
        textViewWidgetTitleAndSwitchSeekBarUnits.visibility = View.GONE

        var min = defaultMinimum
        var max = defaultMaximum

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
                        seekBarSwitchWidgetTitleAndSwitchSeekBar.isChecked = typedArray.getBoolean(
                            R.styleable.WidgetTitleAndSwitchSeekBar_android_checked, false
                        )
                    }
                    R.styleable.WidgetTitleAndSwitchSeekBar_android_subtitle -> {
                        textViewWidgetTitleAndSwitchSeekBarSubtitle.text =
                            typedArray.getText(R.styleable.WidgetTitleAndSwitchSeekBar_android_subtitle)
                        if (!TextUtils.isEmpty(textViewWidgetTitleAndSwitchSeekBarSubtitle.text)) {
                            textViewWidgetTitleAndSwitchSeekBarSubtitle.visibility = View.VISIBLE
                        }
                    }
                    R.styleable.WidgetTitleAndSwitchSeekBar_widgetTitleAndSwitchSeekBarUnits -> {
                        textViewWidgetTitleAndSwitchSeekBarUnits.text =
                            typedArray.getText(R.styleable.WidgetTitleAndSwitchSeekBar_widgetTitleAndSwitchSeekBarUnits)
                        if (!TextUtils.isEmpty(textViewWidgetTitleAndSwitchSeekBarUnits.text)) {
                            textViewWidgetTitleAndSwitchSeekBarUnits.visibility = View.VISIBLE
                        }
                    }
                    R.styleable.WidgetTitleAndSeekBar_widgetTitleAndSeekBarEnableAnimation -> {
                        seekBarWidgetTitleAndSwitchSeekBar.animateChanges = typedArray.getBoolean(
                            R.styleable.WidgetTitleAndSeekBar_widgetTitleAndSeekBarEnableAnimation, true
                        )
                    }

                    R.styleable.WidgetTitleAndSwitchSeekBar_widgetTitleAndSwitchSeekBarMinValue -> {
                        min = typedArray.getInt(
                            R.styleable.WidgetTitleAndSwitchSeekBar_widgetTitleAndSwitchSeekBarMinValue, 0
                        )
                    }
                    R.styleable.WidgetTitleAndSwitchSeekBar_widgetTitleAndSwitchSeekBarMaxValue -> {
                        max = typedArray.getInt(
                            R.styleable.WidgetTitleAndSwitchSeekBar_widgetTitleAndSwitchSeekBarMaxValue, 100
                        )
                    }
                    R.styleable.WidgetTitleAndSwitchSeekBar_android_progress -> {
                        attributeProgress = typedArray.getInt(
                            R.styleable.WidgetTitleAndSwitchSeekBar_android_progress, 0
                        )
                    }
                }
            }
        } finally {

            setSeekBarRange(min, max)

            setProgress(attributeProgress)

            // Initial text view update
            updateTextView(attributeProgress)

            seekBarGroupWidgetTitleAndSwitchSeekBar.visibility = if (seekBarSwitchWidgetTitleAndSwitchSeekBar.isChecked) View.VISIBLE else View.GONE

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

    fun setUnitsTitle(subTitle: String?) {

        if (TextUtils.isEmpty(subTitle)) {
            textViewWidgetTitleAndSwitchSeekBarUnits.visibility = View.GONE
        } else {
            textViewWidgetTitleAndSwitchSeekBarUnits.text = subTitle
            textViewWidgetTitleAndSwitchSeekBarUnits.visibility = View.VISIBLE
        }
    }

    fun setSeekBarRange(min: Int, max: Int) {
        seekBarWidgetTitleAndSwitchSeekBar.setSeekBarRange(min, max)
    }

    fun getProgress(): Int {
        return seekBarWidgetTitleAndSwitchSeekBar.getProgress()
    }

    fun setProgress(value: Int) {
        seekBarWidgetTitleAndSwitchSeekBar.setProgress(value)
    }

    fun setChecked(isChecked: Boolean) {
        seekBarSwitchWidgetTitleAndSwitchSeekBar.isChecked = isChecked
        seekBarGroupWidgetTitleAndSwitchSeekBar.visibility = if (isChecked) View.VISIBLE else View.GONE
    }
}