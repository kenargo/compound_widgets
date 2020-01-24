package com.kenargo.compound_widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import com.kenargo.myapplicationlibrary.R
import kotlinx.android.synthetic.main.widget_title_and_spinner.view.*

class WidgetTitleAndSpinner @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var onSelectedItemChanged: CompoundWidgetInterfaces.OnSelectedItemChanged? = null

    fun setOnSelectedItemChangedListener(listener: CompoundWidgetInterfaces.OnSelectedItemChanged) {
        onSelectedItemChanged = listener
    }

    private fun initSubView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        LayoutInflater.from(context).inflate(R.layout.widget_title_and_spinner, this, true)

        applyAttributes(context, attrs, defStyleAttr)

        widgetSpinnerWidgetTitleAndSpinner.setOnSelectedItemChangedListener(CompoundWidgetInterfaces.OnSelectedItemChanged {
            // Now call the creator with the new selection
            onSelectedItemChanged?.onSelectionChange(it)
        })
    }

    private fun applyAttributes(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        if (attrs == null) {
            return
        }
        val typedArray = context.theme.obtainStyledAttributes(
            attrs, R.styleable.WidgetTitleAndSpinner, defStyleAttr, 0
        )

        var entriesAttribute = 0
        var spinnerWidth: Int = WRAP_CONTENT
        var hint: CharSequence? = null

        try {
            for (index in 0 until typedArray.length()) {

                val attribute: Int = try {
                    typedArray.getIndex(index)
                } catch (ignore: Exception) {
                    continue
                }

                when (attribute) {
                    R.styleable.WidgetTitleAndSpinner_android_text -> {
                        textViewWidgetTitleAndSpinnerTitle.text = typedArray.getText(R.styleable.WidgetTitleAndSpinner_android_text)
                    }
                    R.styleable.WidgetTitleAndSpinner_android_subtitle -> {
                        textViewWidgetTitleAndSpinnerSubtitle.text = typedArray.getText(R.styleable.WidgetTitleAndSpinner_android_subtitle)
                    }
                    R.styleable.WidgetTitleAndSpinner_widgetTitleAndSpinnerHint -> {
                        hint = typedArray.getText(R.styleable.WidgetTitleAndSpinner_widgetTitleAndSpinnerHint)
                    }
                    R.styleable.WidgetTitleAndSpinner_android_entries -> {
                        entriesAttribute = attrs.getAttributeResourceValue(
                            R.styleable.WidgetTitleAndSpinner_android_entries, 0
                        )
                    }
                    R.styleable.WidgetTitleAndSpinner_widgetTitleAndSpinnerMaxItemDisplay -> {
                        widgetSpinnerWidgetTitleAndSpinner.maxItemDisplay = typedArray.getInt(
                            R.styleable.WidgetTitleAndSpinner_widgetTitleAndSpinnerMaxItemDisplay, -1)
                    }
                    R.styleable.WidgetTitleAndSpinner_widgetTitleAndSpinnerWidth -> {
                        spinnerWidth = typedArray.getLayoutDimension(
                            R.styleable.WidgetTitleAndSpinner_widgetTitleAndSpinnerWidth, 0)
                    }
                }
            }
        } finally {

            textViewWidgetTitleAndSpinnerSubtitle.visibility =
                if (textViewWidgetTitleAndSpinnerSubtitle.text.isNullOrEmpty()) {
                    View.GONE
                } else {
                    View.VISIBLE
                }

            if (entriesAttribute != 0) {

                val itemsList = listOf(*context.resources.getStringArray(entriesAttribute))
                widgetSpinnerWidgetTitleAndSpinner.setItemList(itemsList)
            }

            if (spinnerWidth == WRAP_CONTENT) {
                widgetSpinnerWidgetTitleAndSpinner!!.layoutParams.width = widgetSpinnerWidgetTitleAndSpinner!!.getMaxMaxItemWidth()
            } else {
                widgetSpinnerWidgetTitleAndSpinner!!.layoutParams.width = spinnerWidth.toInt()
            }

            typedArray.recycle()
        }
    }

    fun setSelection(index: Int) {
        widgetSpinnerWidgetTitleAndSpinner.selectedIndex = index
    }

    fun setTitle(title: CharSequence?) {
        textViewWidgetTitleAndSpinnerTitle.text = title
    }

    init {
        initSubView(context, attrs!!, defStyleAttr)
    }
}