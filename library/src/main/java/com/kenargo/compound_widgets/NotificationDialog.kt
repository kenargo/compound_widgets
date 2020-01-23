package com.kenargo.compound_widgets

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import com.kenargo.myapplicationlibrary.R
import hakobastvatsatryan.DropdownTextView

class NotificationDialog : DialogFragment() {

    private var timeout: Int? = null

    // All possible widgets across all Notification Dialog layout files
    private var imageViewNotificationDialogTypeIcon: ImageView? = null
    private var textViewNotificationDialogTitle: TextView? = null
    private var textViewNotificationDialogMessage: TextView? = null
    private var textViewNotificationDialogDescription: DropdownTextView? = null
    private var checkBoxTextNavigationDialog: CheckBox? = null
    private var textViewNotificationDialogNegativeResponse: Button? = null
    private var textViewNotificationDialogNeutralResponse: Button? = null
    private var textViewNotificationDialogPositiveResponse: Button? = null
    private var progressBarNotificationDialog: ProgressBar? = null
    private var widgetTitleAndSeekBarNotificationDialog: WidgetTitleAndSeekBar? = null

    enum class NotificationDialogTypes {
        ONE_BUTTON,
        ONE_BUTTON_NO_TITLE,
        ONE_BUTTON_AND_PROGRESS,
        ONE_BUTTONS_AND_EDIT_TEXT,
        ONE_BUTTONS_AND_SEEKBAR,
        TWO_BUTTONS,
        TWO_BUTTONS_NO_TITLE,
        TWO_BUTTONS_AND_PROGRESS,
        TWO_BUTTONS_AND_EDIT_TEXT,
        TWO_BUTTONS_AND_SEEKBAR,
        THREE_BUTTONS,
        THREE_BUTTONS_NO_TITLE,
        THREE_BUTTONS_AND_PROGRESS,
        THREE_BUTTONS_AND_EDIT_TEXT,
        THREE_BUTTONS_AND_SEEKBAR
    }

    private lateinit var builder: Builder

