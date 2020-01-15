package com.kenargo.compound_widgets

import android.content.Context
import android.util.TypedValue

class Conversions {

    companion object {
        fun spToPx(sp: Float, context: Context): Int {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                sp,
                context.resources.displayMetrics
            ).toInt()
        }

        fun dpToPx(dp: Float, context: Context): Int {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                context.resources.displayMetrics
            ).toInt()
        }

        fun dpToSp(dp: Float, context: Context): Int {
            return (dpToPx(
                dp,
                context
            ) / context.resources.displayMetrics.scaledDensity).toInt()
        }

        fun pxToSp(px: Float, context: Context): Int {
            return (px / context.resources.displayMetrics.scaledDensity).toInt()
        }

        fun pxToDp(px: Float, context: Context): Int {
            return (px / context.resources.displayMetrics.density).toInt()
        }
    }
}