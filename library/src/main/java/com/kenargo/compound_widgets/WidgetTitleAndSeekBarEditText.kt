package com.kenargo.compound_widgets

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
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

    private var isTouching = false

    private val defaultMinimum = 0
    private val defaultMaximum = 100

    // Match the callback from SeekBar so I can maintain code compatibility.
    // Information: SeekBar also has a onStartTrackingTouch(SeekBar seekBar) and onStopTrackingTouch(SeekBar seekBar) but I don't need these yet
    private var onSeekBarChangeListener: OnSeekBarChangeListener? = null

    fun setOnSeekBarChangeListener(listener: OnSeekBarChangeListener?) {
        onSeekBarChangeListener = listener
    }

    private var onValueUpdatedListener: CompoundWidgetInterfaces.OnValueUpdatedListener? = null

    fun setOnValueUpdatedListener(listener: CompoundWidgetInterfaces.OnValueUpdatedListener?) {
        onValueUpdatedListener = listener
    }

    // I'm not exposing the OnTextChange listener because I have a callback interface for conversion from display format to value; that's better I think

    private fun initSubView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        LayoutInflater.from(context).inflate(R.layout.widget_title_and_seekbar_edit_text, this, true)

        // TODO: I don't know wht just getting a pointer to the widget causes the designer to fail but it does so for now
        //  just get the pointer only in runtime mode

        applyAttributes(context, attrs, defStyleAttr)

        editTextWidgetTitleAndSeekBarEditTextValue.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (isTouching) {
                    return
                }

                if (onValueUpdatedListener != null) {
                    seekBarWidgetTitleAndSeekBarEditText.setProgress(onValueUpdatedListener?.onUserInputChanged(s.toString())!!)
                } else {
                    if (isValidNumber(s.toString())) {
                        seekBarWidgetTitleAndSeekBarEditText.setProgress(s.toString().toInt(), false)
                    }
                }
            }
        })

        seekBarWidgetTitleAndSeekBarEditText.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

                Log.d("ASDF", "seekBarWidgetTitleAndSeekBarEditText.setOnSeekBarChangeListener, value=$progress, fromUser=$fromUser")

                if (fromUser) {
                    hideKeyboard()
                    updateValueText(getProgress())
                }

                onSeekBarChangeListener?.onProgressChanged(seekBar, progress, fromUser)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                onSeekBarChangeListener?.onStartTrackingTouch(seekBar)
                isTouching = true

                editTextWidgetTitleAndSeekBarEditTextValue.isFocusableInTouchMode = false
                editTextWidgetTitleAndSeekBarEditTextValue.clearFocus()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                onSeekBarChangeListener?.onStopTrackingTouch(seekBar)
                isTouching = false
                editTextWidgetTitleAndSeekBarEditTextValue.isFocusableInTouchMode = true
            }
        })
    }

    private fun updateValueText(value: Int) {

        if (onValueUpdatedListener != null) {
            editTextWidgetTitleAndSeekBarEditTextValue.setText(onValueUpdatedListener?.onProgressValueUpdated(value))
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

        var setProgressValue = defaultMinimum

        var min = defaultMinimum
        var max = defaultMaximum

        var inputType = Integer.MIN_VALUE

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
                            R.styleable.WidgetTitleAndSeekBarEditText_widgetTitleAndSeekBarEditTextEnableAnimation, false
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
                    R.styleable.WidgetTitleAndSeekBarEditText_android_inputType -> {
                        inputType = typedArray.getInt(
                            R.styleable.WidgetTitleAndSeekBarEditText_android_inputType, Integer.MIN_VALUE
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

            // If the user didn't specify the inputType then set a reasonable one based on range
            if (inputType == Integer.MIN_VALUE) {

                editTextWidgetTitleAndSeekBarEditTextValue.inputType = if (seekBarWidgetTitleAndSeekBarEditText.getMinValue() < 0) {
                    InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED
                } else {
                    InputType.TYPE_CLASS_NUMBER
                }
            } else {
                editTextWidgetTitleAndSeekBarEditTextValue.inputType = inputType
            }

            // TextEdit is also updated during the setProgress call
            setProgress(setProgressValue)

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

    fun setUnitsText(subTitle: String?) {

        if (TextUtils.isEmpty(subTitle)) {
            textViewWidgetTitleAndSeekBarEditTextUnits.visibility = View.GONE
        } else {
            textViewWidgetTitleAndSeekBarEditTextUnits.text = subTitle
            textViewWidgetTitleAndSeekBarEditTextUnits.visibility = View.VISIBLE
        }
    }

    fun setSeekBarRange(minimumValue: Int, maximumValue: Int) {
        seekBarWidgetTitleAndSeekBarEditText.setSeekBarRange(minimumValue, maximumValue)
    }

    fun setInputType(inputType: Int) {
        editTextWidgetTitleAndSeekBarEditTextValue.inputType = inputType
    }

    fun getProgress(): Int {
        return seekBarWidgetTitleAndSeekBarEditText.getProgress()
    }

    @JvmOverloads fun setProgress(value: Int, immediate: Boolean = true) {
        Log.d("ASDF", "setProgress = $value")

        // As EditText is not updated during onProgressChanged (unless it's fromUser), I need to update it during the setProgress call
        //  or the EditText will not show the correct value
        updateValueText(value)

        seekBarWidgetTitleAndSeekBarEditText.setProgress(value, immediate)
    }

    init {
        initSubView(context, attrs!!, defStyleAttr)
    }
}