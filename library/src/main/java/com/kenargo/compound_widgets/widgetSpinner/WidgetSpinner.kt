package com.kenargo.compound_widgets.widgetSpinner

import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
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

    private var mPopupWindow: PopupWindow? = null

    private var mAdapter: WidgetSpinnerAdapter? = null
    private val mItemList: MutableList<String> = ArrayList()

    init {
        initSubView(context, attrs, defStyleAttr)
    }

    private var onItemSelectionChanged: CompoundWidgetInterfaces.SelectedItemChanged? = null

    fun setOnItemSelectedListener(onItemSelectionChanged: CompoundWidgetInterfaces.SelectedItemChanged?) {
        this.onItemSelectionChanged = onItemSelectionChanged
    }

    private var mSelectedIndex = 0
    private var isCover = false
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

            if (mPopupWindow!!.isShowing) {
                imageViewWidgetSpinnerDropIndicator.setImageResource(R.drawable.widget_spinner_collapsed_arrow)
                mPopupWindow!!.dismiss()
            } else {
                imageViewWidgetSpinnerDropIndicator.setImageResource(R.drawable.widget_spinner_expanded_arrow)
                showPopupView(context)
            }
        }

        // TODO: Number of items displayed
        mPopupWindowMaxHeight = resources.getDimension(R.dimen.common_44dp).toInt() * 4

        initPopupWindow()
    }

    private fun applyAttributes(
        context: Context, attrs: AttributeSet?, defStyleAttr: Int
    ) {

        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.WidgetSpinner, defStyleAttr, 0)

        try {
            for (index in 0 until typedArray.length()) {

                var attribute: Int = try {
                    typedArray.getIndex(index)
                } catch (ignore: Exception) {
                    continue
                }

                when (attribute) {
                    R.styleable.WidgetSpinner_android_entries -> {
                        val entriesAttribute = attrs!!.getAttributeResourceValue(
                            R.styleable.WidgetSpinner_android_entries, 0
                        )

                        if (entriesAttribute != 0) {

                            mItemList.addAll(
                                listOf(
                                    *context.resources.getStringArray(
                                        entriesAttribute
                                    )
                                )
                            )

                            if (isInEditMode) {
                                // Show the 1st item in the list in the designer
                                textViewWidgetSpinnerText.text = mItemList[0]
                            }
                        }
                    }
                }
            }
        } finally {
            typedArray.recycle()
        }
    }

    private fun initPopupWindow() {

        val showView = LayoutInflater.from(context).inflate(R.layout.widget_spinner_drop_down, null)
        //showView.setBackgroundResource(R.drawable.cell_item_blue_bg)

        val recyclerView: RecyclerView = showView.findViewById(R.id.recyclerViewWidgetSpinnerDropList)
        recyclerView.addItemDecoration(HorizontalDividerItemDecoration.Builder(context).build())

        recyclerView.layoutManager = LinearLayoutManager(context)

        mAdapter = WidgetSpinnerAdapter(context, mItemList, CompoundWidgetInterfaces.SelectedItemChanged { position: Int? ->

            mPopupWindow!!.dismiss()

            textViewWidgetSpinnerText.text = mItemList[position!!]

            mAdapter!!.setCurrentIndex(position)
            mAdapter!!.notifyDataSetChanged()

            onItemSelectionChanged?.onSelectionChange(position)
        })

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = mAdapter

        mPopupWindow = PopupWindow(showView)
        //mPopupWindow!!.isOutsideTouchable = true
        //mPopupWindow!!.setBackgroundDrawable(BitmapDrawable())

        mPopupWindow!!.setOnDismissListener {

        }
    }

    fun updateSpinnerItemList(spinnerItemList: List<String?>?) {

        if (spinnerItemList == null || spinnerItemList.size == 0) {
            return
        }

//        setItemList(spinnerItemList)
    }

    private fun showPopupView(context: Context) {

        val height = (mItemList.size * context.resources.getDimension(R.dimen.common_44dp)).toInt()

        // TODO: Should I use the full width of the entries?
        mPopupWindow!!.width = linearLayoutSpinner.measuredWidth

        setDropdownHeight(height)

        val curWindowHeight = if (height > mPopupWindowMaxHeight) {
            mPopupWindowMaxHeight
        } else {
            height
        }

        val rect = Rect()

        getWindowVisibleDisplayFrame(rect)

        if (isCover) {
            val location = IntArray(2)
            textViewWidgetSpinnerText.getLocationOnScreen(location)

            val locationWindow = IntArray(2)
            textViewWidgetSpinnerText.getLocationInWindow(locationWindow)

            if (location[1] + curWindowHeight > rect.bottom) {
                mPopupWindow!!.showAtLocation(
                    textViewWidgetSpinnerText, Gravity.NO_GRAVITY, locationWindow[0], locationWindow[1] + getHeight() - mPopupWindowMaxHeight
                )
            } else {
                mPopupWindow!!.showAtLocation(
                    textViewWidgetSpinnerText, Gravity.NO_GRAVITY, locationWindow[0], locationWindow[1]
                )
            }

        } else {
            mPopupWindow!!.showAsDropDown(textViewWidgetSpinnerText)
        }
    }

    fun setCover(cover: Boolean) {
        isCover = cover
    }

//    fun setItemList(itemList: List<String?>?) {
//
//        mItemList.clear()
//        mItemList.addAll(itemList)
//
//        if (isInEditMode) {
//            // Show the 1st item in the list in the designer
//            textViewWidgetSpinnerText.text = mItemList[0]
//            return
//        }
//
//        mAdapter!!.notifyDataSetChanged()
//        setSelectedIndex(mSelectedIndex)
//    }

    fun setSelection(index: Int) {
        mSelectedIndex = index
        if (index >= 0 && index < mItemList.size) {
            text = mItemList[index]
        }
    }

    fun setSelectedIndex(index: Int) {
        if (index >= 0 && index < mItemList.size) {
            text = mItemList[index]
        }
    }

    fun setText(@StringRes resid: Int) {
        textViewWidgetSpinnerText.setText(resid)
    }

    var text: CharSequence?
        get() = textViewWidgetSpinnerText.text.toString().trim { it <= ' ' }
        set(text) {
            textViewWidgetSpinnerText.text = text
            mAdapter!!.setCurrentIndex(mItemList.indexOf(text))
            mAdapter!!.notifyDataSetChanged()
        }

    fun setDropdownMaxHeight(height: Int) {
        mPopupWindowMaxHeight = height
        mPopupWindow!!.height = calculatePopupWindowHeight()
    }

    /**
     * Set the height of the dropdown menu
     *
     * @param height the height in pixels
     */
    fun setDropdownHeight(height: Int) {

        mPopupWindowHeight = height
        mPopupWindow!!.height = calculatePopupWindowHeight()
    }

    private fun calculatePopupWindowHeight(): Int {

        if (mAdapter == null) {
            return WindowManager.LayoutParams.WRAP_CONTENT
        }

        val listViewHeight = mAdapter!!.itemCount * resources.getDimension(R.dimen.common_44dp)

        if (mPopupWindowMaxHeight > 0 && listViewHeight > mPopupWindowMaxHeight) {
            return mPopupWindowMaxHeight
        } else if (mPopupWindowHeight != WindowManager.LayoutParams.MATCH_PARENT && mPopupWindowHeight != WindowManager.LayoutParams.WRAP_CONTENT && mPopupWindowHeight <= listViewHeight) {
            return mPopupWindowHeight
        }

        return WindowManager.LayoutParams.WRAP_CONTENT
    }
}