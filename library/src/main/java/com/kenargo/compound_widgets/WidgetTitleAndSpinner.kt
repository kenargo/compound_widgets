package com.kenargo.compound_widgets

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.*
import android.widget.FrameLayout
import com.ashraf007.expandableselectionview.adapter.BasicStringAdapter
import com.kenargo.myapplicationlibrary.R
import kotlinx.android.synthetic.main.widget_title_and_spinner.view.*

class WidgetTitleAndSpinner @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        initSubView(context, attrs!!, defStyleAttr)
    }

    private var onSelectionChangeListener: Interfaces.SelectedItemChanged? = null

    fun setOnSelectionChange(listener: Interfaces.SelectedItemChanged) {
        onSelectionChangeListener = listener
    }

    private fun initSubView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        LayoutInflater.from(context).inflate(R.layout.widget_title_and_spinner, this, true)
        applyAttributes(context, attrs, defStyleAttr)

        expandableSingleSelectionViewWidgetTitleAndSpinner.setOnSelectionChange(com.ashraf007.expandableselectionview.Interfaces.SelectedItemChanged {
            // Now call the creator with the new selection
            onSelectionChangeListener?.onSelectionChange(it)
        })
    }

    private fun applyAttributes(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        if (attrs == null) {
            return
        }
        val typedArray = context.theme.obtainStyledAttributes(
            attrs, R.styleable.WidgetTitleAndSpinner, defStyleAttr, 0
        )
        // The control will be shown if there is a value
        textViewWidgetTitleAndSpinnerSubtitle.visibility = View.GONE

        var entriesAttribute = 0
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
                        if (!TextUtils.isEmpty(textViewWidgetTitleAndSpinnerSubtitle.text)) {
                            textViewWidgetTitleAndSpinnerSubtitle.visibility = View.VISIBLE
                        }
                    }
                    R.styleable.WidgetTitleAndSpinner_widgetTitleAndSpinnerHint -> {
                        hint = typedArray.getText(R.styleable.WidgetTitleAndSpinner_widgetTitleAndSpinnerHint)
                    }
                    R.styleable.WidgetTitleAndSpinner_android_entries -> {
                        entriesAttribute = attrs.getAttributeResourceValue(
                            R.styleable.WidgetTitleAndSpinner_android_entries, 0
                        )

                    }
                }
            }
        } finally {

            if (entriesAttribute != 0) {

                val genders = listOf(*context.resources.getStringArray(entriesAttribute))

                // Provide a list of strings and an optional hint
                val expandableAdapter = BasicStringAdapter(genders, hint?.toString())

                // Set the adapter to the component
                expandableSingleSelectionViewWidgetTitleAndSpinner.setAdapter(expandableAdapter)
            }

            typedArray.recycle()
        }
    }

    fun setSelection(index: Int) {
        expandableSingleSelectionViewWidgetTitleAndSpinner.selectIndex(index)
    }

    fun setTitle(title: CharSequence?) {
        textViewWidgetTitleAndSpinnerTitle.text = title
    }
}