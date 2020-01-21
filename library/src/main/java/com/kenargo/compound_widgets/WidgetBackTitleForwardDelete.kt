package com.kenargo.compound_widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.kenargo.myapplicationlibrary.R
import kotlinx.android.synthetic.main.widget_back_title_forward_delete.view.*

class WidgetBackTitleForwardDelete @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var mWidgetBackTitleForwardDeleteListener: WidgetBackTitleForwardDeleteListener? = null

    private fun initSubView(
        context: Context, attrs: AttributeSet?, defStyleAttr: Int
    ) {
        LayoutInflater.from(context).inflate(R.layout.widget_back_title_forward_delete, this, true)

        applyAttributes(context, attrs, defStyleAttr)

        imageViewWidgetBackTitleForwardDeletePrevious.setOnClickListener {
            mWidgetBackTitleForwardDeleteListener?.onMovePrevious()
        }

        imageViewWidgetBackTitleForwardDeleteNext.setOnClickListener {
            mWidgetBackTitleForwardDeleteListener?.onMoveNext()
        }

        imageViewWidgetBackTitleForwardDeleteDelete.setOnClickListener {
            mWidgetBackTitleForwardDeleteListener?.onDelete()
        }
    }

    private fun applyAttributes(
        context: Context, attrs: AttributeSet?, defStyleAttr: Int
    ) {
        if (attrs == null) {
            return
        }

        val typedArray = context.theme.obtainStyledAttributes(
            attrs, R.styleable.WidgetBackTitleForwardDelete, defStyleAttr, 0
        )

        try {
            for (index in 0 until typedArray.length()) {

                val attribute: Int = try {
                    typedArray.getIndex(index)
                } catch (ignore: Exception) {
                    continue
                }

                if (attribute == R.styleable.WidgetBackTitleForwardDelete_android_text) {
                    textViewWidgetBackTitleForwardDeleteTitle.text = typedArray.getText(R.styleable.WidgetBackTitleForwardDelete_android_text)
                }
            }
        } finally {
            typedArray.recycle()
        }
    }

    var text: CharSequence?
        get() = textViewWidgetBackTitleForwardDeleteTitle.text
        set(title) {
            textViewWidgetBackTitleForwardDeleteTitle.text = title
        }

    fun showDeleteButton(show: Boolean) {
        imageViewWidgetBackTitleForwardDeleteDelete.visibility = if (show) View.VISIBLE else View.GONE
    }

    fun setWidgetBackTitleForwardDeleteListener(widgetBackTitleForwardDeleteListener: WidgetBackTitleForwardDeleteListener?) {
        mWidgetBackTitleForwardDeleteListener = widgetBackTitleForwardDeleteListener
    }

    interface WidgetBackTitleForwardDeleteListener {
        fun onMovePrevious()
        fun onMoveNext()
        fun onDelete()
    }

    init {
        initSubView(context, attrs, defStyleAttr)
    }
}