package com.kenargo.compound_widgets

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.*
import android.widget.FrameLayout
import com.kenargo.myapplicationlibrary.R
import com.suke.widget.SwitchButton
import kotlinx.android.synthetic.main.widget_title_and_switch.view.*

class WidgetTitleAndSwitch @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        initSubView(context, attrs!!, defStyleAttr)
    }

    private fun initSubView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        LayoutInflater.from(context).inflate(R.layout.widget_title_and_switch, this, true)
        applyAttributes(context, attrs, defStyleAttr)
    }

    private fun applyAttributes(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        if (attrs == null) {
            return
        }

        val typedArray = context.theme.obtainStyledAttributes(
            attrs, R.styleable.WidgetTitleAndSwitch, defStyleAttr, 0
        )

        // The control will be shown if there is a value
        textViewWidgetTitleAndSwitchSubtitle.visibility = View.GONE

        try {
            for (index in 0 until typedArray.length()) {

                val attribute = try {
                    typedArray.getIndex(index)
                } catch (ignore: Exception) {
                    continue
                }

                // TODO: Need to exposed attributes from the switch
                when (attribute) {
                    R.styleable.WidgetTitleAndSwitch_android_text -> {
                        textViewWidgetTitleAndSwitchTitle.text = typedArray.getText(R.styleable.WidgetTitleAndSwitch_android_text)
                    }
                    R.styleable.WidgetTitleAndSwitch_android_checked -> {
                        widgetLabeledSwitchWidgetTitleAndSwitch.isChecked = typedArray.getBoolean(
                            R.styleable.WidgetTitleAndSwitch_android_checked, false
                        )
                    }
                    R.styleable.WidgetTitleAndSwitch_android_subtitle -> {
                        textViewWidgetTitleAndSwitchSubtitle.text = typedArray.getText(R.styleable.WidgetTitleAndSwitch_android_subtitle)
                        if (!TextUtils.isEmpty(textViewWidgetTitleAndSwitchSubtitle.text)) {
                            textViewWidgetTitleAndSwitchSubtitle.visibility = View.VISIBLE
                        }
                    }
                }
            }
        } finally {
            typedArray.recycle()
        }
    }

    fun setTitle(title: String?) {
        textViewWidgetTitleAndSwitchTitle.text = title
    }

    var isChecked: Boolean
        get() = widgetLabeledSwitchWidgetTitleAndSwitch.isChecked
        set(isChecked) {
            widgetLabeledSwitchWidgetTitleAndSwitch.isChecked = isChecked
        }

    fun setOnCheckedChangeListener(listener: SwitchButton.OnCheckedChangeListener?) {
        widgetLabeledSwitchWidgetTitleAndSwitch.setOnCheckedChangeListener(listener)
    }
}