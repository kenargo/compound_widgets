package com.kenargo.compound_widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.ashraf007.expandableselectionview.ExpandableSelectionViewInterfaces
import com.ashraf007.expandableselectionview.adapter.BasicStringAdapter
import com.kenargo.myapplicationlibrary.R
import kotlinx.android.synthetic.main.widget_title_and_spinner.view.*

class WidgetTitleAndSpinner @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        initSubView(context, attrs!!, defStyleAttr)
    }

    private var onSelectionChangeListener: CompoundWidgetInterfaces.SelectedItemChanged? = null

    fun setOnSelectionChange(listener: CompoundWidgetInterfaces.SelectedItemChanged) {
        onSelectionChangeListener = listener
    }

    private fun initSubView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        LayoutInflater.from(context).inflate(R.layout.widget_title_and_spinner, this, true)
        applyAttributes(context, attrs, defStyleAttr)

        expandableSingleSelectionViewWidgetTitleAndSpinner.setOnSelectionChange(ExpandableSelectionViewInterfaces.SelectedItemChanged {
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

            textViewWidgetTitleAndSpinnerSubtitle.visibility =
                if (textViewWidgetTitleAndSpinnerSubtitle.text.isNullOrEmpty()) {
                    View.GONE
                } else {
                    View.VISIBLE
                }

            if (entriesAttribute != 0) {

                val itemsList = listOf(*context.resources.getStringArray(entriesAttribute))

                // Provide a list of strings and an optional hint
                val expandableAdapter = BasicStringAdapter(itemsList, hint?.toString())

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