    private fun apply(builder: Builder) {
        this.builder = builder
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val layoutResourceID: Int

        when (builder.notificationDialogType) {

            NotificationDialogTypes.ONE_BUTTON_AND_PROGRESS, NotificationDialogTypes.TWO_BUTTONS_AND_PROGRESS, NotificationDialogTypes.THREE_BUTTONS_AND_PROGRESS -> {
                layoutResourceID = R.layout.notification_progress_dialog
            }

            NotificationDialogTypes.ONE_BUTTONS_AND_EDIT_TEXT, NotificationDialogTypes.TWO_BUTTONS_AND_EDIT_TEXT, NotificationDialogTypes.THREE_BUTTONS_AND_EDIT_TEXT -> {
                layoutResourceID = R.layout.notification_edit_text_dialog
            }

            NotificationDialogTypes.ONE_BUTTONS_AND_SEEKBAR, NotificationDialogTypes.TWO_BUTTONS_AND_SEEKBAR, NotificationDialogTypes.THREE_BUTTONS_AND_SEEKBAR -> {
                layoutResourceID = R.layout.notification_seekbar_dialog
            }

            // Covered by else:
            // NotificationDialogTypes.ONE_BUTTON,
            // NotificationDialogTypes.TWO_BUTTONS,
            // NotificationDialogTypes.THREE_BUTTONS,
            // NotificationDialogTypes.ONE_BUTTON_NO_TITLE,
            // NotificationDialogTypes.TWO_BUTTONS_NO_TITLE,
            // NotificationDialogTypes.THREE_BUTTONS_NO_TITLE
            else -> {
                layoutResourceID = R.layout.notification_dialog
            }
        }

        val view = inflater.inflate(layoutResourceID, container, false)

        dialog!!.window!!.attributes!!.windowAnimations = R.style.DialogAnimations
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val groupTitleAndHeader = view.findViewById<View>(R.id.groupTitleAndHeader)

        // Try and get all the various widgets from all known Notification Dialog layouts
        imageViewNotificationDialogTypeIcon = view.findViewById(R.id.imageViewNotificationDialogTypeIcon)

        textViewNotificationDialogTitle = view.findViewById(R.id.textViewNotificationDialogTitle)
        textViewNotificationDialogMessage = view.findViewById(R.id.textViewNotificationDialogMessage)
        textViewNotificationDialogDescription = view.findViewById(R.id.textViewNotificationDialogDescription)
        checkBoxTextNavigationDialog = view.findViewById(R.id.checkBoxTextNavigationDialog)

        textViewNotificationDialogNegativeResponse = view.findViewById(R.id.textViewNotificationDialogNegativeResponse)
        textViewNotificationDialogNeutralResponse = view.findViewById(R.id.textViewNotificationDialogNeutralResponse)
        textViewNotificationDialogPositiveResponse = view.findViewById(R.id.textViewNotificationDialogPositiveResponse)

        progressBarNotificationDialog = view.findViewById(R.id.progressBarNotificationDialog)

        widgetTitleAndSeekBarNotificationDialog = view.findViewById(R.id.widgetTitleAndSeekBarNotificationDialog)

        val dividerNotificationDialogNegativeResponse = view.findViewById<View>(R.id.dividerNotificationDialogNegativeResponse)
        val dividerNotificationDialogNeutralResponse = view.findViewById<View>(R.id.dividerNotificationDialogNeutralResponse)

        when (builder.notificationDialogType) {
            NotificationDialogTypes.ONE_BUTTON, NotificationDialogTypes.ONE_BUTTON_AND_PROGRESS, NotificationDialogTypes.ONE_BUTTONS_AND_EDIT_TEXT, NotificationDialogTypes.ONE_BUTTONS_AND_SEEKBAR -> {
                textViewNotificationDialogNegativeResponse!!.visibility = View.GONE
                dividerNotificationDialogNegativeResponse.visibility = View.GONE

                textViewNotificationDialogNeutralResponse!!.visibility = View.GONE
                dividerNotificationDialogNeutralResponse.visibility = View.GONE
            }

            NotificationDialogTypes.ONE_BUTTON_NO_TITLE -> {
                textViewNotificationDialogNegativeResponse!!.visibility = View.GONE
                dividerNotificationDialogNegativeResponse.visibility = View.GONE

                textViewNotificationDialogNeutralResponse!!.visibility = View.GONE
                dividerNotificationDialogNeutralResponse.visibility = View.GONE

                groupTitleAndHeader.visibility = View.GONE
            }

            NotificationDialogTypes.TWO_BUTTONS, NotificationDialogTypes.TWO_BUTTONS_AND_PROGRESS, NotificationDialogTypes.TWO_BUTTONS_AND_EDIT_TEXT, NotificationDialogTypes.TWO_BUTTONS_AND_SEEKBAR -> {
                textViewNotificationDialogNeutralResponse!!.visibility = View.GONE
                dividerNotificationDialogNeutralResponse.visibility = View.GONE
            }

            NotificationDialogTypes.TWO_BUTTONS_NO_TITLE -> {
                textViewNotificationDialogNeutralResponse!!.visibility = View.GONE
                dividerNotificationDialogNeutralResponse.visibility = View.GONE
                groupTitleAndHeader.visibility = View.GONE
            }

            NotificationDialogTypes.THREE_BUTTONS, NotificationDialogTypes.THREE_BUTTONS_AND_PROGRESS, NotificationDialogTypes.THREE_BUTTONS_AND_EDIT_TEXT, NotificationDialogTypes.THREE_BUTTONS_AND_SEEKBAR -> {
            }

            NotificationDialogTypes.THREE_BUTTONS_NO_TITLE -> {
                groupTitleAndHeader.visibility = View.GONE
            }
        }

        builder.timeout?.let { timeout = it }

        builder.notificationDialogIcon?.let { setType(it) }

        builder.title?.let { textViewNotificationDialogTitle!!.text = it }

        builder.message?.let { textViewNotificationDialogMessage!!.text = it }

        builder.descriptionText?.let { textViewNotificationDialogDescription?.setContentText(it.toString()) }

        if (textViewNotificationDialogDescription?.getContentTextView()!!.text!!.isNullOrEmpty()) {
            textViewNotificationDialogDescription?.visibility = View.GONE
        }

        builder.checkBoxText?.let { checkBoxTextNavigationDialog?.text = it }

        if (builder.checkBoxText.isNullOrEmpty()) {
            checkBoxTextNavigationDialog?.visibility = View.GONE
        }

        builder.negativeButton?.let { textViewNotificationDialogNegativeResponse?.text = it }

        builder.neutralButton?.let { textViewNotificationDialogNeutralResponse?.text = it }

        builder.positiveButton?.let { textViewNotificationDialogPositiveResponse?.text = it }

        builder.progress?.let { progressBarNotificationDialog?.setProgress(it) }
        builder.progressIndeterminant?.let { progressBarNotificationDialog?.isIndeterminate = it }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.progressMin?.let { progressBarNotificationDialog?.min = it }
        }

        builder.progressMax?.let { progressBarNotificationDialog?.max = it }

        if (builder.cancelable || builder.timeout != null) {
            // If the caller specified cancellable or this is a timeout dialog (which should allow
            //  the user to cancel then set cancellable)
            isCancelable = true
        }

        textViewNotificationDialogNegativeResponse?.setOnClickListener {
            dismiss()
            builder.onClickListener?.onClick(null, DialogInterface.BUTTON_NEGATIVE)
        }

        textViewNotificationDialogNeutralResponse?.setOnClickListener {
            dismiss()
            builder.onClickListener?.onClick(null, DialogInterface.BUTTON_NEUTRAL)
        }

