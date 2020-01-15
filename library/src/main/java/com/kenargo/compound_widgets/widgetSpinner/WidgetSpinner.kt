package com.kenargo.compound_widgets.widgetSpinner

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kenargo.compound_widgets.CompoundWidgetInterfaces
import com.kenargo.compound_widgets.Conversions
import com.kenargo.myapplicationlibrary.R
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration
import kotlinx.android.synthetic.main.widget_spinner.view.*
import java.util.*
import kotlin.math.max

class WidgetSpinner @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
    ) : FrameLayout(context, attrs, defStyleAttr) {

    private var widgetSpinnerPopupList: PopupWindow? = null

    private var widgetSpinnerAdapter: WidgetSpinnerAdapter? = null
    private val itemsList: MutableList<String> = ArrayList()

    var maxItemDisplay = -1

    init {
        initSubView(context, attrs, defStyleAttr)
    }

    private fun getHeightOfSingleItem(): Int {
        // Height of the recyclerview items (widget_spinner_item) is fixed at 44dp
        return context.resources.getDimension(R.dimen.common_44dp).toInt()
    }

    private fun getHeightOfAllItems(): Int {
        // Height of the recyclerview items (widget_spinner_item) is fixed at 44dp
        return getHeightOfSingleItem() * widgetSpinnerAdapter!!.itemCount
    }

    private var onItemSelectionChanged: CompoundWidgetInterfaces.SelectedItemChanged? = null

    fun setOnItemSelectedListener(onItemSelectionChanged: CompoundWidgetInterfaces.SelectedItemChanged?) {
        this.onItemSelectionChanged = onItemSelectionChanged
    }

    var selectedIndex = 0
        set(value) {
            field = value

            if (value >= 0 && value < itemsList.size) {
                setText(itemsList[value])
            }
        }

    var coverParent = false

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
                showPopupWindow()
            }
        }

        initializePopupWindow(CompoundWidgetInterfaces.SelectedItemChanged { position: Int? ->

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

                val attribute: Int = try {
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
                    R.styleable.WidgetSpinner_widgetSpinnerMaxItemDisplay -> {
                        maxItemDisplay = typedArray.getInt(
                            R.styleable.WidgetSpinner_widgetSpinnerMaxItemDisplay, -1)
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

    private fun initializePopupWindow(onSelectedListener: CompoundWidgetInterfaces.SelectedItemChanged)  {

        val showView = LayoutInflater.from(context).inflate(R.layout.widget_spinner_drop_down, null)

        val recyclerView: RecyclerView = showView.findViewById(R.id.recyclerViewWidgetSpinnerDropList)
        recyclerView.addItemDecoration(HorizontalDividerItemDecoration.Builder(context).build())

        recyclerView.layoutManager = LinearLayoutManager(context)

        widgetSpinnerAdapter = WidgetSpinnerAdapter(context, itemsList, onSelectedListener)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = widgetSpinnerAdapter

        widgetSpinnerPopupList = PopupWindow(showView)

        // Don't use isOutsideTouchable or the PopupWindow for hide/show if the user touches the droplist
        // to dismiss it.
        widgetSpinnerPopupList!!.isFocusable = true
    }

    private fun showPopupWindow() {

        val popupWindowMaxHeight = if (maxItemDisplay == -1) {
            getHeightOfAllItems()
        } else {
            getHeightOfSingleItem() * maxItemDisplay
        }

        widgetSpinnerPopupList!!.height = popupWindowMaxHeight

        // Create a TextView for calculating the accurate width of the PopupWindow
        val textViewTemp = TextView(context)
        textViewTemp.text = getMaxLengthItemString()
        textViewTemp.measure(0, 0)
        val calculatedWidth: Int = textViewTemp.measuredWidth

        widgetSpinnerPopupList!!.width =
            max(linearLayoutSpinner.measuredWidth, Conversions.spToPx(calculatedWidth.toFloat(), context))

        if (coverParent) {

            val location = IntArray(2)
            textViewWidgetSpinnerText.getLocationOnScreen(location)

            val locationWindow = IntArray(2)
            textViewWidgetSpinnerText.getLocationInWindow(locationWindow)

            val visibleRect = Rect()
            getWindowVisibleDisplayFrame(visibleRect)

            val curWindowHeight = max(popupWindowMaxHeight, getHeightOfAllItems())

            if (location[1] + curWindowHeight > visibleRect.bottom) {
                widgetSpinnerPopupList!!.showAtLocation(
                    textViewWidgetSpinnerText,
                    Gravity.NO_GRAVITY,
                    locationWindow[0],
                    locationWindow[1] + height - popupWindowMaxHeight
                )
            } else {
                widgetSpinnerPopupList!!.showAtLocation(
                    textViewWidgetSpinnerText,
                    Gravity.NO_GRAVITY,
                    locationWindow[0],
                    locationWindow[1]
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
    }

    fun setText( text: CharSequence) {
        textViewWidgetSpinnerText.text = text
        widgetSpinnerAdapter!!.setCurrentIndex(itemsList.indexOf(text))
        widgetSpinnerAdapter!!.notifyDataSetChanged()
    }

    fun getText(): String {
        return textViewWidgetSpinnerText.text.toString().trim { it <= ' ' }
    }

    fun updateSpinnerItemList(spinnerItemList: List<String?>?) {

        if (spinnerItemList == null || spinnerItemList.size == 0) {
            return
        }

//        setItemList(spinnerItemList)
    }

    private fun getMaxLengthItemString(): String {
        var maxLength = 0
        var maxLengthItem = -1

        itemsList.forEachIndexed { index, s ->
            maxLengthItem =
                if (s.length > maxLength) {
                    maxLength = index; index
                } else {
                    maxLengthItem
                }
        }

        return itemsList[maxLengthItem]
    }
}