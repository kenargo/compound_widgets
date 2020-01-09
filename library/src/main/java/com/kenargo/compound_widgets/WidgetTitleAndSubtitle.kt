package com.kenargo.compound_widgets

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.*
import android.widget.FrameLayout
import com.kenargo.myapplicationlibrary.R
import kotlinx.android.synthetic.main.widget_title_and_subtitle.view.*

class WidgetTitleAndSubtitle @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        initSubView(context, attrs!!, defStyleAttr)
    }

    private fun initSubView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        LayoutInflater.from(context).inflate(R.layout.widget_title_and_subtitle, this, true)

        applyAttributes(context, attrs, defStyleAttr)
    }

    private fun applyAttributes(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        if (attrs == null) {
            return
        }

        val typedArray = context.theme.obtainStyledAttributes(
            attrs, R.styleable.WidgetTitleAndSubtitle, defStyleAttr, 0
        )

        try {
            for (index in 0 until typedArray.length()) {

                val attribute = try {
                    typedArray.getIndex(index)
                } catch (ignore: Exception) {
                    continue
                }

                when (attribute) {
                    R.styleable.WidgetTitleAndSubtitle_android_text -> {
                        textViewWidgetTitleAndSubtitleTitle.text = typedArray.getText(R.styleable.WidgetTitleAndSubtitle_android_text)
                    }
                    R.styleable.WidgetTitleAndSubtitle_android_subtitle -> {
                        textViewWidgetTitleAndSubtitleSubtitle.text = typedArray.getText(R.styleable.WidgetTitleAndSubtitle_android_subtitle)
                    }
                }
            }

        } finally {
            typedArray.recycle()
        }
    }

    fun setText(title: CharSequence?) {
        textViewWidgetTitleAndSubtitleTitle.text = title
    }

    fun setSubtitle(subTitle: CharSequence?) {
        if (TextUtils.isEmpty(textViewWidgetTitleAndSubtitleSubtitle.text)) {
            textViewWidgetTitleAndSubtitleSubtitle.visibility = View.GONE
        } else {
            textViewWidgetTitleAndSubtitleSubtitle.text = subTitle
            textViewWidgetTitleAndSubtitleSubtitle.visibility = View.VISIBLE
        }
    }
}