        textViewNotificationDialogPositiveResponse?.setOnClickListener {
            dismiss()
            builder.onClickListener?.onClick(null, DialogInterface.BUTTON_POSITIVE)
        }
    }

    private fun setType(type: NotificationDialogIcons?) {

        // TODO: I want the icon on the side and not in the title area
        if (imageViewNotificationDialogTypeIcon != null) {
            when (type) {
                NotificationDialogIcons.SUCCESS -> {
                    imageViewNotificationDialogTypeIcon!!.setImageResource(R.drawable.icon_dialog_success)
                    textViewNotificationDialogTitle!!.text = getString(R.string.notificationDialogSuccess)
                }
                NotificationDialogIcons.WARNING -> {
                    imageViewNotificationDialogTypeIcon!!.setImageResource(R.drawable.icon_dialog_warning)
                    textViewNotificationDialogTitle!!.text = getString(R.string.notificationDialogWarning)
                }
                NotificationDialogIcons.INFORMATION -> {
                    imageViewNotificationDialogTypeIcon!!.setImageResource(R.drawable.icon_dialog_information)
                    textViewNotificationDialogTitle!!.text = getString(R.string.notificationDialogInformation)
                }
                NotificationDialogIcons.QUESTION -> {
                    imageViewNotificationDialogTypeIcon!!.setImageResource(R.drawable.icon_dialog_question)
                    textViewNotificationDialogTitle!!.text = getString(R.string.notificationDialogQuestion)
                }
                NotificationDialogIcons.FAILURE -> {
                    imageViewNotificationDialogTypeIcon!!.setImageResource(R.drawable.icon_dialog_failure)
                    textViewNotificationDialogTitle!!.text = getString(R.string.notificationDialogFailure)
                }
            }
        }
    }

    // Most values are only set in the builder but a few are exposed for dynamic setting
    fun setMessage(message: String) {
        textViewNotificationDialogMessage!!.text = message
    }

    override fun onStart() {
        super.onStart()
        timeout?.let {
            handler = Handler()
            handler?.postDelayed(runnable, TIMEOUT_VALUE.toLong())
        }
    }

    override fun dismiss() {
        super.dismiss()
        builder.onDismissListener?.onDismiss(null)
    }

    private var handler: Handler? = null

    private val runnable = Runnable {

        dismiss()

        builder.onDismissListener?.onDismiss(null)
    }

    enum class NotificationDialogIcons {
        SUCCESS,
        WARNING,
        INFORMATION,
        QUESTION,
        FAILURE
    }

    class Builder constructor(
        var context: Context

    ) {
        var notificationDialogType: NotificationDialogTypes? = null
            private set

        var notificationDialogIcon: NotificationDialogIcons? = null
            private set

        var title: CharSequence? = null
            private set

        var message: CharSequence? = null
            private set

        var descriptionText: CharSequence? = null
            private set

        var negativeButton: CharSequence? = null
            private set

        var neutralButton: CharSequence? = null
            private set

        var positiveButton: CharSequence? = null
            private set

        var checkBoxText: CharSequence? = null
            private set

        var timeout: Int? = null
            private set

        var progress: Int? = null
            private set

        var progressIndeterminant: Boolean? = null
            private set

        var progressMin: Int? = null
            private set

        var progressMax: Int? = null
            private set

        var cancelable: Boolean = false
            private set

        var onDismissListener: DialogInterface.OnDismissListener? = null
            private set

        var onClickListener: DialogInterface.OnClickListener? = null
            private set

        fun build(): NotificationDialog {
            return newInstance(this)
        }

        fun setNotificationType(notificationDialogType: NotificationDialogTypes) = apply {
            this.notificationDialogType = notificationDialogType
        }

        fun setNotificationIcon(type: NotificationDialogIcons) = apply {
            this.notificationDialogIcon = type
        }

        fun setText(text: String) = apply {
            this.title = text
        }

        fun setMessage(text: String) = apply {
            this.message = text
        }

        fun setDescriptionText(text: String) = apply {
            this.descriptionText = text
        }

        fun setNegativeButton(text: String) = apply {
            this.negativeButton = text
        }

        fun setNeutralButton(text: String) = apply {
            this.neutralButton = text
        }

        fun setPositiveButton(text: String) = apply {
            this.positiveButton = text
        }

        fun setCheckBoxText(text: String) = apply {
            this.checkBoxText = text
        }

        fun setTimeout(timeout: Int?) = apply {
            this.timeout = timeout
        }

        fun setProgress(progress: Int?) = apply {
            this.progress = progress
        }

        fun setProgressIndeterminant(indeterminate: Boolean?) = apply {
            this.progressIndeterminant = indeterminate
        }

        fun setProgressMin(min: Int?) = apply {
            this.progressMin = min
        }

        fun setProgressMax(max: Int?) = apply {
            this.progressMax = max
        }

        fun setCancelable(cancelable: Boolean) = apply {
            this.cancelable = cancelable
        }

        fun setOnDismissListener(listener: DialogInterface.OnDismissListener?) = apply {
            this.onDismissListener = listener
        }

        fun setOnClickListener(listener: DialogInterface.OnClickListener?) = apply {
            this.onClickListener = listener
        }
    }

    companion object {

        private const val TIMEOUT_VALUE = 5000

        fun newInstance(builder: Builder): NotificationDialog {
            val notificationDialog = NotificationDialog()
            notificationDialog.apply(builder)
            return notificationDialog
        }
    }
}