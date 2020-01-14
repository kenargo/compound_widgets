package com.kenargo.compound_widgets

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import com.kenargo.myapplicationlibrary.R
import kotlinx.android.synthetic.main.widget_title_and_seekbar_edit_text.view.*

class WidgetTitleAndSeekBarEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        initSubView(context, attrs!!, defStyleAttr)
    }

    private var isTouching = false

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
        LayoutInflater.from(context).inflate(R.layout.widget_title_and_seekbar_edit_text, this, true)

        // TODO: I don't know wht just getting a pointer to the widget causes the designer to fail but it does so for now
        //  just get the pointer only in runtime mode

        applyAttributes(context, attrs, defStyleAttr)

        // In edit mode I don't need handlers and callbacks
        if (isInEditMode) {
            return
        }

        editTextWidgetTitleAndSeekBarEditTextValue.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (isTouching) {
                    return
                }

                if (isValidNumber(s.toString())) {
                    seekBarWidgetTitleAndSeekBarEditText.setProgress(s.toString().toInt())
                }
            }
        })

        seekBarWidgetTitleAndSeekBarEditText.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    hideKeyboard()
                    updateValueText(getProgress())
                }

                seekBarChangeListener?.onProgressChanged(seekBar, progress, fromUser)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                seekBarChangeListener?.onStartTrackingTouch(seekBar)
                isTouching = true

                editTextWidgetTitleAndSeekBarEditTextValue.isFocusableInTouchMode = false
                editTextWidgetTitleAndSeekBarEditTextValue.clearFocus()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                seekBarChangeListener?.onStopTrackingTouch(seekBar)
                isTouching = false
                editTextWidgetTitleAndSeekBarEditTextValue.isFocusableInTouchMode = true
            }
        })
    }

    private fun updateValueText(value: Int) {

        if (valueUpdatedListener != null) {
            editTextWidgetTitleAndSeekBarEditTextValue.setText(valueUpdatedListener?.onValueUpdated(value))
        } else {
            editTextWidgetTitleAndSeekBarEditTextValue.setText(value.toString())
            editTextWidgetTitleAndSeekBarEditTextValue.setSelection(
                editTextWidgetTitleAndSeekBarEditTextValue.text.length
            )
        }
    }

    private fun applyAttributes(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        if (attrs == null) {
            return
        }

        val typedArray = context.theme.obtainStyledAttributes(
            attrs, R.styleable.WidgetTitleAndSeekBarEditText, defStyleAttr, 0
        )

        var setProgressValue = 0

        var min = defaultMinimum
        var max = defaultMaximum

        try {
            for (index in 0 until typedArray.length()) {

                val attribute: Int = try {
                    typedArray.getIndex(index)
                } catch (ignore: Exception) {
                    continue
                }

                // TODO: Consider changing to use the range edit and expose its attributes
                // Currently using EditText
                when (attribute) {
                    R.styleable.WidgetTitleAndSeekBarEditText_android_text -> {
                        textViewWidgetTitleAndSeekBarEditTextTitle.text = typedArray.getText(R.styleable.WidgetTitleAndSeekBarEditText_android_text)
                    }
                    R.styleable.WidgetTitleAndSeekBarEditText_widgetTitleAndSeekBarEditTextUnits -> {
                        textViewWidgetTitleAndSeekBarEditTextUnits.text =
                            typedArray.getText(R.styleable.WidgetTitleAndSeekBarEditText_widgetTitleAndSeekBarEditTextUnits)
                    }
                    R.styleable.WidgetTitleAndSeekBarEditText_widgetTitleAndSeekBarEditTextEnableAnimation -> {
                        seekBarWidgetTitleAndSeekBarEditText.animateChanges = typedArray.getBoolean(
                            R.styleable.WidgetTitleAndSeekBarEditText_widgetTitleAndSeekBarEditTextEnableAnimation, true
                        )
                    }
                    R.styleable.WidgetTitleAndSeekBarEditText_widgetTitleAndSeekBarEditTextMinValue -> {
                        min = typedArray.getInt(
                            R.styleable.WidgetTitleAndSeekBarEditText_widgetTitleAndSeekBarEditTextMinValue, defaultMinimum
                        )
                    }
                    R.styleable.WidgetTitleAndSeekBarEditText_widgetTitleAndSeekBarEditTextMaxValue -> {
                        max = typedArray.getInt(
                            R.styleable.WidgetTitleAndSeekBarEditText_widgetTitleAndSeekBarEditTextMaxValue, defaultMaximum
                        )
                    }
                    R.styleable.WidgetTitleAndSeekBarEditText_android_progress -> {
                        setProgressValue = typedArray.getInt(
                            R.styleable.WidgetTitleAndSeekBarEditText_android_progress, defaultMinimum
                        )
                    }
                }
            }
        } finally {

            textViewWidgetTitleAndSeekBarEditTextUnits.visibility =
                if (textViewWidgetTitleAndSeekBarEditTextUnits.text.isNullOrEmpty()) {
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

    private fun isValidNumber(text: String): Boolean {
        return text.matches(Regex("^\\s*-?[0-9]{1,10}\\s*\$"))
    }

    private fun hideKeyboard() {
        val imm = context.applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(
            editTextWidgetTitleAndSeekBarEditTextValue.windowToken, InputMethodManager.HIDE_NOT_ALWAYS
        )
    }

    fun setTitle(title: String?) {

        if (title.isNullOrEmpty()) {
            textViewWidgetTitleAndSeekBarEditTextTitle.visibility = View.GONE
        } else {
            textViewWidgetTitleAndSeekBarEditTextTitle.text = title
            textViewWidgetTitleAndSeekBarEditTextTitle.visibility = View.VISIBLE
        }
    }

    fun setValueText(subTitle: String?) {

        if (TextUtils.isEmpty(subTitle)) {
            textViewWidgetTitleAndSeekBarEditTextUnits.visibility = View.GONE
        } else {
            textViewWidgetTitleAndSeekBarEditTextUnits.text = subTitle
            textViewWidgetTitleAndSeekBarEditTextUnits.visibility = View.VISIBLE
        }
    }

    fun setSeekBarRange(min: Int, max: Int) {
        seekBarWidgetTitleAndSeekBarEditText.setSeekBarRange(min, max)

        editTextWidgetTitleAndSeekBarEditTextValue.inputType = if (min < 0) {
            InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED
        } else {
            InputType.TYPE_CLASS_NUMBER
        }
    }

    fun getProgress(): Int {
        return seekBarWidgetTitleAndSeekBarEditText.getProgress()
    }

    fun setProgress(value: Int) {
        seekBarWidgetTitleAndSeekBarEditText.setProgress(value)
    }
}