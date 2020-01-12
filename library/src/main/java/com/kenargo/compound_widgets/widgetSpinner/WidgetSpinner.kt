package com.kenargo.compound_widgets.widgetSpinner

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.PopupWindow
import androidx.annotation.StringRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kenargo.compound_widgets.CompoundWidgetInterfaces
import com.kenargo.myapplicationlibrary.R
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration
import kotlinx.android.synthetic.main.widget_spinner.view.*
import java.util.*

class WidgetSpinner @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
    ) : FrameLayout(context, attrs, defStyleAttr) {

    private var widgetSpinnerPopupList: PopupWindow? = null

    private var widgetSpinnerAdapter: WidgetSpinnerAdapter? = null
    private val itemsList: MutableList<String> = ArrayList()

    init {
        initSubView(context, attrs, defStyleAttr)
    }

    private var onItemSelectionChanged: CompoundWidgetInterfaces.SelectedItemChanged? = null

    fun setOnItemSelectedListener(onItemSelectionChanged: CompoundWidgetInterfaces.SelectedItemChanged?) {
        this.onItemSelectionChanged = onItemSelectionChanged
    }

    var selectedIndex = 0
        set(value) {
            field = value

            if (value >= 0 && value < itemsList.size) {
                text = itemsList[value]
            }
        }

    var coverParent = false

    private var mPopupWindowMaxHeight = 0
    private var mPopupWindowHeight = 0

    private fun initSubView(
        context: Context, attrs: AttributeSet?, defStyleAttr: Int
    ) {
        LayoutInflater.from(context).inflate(R.layout.widget_spinner, this, true)

        applyAttributes(context, attrs, defStyleAttr)

        // In edit mode I don't need handlers and callbacks
        if (isInEditMode) {
            return
        }

        linearLayoutSpinner.setOnClickListener {

            if (widgetSpinnerPopupList!!.isShowing) {

                imageViewWidgetSpinnerDropIndicator.setImageResource(R.drawable.widget_spinner_collapsed_arrow)
                widgetSpinnerPopupList!!.dismiss()
            } else {

                imageViewWidgetSpinnerDropIndicator.setImageResource(R.drawable.widget_spinner_expanded_arrow)
                showPopupView(context)
            }
        }

        // TODO: Number of items displayed
        mPopupWindowMaxHeight = resources.getDimension(R.dimen.common_44dp).toInt() * 4

        initPopupWindow(CompoundWidgetInterfaces.SelectedItemChanged { position: Int? ->

            widgetSpinnerPopupList!!.dismiss()

            imageViewWidgetSpinnerDropIndicator.setImageResource(R.drawable.widget_spinner_collapsed_arrow)

            textViewWidgetSpinnerText.text = itemsList[position!!]

            widgetSpinnerAdapter!!.setCurrentIndex(position)
            widgetSpinnerAdapter!!.notifyDataSetChanged()

            onItemSelectionChanged?.onSelectionChange(position)
        })
    }

    private fun applyAttributes(
        context: Context, attrs: AttributeSet?, defStyleAttr: Int
    ) {

        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.WidgetSpinner, defStyleAttr, 0)

        var entriesAttribute = 0

        try {
            for (index in 0 until typedArray.length()) {

                var attribute: Int = try {
                    typedArray.getIndex(index)
                } catch (ignore: Exception) {
                    continue
                }

                when (attribute) {
                    R.styleable.WidgetSpinner_widgetSpinnerCoverParent -> {
                        coverParent = typedArray.getBoolean(
                            R.styleable.WidgetSpinner_widgetSpinnerCoverParent, true
                        )
                    }
                    R.styleable.WidgetSpinner_android_entries -> {
                        entriesAttribute = attrs!!.getAttributeResourceValue(
                            R.styleable.WidgetSpinner_android_entries, 0
                        )
                    }
                }
            }
        } finally {

            if (entriesAttribute != 0) {

                itemsList.addAll(
                    listOf(*context.resources.getStringArray(entriesAttribute))
                )

                if (isInEditMode) {
                    // Show the 1st item in the list in the designer
                    textViewWidgetSpinnerText.text = itemsList[0]
                }
            }

            typedArray.recycle()
        }
    }

    private fun initPopupWindow(onSelectedListener: CompoundWidgetInterfaces.SelectedItemChanged)  {

        val showView = LayoutInflater.from(context).inflate(R.layout.widget_spinner_drop_down, null)

        val recyclerView: RecyclerView = showView.findViewById(R.id.recyclerViewWidgetSpinnerDropList)
        recyclerView.addItemDecoration(HorizontalDividerItemDecoration.Builder(context).build())

        recyclerView.layoutManager = LinearLayoutManager(context)

        widgetSpinnerAdapter = WidgetSpinnerAdapter(context, itemsList, onSelectedListener)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = widgetSpinnerAdapter

        widgetSpinnerPopupList = PopupWindow(showView)
        //mPopupWindow!!.isOutsideTouchable = true
    }

    private fun showPopupView(context: Context)  {

        val height = (itemsList.size * context.resources.getDimension(R.dimen.common_44dp)).toInt()

        // TODO: Should I use the full width of the entries?
        widgetSpinnerPopupList!!.width = linearLayoutSpinner.measuredWidth

        setDropdownHeight(height)

        val curWindowHeight = if (height > mPopupWindowMaxHeight) {
            mPopupWindowMaxHeight
        } else {
            height
        }

        val rect = Rect()

        getWindowVisibleDisplayFrame(rect)

        if (coverParent) {

            val location = IntArray(2)
            textViewWidgetSpinnerText.getLocationOnScreen(location)

            val locationWindow = IntArray(2)
            textViewWidgetSpinnerText.getLocationInWindow(locationWindow)

            if (location[1] + curWindowHeight > rect.bottom) {
                widgetSpinnerPopupList!!.showAtLocation(
                    textViewWidgetSpinnerText, Gravity.NO_GRAVITY, locationWindow[0], locationWindow[1] + getHeight() - mPopupWindowMaxHeight
                )
            } else {
                widgetSpinnerPopupList!!.showAtLocation(
                    textViewWidgetSpinnerText, Gravity.NO_GRAVITY, locationWindow[0], locationWindow[1]
                )
            }

        } else {
            widgetSpinnerPopupList!!.showAsDropDown(textViewWidgetSpinnerText)
        }
    }

    fun setItemList(itemList: List<String>?) {

        itemsList.clear()

        itemList?.let {

            it.forEach { oneValue ->
                itemsList.add(oneValue)
            }

            if (isInEditMode) {
                // Show the 1st item in the list in the designer
                textViewWidgetSpinnerText.text = itemsList[0]
                return
            }
        }

        widgetSpinnerAdapter!!.notifyDataSetChanged()
        //selectedIndex = mSelectedIndex
    }

    fun setText(@StringRes resid: Int) {
        textViewWidgetSpinnerText.setText(resid)
    }

    var text: CharSequence?
        get() = textViewWidgetSpinnerText.text.toString().trim { it <= ' ' }
        set(text) {
            textViewWidgetSpinnerText.text = text
            widgetSpinnerAdapter!!.setCurrentIndex(itemsList.indexOf(text))
            widgetSpinnerAdapter!!.notifyDataSetChanged()
        }

    fun setDropdownMaxHeight(height: Int) {
        mPopupWindowMaxHeight = height
        widgetSpinnerPopupList!!.height = calculatePopupWindowHeight()
    }

    /**
     * Set the height of the dropdown menu
     *
     * @param height the height in pixels
     */
    fun setDropdownHeight(height: Int) {

        mPopupWindowHeight = height
        widgetSpinnerPopupList!!.height = calculatePopupWindowHeight()
    }

    private fun calculatePopupWindowHeight(): Int {

        if (widgetSpinnerAdapter == null) {
            return WindowManager.LayoutParams.WRAP_CONTENT
        }

        val listViewHeight = widgetSpinnerAdapter!!.itemCount * resources.getDimension(R.dimen.common_44dp)

        if (mPopupWindowMaxHeight > 0 && listViewHeight > mPopupWindowMaxHeight) {
            return mPopupWindowMaxHeight
        } else if (mPopupWindowHeight != WindowManager.LayoutParams.MATCH_PARENT && mPopupWindowHeight != WindowManager.LayoutParams.WRAP_CONTENT && mPopupWindowHeight <= listViewHeight) {
            return mPopupWindowHeight
        }

        return WindowManager.LayoutParams.WRAP_CONTENT
    }


    fun updateSpinnerItemList(spinnerItemList: List<String?>?) {

        if (spinnerItemList == null || spinnerItemList.size == 0) {
            return
        }

//        setItemList(spinnerItemList)
    }
}