package com.kenargo.compound_widgets

import android.content.Context
import android.os.*
import android.util.AttributeSet
import android.view.*
import android.widget.FrameLayout
import com.kenargo.myapplicationlibrary.R
import kotlinx.android.synthetic.main.widget_play_pause_control.view.*

class WidgetPlayPauseControl @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        initSubView(context, attrs!!, defStyleAttr)
    }

    interface OnWidgetPlayPauseControlListener {
        fun onLoad()
        fun onSave()
        fun onProperties()
        fun onStart()
        fun onPause()
        fun onStop()
    }

    var onWidgetPlayPauseControlListener: OnWidgetPlayPauseControlListener? = null

    private var supportsLoad = false
    private var supportsSave = false
    private var supportsProperties = false
    private var supportsPause = true
    private var supportsStart = true
    private var supportsCancel = true

    private fun initSubView(context: Context, attrs: AttributeSet, defStyleAttr: Int) {
        LayoutInflater.from(context).inflate(R.layout.widget_play_pause_control, this, true)

        applyAttributes(context, attrs, defStyleAttr)

        // Initialize the controls visibility  states based on attributes

        imageViewWidgetPlayPauseControlLoad.visibility = if (supportsLoad) View.VISIBLE else View.GONE
        imageViewWidgetPlayPauseControlSave.visibility = if (supportsSave) View.VISIBLE else View.GONE
        imageViewWidgetPlayPauseControlStart.visibility = if (supportsStart) View.VISIBLE else View.GONE

        // A few things are hidden even if they are supported as they only show based on flight status
        imageViewWidgetPlayPauseControlPause.visibility = View.GONE

        if (supportsStart && supportsCancel) {
            imageViewWidgetPlayPauseControlStop.visibility = View.GONE
        }

        // Correct, I don't check for null before calling the interactionListener because if there is no interactionListener
        //  then that would be an error on my part and I want to catch it.

        // In edit mode I don't need handlers and callbacks
        if (isInEditMode) {
            return
        }

        imageViewWidgetPlayPauseControlLoad.setOnClickListener { onWidgetPlayPauseControlListener?.onLoad() }
        imageViewWidgetPlayPauseControlSave.setOnClickListener { onWidgetPlayPauseControlListener?.onSave() }
        imageViewWidgetPlayPauseControlProperties.setOnClickListener { onWidgetPlayPauseControlListener?.onProperties() }
        imageViewWidgetPlayPauseControlStart.setOnClickListener { onWidgetPlayPauseControlListener?.onStart() }
        imageViewWidgetPlayPauseControlPause.setOnClickListener { onWidgetPlayPauseControlListener?.onPause() }
        imageViewWidgetPlayPauseControlStop.setOnClickListener { onWidgetPlayPauseControlListener?.onStop() }

        setActivityState(PlayPauseStates.IS_IDLE)
    }

    private fun applyAttributes(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        if (attrs == null) {
            return
        }

        val typedArray = context.theme.obtainStyledAttributes(
            attrs, R.styleable.WidgetPlayPauseControl, defStyleAttr, 0
        )

        try {

            for (index in 0 until typedArray.length()) {

                val attribute: Int

                try {
                    attribute = typedArray.getIndex(index)
                } catch (ignore: Exception) {
                    continue
                }

                when (attribute) {
                    R.styleable.WidgetPlayPauseControl_widgetPlayPauseSupportsLoad -> supportsLoad = typedArray.getBoolean(
                        R.styleable.WidgetPlayPauseControl_widgetPlayPauseSupportsLoad, false
                    )
                    R.styleable.WidgetPlayPauseControl_widgetPlayPauseSupportsSave -> supportsSave = typedArray.getBoolean(
                        R.styleable.WidgetPlayPauseControl_widgetPlayPauseSupportsSave, false
                    )
                    R.styleable.WidgetPlayPauseControl_widgetPlayPauseSupportsProperties -> supportsProperties = typedArray.getBoolean(
                        R.styleable.WidgetPlayPauseControl_widgetPlayPauseSupportsProperties, false
                    )
                    R.styleable.WidgetPlayPauseControl_widgetPlayPauseSupportsPause -> supportsPause = typedArray.getBoolean(
                        R.styleable.WidgetPlayPauseControl_widgetPlayPauseSupportsPause, true
                    )
                    R.styleable.WidgetPlayPauseControl_widgetPlayPauseSupportsStart -> supportsStart = typedArray.getBoolean(
                        R.styleable.WidgetPlayPauseControl_widgetPlayPauseSupportsStart, true
                    )
                    R.styleable.WidgetPlayPauseControl_widgetPlayPauseSupportsCancel -> supportsCancel = typedArray.getBoolean(
                        R.styleable.WidgetPlayPauseControl_widgetPlayPauseSupportsCancel, true
                    )
                }
            }

        } finally {
            typedArray.recycle()
        }
    }

    private var isPlanningMode = false
        set(value) {
            field = value

            if (value) {
                imageViewWidgetPlayPauseControlLoad.visibility = if (supportsLoad) View.VISIBLE else View.GONE
                imageViewWidgetPlayPauseControlSave.visibility = if (supportsSave) View.VISIBLE else View.GONE
                imageViewWidgetPlayPauseControlProperties.visibility = if (supportsProperties) View.VISIBLE else View.GONE

                // Hide all flight controls
                imageViewWidgetPlayPauseControlStart.visibility = View.GONE
                imageViewWidgetPlayPauseControlPause.visibility = View.GONE
                imageViewWidgetPlayPauseControlStop.visibility = View.GONE
            }
        }

    enum class PlayPauseStates {
        IS_IDLE,
        IS_PLAYING,
        IS_PAUSED
    }

    private var currentState = PlayPauseStates.IS_IDLE

    fun setActivityState(playPauseStates: PlayPauseStates) {

        if (isPlanningMode) {
            // Nothing to change; planning mode is configured in the property setter
            return
        }

        currentState = playPauseStates

        // If I'm not on the UI thread, call myself on the UI thread
        if (Looper.getMainLooper().thread !== Thread.currentThread()) {

            // I don't need this version; it works but I'm in a view so I have a looper
            Handler(Looper.getMainLooper()).post {
                setActivityState(playPauseStates)
            }
            return
        }

        if (playPauseStates != PlayPauseStates.IS_IDLE) {

            if (supportsPause) {

                if (playPauseStates == PlayPauseStates.IS_PAUSED) {
                    imageViewWidgetPlayPauseControlStart.visibility = View.VISIBLE
                    imageViewWidgetPlayPauseControlPause.visibility = View.GONE
                } else {
                    imageViewWidgetPlayPauseControlStart.visibility = View.GONE
                    imageViewWidgetPlayPauseControlPause.visibility = View.VISIBLE
                }
            } else {
                imageViewWidgetPlayPauseControlPause.visibility = View.GONE
                imageViewWidgetPlayPauseControlStart.visibility = View.GONE
            }

            // Flying, disable all edit operations

            if (supportsLoad) {
                imageViewWidgetPlayPauseControlLoad.visibility = View.GONE
            }

            if (supportsProperties) {
                imageViewWidgetPlayPauseControlProperties.visibility = View.GONE
            }

            if (supportsCancel) {
                // You can always cancel missionTask
                imageViewWidgetPlayPauseControlStop.visibility = View.VISIBLE
            }

            // Information: You can always save if it's supported.
            //  If it's not supported then the control is already hidden so nothing to do
        } else {

            // FlightMode is not active

            imageViewWidgetPlayPauseControlLoad.visibility = if (supportsLoad) {
                View.VISIBLE
            } else {
                View.GONE
            }

            imageViewWidgetPlayPauseControlSave.visibility = if (supportsSave) {
                View.VISIBLE
            } else {
                View.GONE
            }

            imageViewWidgetPlayPauseControlProperties.visibility = if (supportsProperties) {
                View.VISIBLE
            } else {
                View.GONE
            }

            if (supportsStart) {
                imageViewWidgetPlayPauseControlStart.visibility = View.VISIBLE
            }

            // If flight mode isn't active then pause and stop are always gone
            imageViewWidgetPlayPauseControlPause.visibility = View.GONE
            imageViewWidgetPlayPauseControlStop.visibility = View.GONE
        }
    }
}