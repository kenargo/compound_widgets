package com.kenargo.compound_widgets.widgetSpinner

import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.view.*
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.kenargo.compound_widgets.CompoundWidgetInterfaces
import com.kenargo.compound_widgets.widgetSpinner.WidgetSpinnerAdapter.WidgetViewHolder
import com.kenargo.myapplicationlibrary.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.widget_spinner_item.*

open class WidgetSpinnerAdapter : RecyclerView.Adapter<WidgetViewHolder> {

    private val context: Context

    private val mList: List<String>

    private var selectPosition = -1

    private var onSelectedItemChanged: CompoundWidgetInterfaces.OnSelectedItemChanged

    fun setOnSelectedItemChangedListener(listener: CompoundWidgetInterfaces.OnSelectedItemChanged) {
        this.onSelectedItemChanged = listener
    }

    constructor(
            context: Context, list: List<String>, position: Int, onItemSelectionChangedOn: CompoundWidgetInterfaces.OnSelectedItemChanged
    ) {
        this.context = context
        mList = list
        this.onSelectedItemChanged = onItemSelectionChangedOn
        selectPosition = position
    }

    constructor(
            context: Context, list: List<String>, onItemSelectionChangedOn: CompoundWidgetInterfaces.OnSelectedItemChanged
    ) {
        this.context = context
        mList = list
        this.onSelectedItemChanged = onItemSelectionChangedOn
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WidgetViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.widget_spinner_item, parent, false)
        val widgetViewHolder = WidgetViewHolder(view, (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK))
        view.setOnClickListener { onSelectedItemChanged.onSelectionChange(widgetViewHolder.adapterPosition) }
        return widgetViewHolder
    }

    override fun onBindViewHolder(holder: WidgetViewHolder, position: Int) {
        holder.bind(mList[position], (position == selectPosition))
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun setCurrentIndex(currentIndex: Int) {
        selectPosition = currentIndex
    }

    class WidgetViewHolder (override val containerView: View?, private val currentNightMode: Int) : RecyclerView.ViewHolder(containerView!!.rootView),
        LayoutContainer {

        fun bind (listItem: String?, isSelected: Boolean = false) {

            textViewWidgetSpinnerItem.text = listItem

            if (currentNightMode == Configuration.UI_MODE_NIGHT_NO ||
                currentNightMode == Configuration.UI_MODE_NIGHT_UNDEFINED) {

                relativeViewWidgetSpinnerItem.setBackgroundColor(Color.WHITE)
                textViewWidgetSpinnerItem.setTextColor(if (isSelected) Color.BLUE else Color.DKGRAY)
            } else {
                relativeViewWidgetSpinnerItem.setBackgroundColor(Color.DKGRAY)
                textViewWidgetSpinnerItem.setTextColor(if (isSelected) Color.CYAN else Color.WHITE)
            }

            imageViewWidgetSpinnerItemSelected.visibility = if (isSelected) View.VISIBLE else View.INVISIBLE
        }
    }
}