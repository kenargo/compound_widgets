package com.kenargo.compoundwidgetsampleapp

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.widget.CompoundButton
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ashraf007.expandableselectionview.ExpandableSelectionViewInterfaces
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

        val notificationDialogResourceIds = arrayOf(
            NotificationDialogTypes.ONE_BUTTON,
            NotificationDialogTypes.ONE_BUTTON_NO_TITLE,
            NotificationDialogTypes.ONE_BUTTON_AND_PROGRESS,
            NotificationDialogTypes.ONE_BUTTONS_AND_EDIT_TEXT,
            NotificationDialogTypes.ONE_BUTTONS_AND_SEEKBAR,
            NotificationDialogTypes.TWO_BUTTONS,
            NotificationDialogTypes.TWO_BUTTONS_NO_TITLE,
            NotificationDialogTypes.TWO_BUTTONS_AND_PROGRESS,
            NotificationDialogTypes.TWO_BUTTONS_AND_EDIT_TEXT,
            NotificationDialogTypes.TWO_BUTTONS_AND_SEEKBAR,
            NotificationDialogTypes.THREE_BUTTONS,
            NotificationDialogTypes.THREE_BUTTONS_NO_TITLE,
            NotificationDialogTypes.THREE_BUTTONS_AND_PROGRESS,
            NotificationDialogTypes.THREE_BUTTONS_AND_EDIT_TEXT,
            NotificationDialogTypes.THREE_BUTTONS_AND_SEEKBAR
        )

        expandableSingleSelectionView!!.setOnSelectionChange(ExpandableSelectionViewInterfaces.SelectedItemChanged {

            it?.let {

                Builder(this)
                    .setNotificationType(notificationDialogResourceIds[it])
                    .setNotificationIcon(NotificationDialogIcons.WARNING)
                    .setMessage("This is some error message you might want to show the user")
                    .setDescriptionText("You can also have a varying length of a description text that includes some details on how to resolve the issue")
                    .setPositiveButton("OK")
                    .setOnDismissListener(DialogInterface.OnDismissListener {
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
                Toast.makeText(applicationContext, "Previous Clocked", Toast.LENGTH_SHORT).show()
            }

            override fun onMoveNext() {
                Toast.makeText(applicationContext, "Next Clocked", Toast.LENGTH_SHORT).show()
            }

            override fun onDelete() {
                Toast.makeText(applicationContext, "Delete Clocked", Toast.LENGTH_SHORT).show()
            }
        })

        widgetTitleAndSeekBarEditText.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.d("ASDF", "Value: $progress")

                widgetTitleAndSwitchSeekBar.setProgress(progress)
                widgetMinMaxSeekBar.setProgress(progress)
                widgetTitleAndSeekBar.setProgress(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                Log.d("ASDF", "Value: ${seekBar!!.progress}")
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                Log.d("ASDF", "Value: ${seekBar!!.progress}")
            }
        })

        widgetTitleAndSwitchSeekBar.setOnCheckedChangedListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        })

        widgetTitleAndSpinner.setOnSelectionChange(CompoundWidgetInterfaces.SelectedItemChanged {

        })


    }
}