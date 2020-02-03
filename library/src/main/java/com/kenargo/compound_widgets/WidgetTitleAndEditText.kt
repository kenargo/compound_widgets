package com.kenargo.compound_widgets

import android.content.Context
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import com.kenargo.myapplicationlibrary.R
import kotlinx.android.synthetic.main.widget_title_and_edit_text.view.*

class WidgetTitleAndEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var onTextChangedListener: TextWatcher? = null

    fun addTextChangedListener(listener: TextWatcher?) {
        onTextChangedListener = listener
    }

    // I'm not exposing the OnTextChange listener because I have a callback interface for conversion from display format to value; that's better I think

    private fun initSubView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        LayoutInflater.from(context).inflate(R.layout.widget_title_and_edit_text, this, true)

        // TODO: I don't know wht just getting a pointer to the widget causes the designer to fail but it does so for now
        //  just get the pointer only in runtime mode

        applyAttributes(context, attrs, defStyleAttr)

        editTextWidgetTitleAndEditTextValue.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                onTextChangedListener?.afterTextChanged(s)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                onTextChangedListener?.beforeTextChanged(s, start, count, after)
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                onTextChangedListener?.onTextChanged(s, start, before, count)
            }
        })
    }

    private fun applyAttributes(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {

        if (attrs == null) {
            return
        }

        val typedArray = context.theme.obtainStyledAttributes(
            attrs, R.styleable.WidgetTitleAndEditText, defStyleAttr, 0
        )

        var inputType = Integer.MIN_VALUE

        try {
            for (index in 0 until typedArray.length()) {

                val attribute: Int = try {
                    typedArray.getIndex(index)
                } catch (ignore: Exception) {
                    continue
                }

                when (attribute) {
                    R.styleable.WidgetTitleAndEditText_android_text -> {
                        textViewWidgetTitleAndEditTextTitle.text =
                            typedArray.getText(R.styleable.WidgetTitleAndEditText_android_text)
                    }
                    R.styleable.WidgetTitleAndEditText_widgetTitleAndEditTextUnits -> {
                        textViewWidgetTitleAndEditTextUnits.text =
                            typedArray.getText(R.styleable.WidgetTitleAndEditText_widgetTitleAndEditTextUnits)
                    }
                    R.styleable.WidgetTitleAndEditText_widgetTitleAndEditTextEditText -> {
                        editTextWidgetTitleAndEditTextValue.setText(
                            typedArray.getText(R.styleable.WidgetTitleAndEditText_widgetTitleAndEditTextEditText)
                        )
                    }
                    R.styleable.WidgetTitleAndEditText_android_inputType -> {
                        inputType = typedArray.getInt(
                            R.styleable.WidgetTitleAndEditText_android_inputType, Integer.MIN_VALUE
                        )
                    }
                    R.styleable.WidgetTitleAndEditText_android_subtitle -> {
                        textViewWidgetTitleAndEditTextSubtitle.text = typedArray.getText(R.styleable.WidgetTitleAndEditText_android_subtitle)
                    }
                }
            }
        } finally {

            textViewWidgetTitleAndEditTextSubtitle.visibility =
                if (textViewWidgetTitleAndEditTextSubtitle.text.isNullOrEmpty()) {
                    View.GONE
                } else {
                    View.VISIBLE
                }

            textViewWidgetTitleAndEditTextUnits.visibility =
                if (textViewWidgetTitleAndEditTextUnits.text.isNullOrEmpty()) {
                    View.GONE
                } else {
                    View.VISIBLE
                }

            // If the user didn't specify the inputType then set a reasonable one based on range
            if (inputType != Integer.MIN_VALUE) {
                editTextWidgetTitleAndEditTextValue.inputType = inputType
            }

            typedArray.recycle()
        }
    }

    private fun isValidNumber(text: String): Boolean {
        return text.matches(Regex("^\\s*-?[0-9]{1,10}\\s*\$"))
    }

    private fun hideKeyboard() {
        val imm = context.applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(
            editTextWidgetTitleAndEditTextValue.windowToken, InputMethodManager.HIDE_NOT_ALWAYS
        )
    }

    fun setTitle(title: String?) {

        if (title.isNullOrEmpty()) {
            textViewWidgetTitleAndEditTextTitle.visibility = View.GONE
        } else {
            textViewWidgetTitleAndEditTextTitle.text = title
            textViewWidgetTitleAndEditTextTitle.visibility = View.VISIBLE
        }
    }

    fun setUnitsText(subTitle: String?) {

        if (TextUtils.isEmpty(subTitle)) {
            textViewWidgetTitleAndEditTextUnits.visibility = View.GONE
        } else {
            textViewWidgetTitleAndEditTextUnits.text = subTitle
            textViewWidgetTitleAndEditTextUnits.visibility = View.VISIBLE
        }
    }

    fun setInputType(inputType: Int) {
        editTextWidgetTitleAndEditTextValue.inputType = inputType
    }

    init {
        initSubView(context, attrs!!, defStyleAttr)
    }
}