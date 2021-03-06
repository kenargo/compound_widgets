package com.kenargo.compoundwidgetsampleapp

import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kenargo.compound_widgets.CompoundWidgetInterfaces
import com.kenargo.compound_widgets.NotificationDialog.*
import com.kenargo.compound_widgets.WidgetBackTitleForwardDelete
import kotlinx.android.synthetic.main.controls_preview.*


class ControlsKotlinPreview : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.controls_preview)

        widgetTitleAndSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.d("Example", "onProgressChanged: $progress, fromUser=$fromUser")
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                Log.d("Example", "onProgressChanged: $seekBar.progress")
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                Log.d("Example", "onProgressChanged: $seekBar/progress")
            }
        })

        widgetTitleAndSeekBar.setOnValueUpdatedListener(CompoundWidgetInterfaces.OnProgressValueUpdatedListener {
            return@OnProgressValueUpdatedListener "About: $it"
        })

        val notificationDialogResourceIds = arrayOf(
            NotificationDialogTypes.ONE_BUTTON,
            NotificationDialogTypes.ONE_BUTTON_NO_TITLE,
            NotificationDialogTypes.ONE_BUTTON_AND_PROGRESS,
            NotificationDialogTypes.ONE_BUTTONS_AND_EDIT_TEXT,
            NotificationDialogTypes.ONE_BUTTONS_AND_SEEKBAR,
            NotificationDialogTypes.ONE_BUTTONS_AND_SEEKBAR_EDIT_TEXT,

            NotificationDialogTypes.TWO_BUTTONS,
            NotificationDialogTypes.TWO_BUTTONS_NO_TITLE,
            NotificationDialogTypes.TWO_BUTTONS_AND_PROGRESS,
            NotificationDialogTypes.TWO_BUTTONS_AND_EDIT_TEXT,
            NotificationDialogTypes.TWO_BUTTONS_AND_SEEKBAR,
            NotificationDialogTypes.TWO_BUTTONS_AND_SEEKBAR_EDIT_TEXT,

            NotificationDialogTypes.THREE_BUTTONS,
            NotificationDialogTypes.THREE_BUTTONS_NO_TITLE,
            NotificationDialogTypes.THREE_BUTTONS_AND_PROGRESS,
            NotificationDialogTypes.THREE_BUTTONS_AND_EDIT_TEXT,
            NotificationDialogTypes.THREE_BUTTONS_AND_SEEKBAR,
            NotificationDialogTypes.THREE_BUTTONS_AND_SEEKBAR_EDIT_TEXT
        )

        widgetSpinner.setOnSelectedItemChangedListener(CompoundWidgetInterfaces.OnSelectedItemChanged {

            it?.let {

                Builder(this)
                    .setNotificationType(notificationDialogResourceIds[it])
                    .setNotificationIcon(NotificationDialogIcons.NONE)
                    .setMessage("This is some error message you might want to show the user")
                    .setDescriptionText("You can also have a varying length of a description text that includes some details on how to resolve the issue")
                    .setPositiveButton("OK")
                    .setOnDismissListener(CompoundWidgetInterfaces.OnDismissListener {
                        Toast.makeText(this, "Notification dialog dismissed", Toast.LENGTH_SHORT)
                            .show()
                    })
                    .build()
                    .show(supportFragmentManager, "fragment_edit_name")
            }
        })

        widgetBackTitleForwardDelete!!.setWidgetBackTitleForwardDeleteListener(object :
            WidgetBackTitleForwardDelete.WidgetBackTitleForwardDeleteListener {
            override fun onMovePrevious() {
                Toast.makeText(applicationContext, "Previous Clicked", Toast.LENGTH_SHORT).show()
            }

            override fun onMoveNext() {
                Toast.makeText(applicationContext, "Next Clicked", Toast.LENGTH_SHORT).show()
            }

            override fun onDelete() {
                Toast.makeText(applicationContext, "Delete Clicked", Toast.LENGTH_SHORT).show()
            }
        })

        widgetTitleAndSeekBarEditText.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.d("ASDF", "Value: $progress")

                //widgetTitleAndSwitchSeekBar.setProgress(progress)
                //widgetMinMaxSeekBar.setProgress(progress, true)
                //widgetTitleAndSeekBar.setProgress(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                Log.d("ASDF", "Value: ${seekBar!!.progress}")
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                Log.d("ASDF", "Value: ${seekBar!!.progress}")
            }
        })

        //widgetTitleAndSeekBarEditText.setOnValueUpdatedListener(object : OnValueUpdatedListener {
        //    override fun onProgressValueUpdated(value: Int): String {
        //        return ">>$value"
        //    }
        //
        //    override fun onUserInputChanged(value: String): Int {
        //        return try {
        //            value.substring(2).toInt()
        //        } catch (ignore: NumberFormatException) {
        //            // returning MIN_VALUE will result in no change to progress
        //            Int.MIN_VALUE
        //        }
        //    }
        //})
        //
        widgetTitleAndSeekBarEditText.setProgress(0)

        widgetTitleAndSwitchSeekBar.setOnCheckedChangedListener(CompoundWidgetInterfaces.OnCheckedChangeListener { _, _ ->
            Toast.makeText(applicationContext, "widgetTitleAndSwitchSeekBar Clicked", Toast.LENGTH_SHORT).show()
        })

        widgetTitleAndSpinner.setOnSelectedItemChangedListener(CompoundWidgetInterfaces.OnSelectedItemChanged {
            Toast.makeText(applicationContext, "widgetTitleAndSpinner item: $it", Toast.LENGTH_SHORT).show()
        })
    }
}