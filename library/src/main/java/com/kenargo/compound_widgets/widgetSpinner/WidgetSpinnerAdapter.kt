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

open class WidgetSpinnerAdapter : RecyclerView.Adapter<WidgetViewHolder> {

    private val context: Context

    private val mList: List<String>

    private var selectPosition = -1

    private var onItemSelectionChanged: CompoundWidgetInterfaces.SelectedItemChanged

    fun setOnItemClickListener(onItemSelectionChanged: CompoundWidgetInterfaces.SelectedItemChanged) {
        this.onItemSelectionChanged = onItemSelectionChanged
    }

    constructor(
        context: Context, list: List<String>, position: Int, onItemSelectionChanged: CompoundWidgetInterfaces.SelectedItemChanged
    ) {
        this.context = context
        mList = list
        this.onItemSelectionChanged = onItemSelectionChanged
        selectPosition = position
    }

    constructor(
        context: Context, list: List<String>, onItemSelectionChanged: CompoundWidgetInterfaces.SelectedItemChanged
    ) {
        this.context = context
        mList = list
        this.onItemSelectionChanged = onItemSelectionChanged
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WidgetViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.widget_spinner_item, parent, false)
        val widgetViewHolder = WidgetViewHolder(view)
        view.setOnClickListener { onItemSelectionChanged.onSelectionChange(widgetViewHolder.adapterPosition) }
        return widgetViewHolder
    }

    override fun onBindViewHolder(holder: WidgetViewHolder, position: Int) {

        holder.textViewWidgetSpinnerItem.text = mList[position]

        // https://medium.com/androiddevelopers/appcompat-v23-2-daynight-d10f90c83e94#.28mjofc2z
        // Good article on dark mode and how Android implements the different states
        val currentNightMode = (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK)

        if (currentNightMode == Configuration.UI_MODE_NIGHT_NO ||
            currentNightMode == Configuration.UI_MODE_NIGHT_UNDEFINED) {

            holder.relativeViewWidgetSpinnerItem.setBackgroundColor(Color.WHITE)
            holder.textViewWidgetSpinnerItem.setTextColor(if (position == selectPosition) Color.BLUE else Color.DKGRAY)
        } else {
            holder.relativeViewWidgetSpinnerItem.setBackgroundColor(Color.DKGRAY)
            holder.textViewWidgetSpinnerItem.setTextColor(if (position == selectPosition) Color.CYAN else Color.WHITE)
        }

        holder.imageViewWidgetSpinnerItemSelected.visibility = if (position == selectPosition) View.VISIBLE else View.INVISIBLE
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun setCurrentIndex(currentIndex: Int) {
        selectPosition = currentIndex
    }

    class WidgetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val relativeViewWidgetSpinnerItem: View = itemView.findViewById(R.id.relativeViewWidgetSpinnerItem)
        val textViewWidgetSpinnerItem: TextView = itemView.findViewById(R.id.textViewWidgetSpinnerItem)
        val imageViewWidgetSpinnerItemSelected: ImageView = itemView.findViewById(R.id.imageViewWidgetSpinnerItemSelected)
    }
}