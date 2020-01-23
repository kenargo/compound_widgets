package com.kenargo.compoundwidgetsampleapp;

import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.kenargo.compound_widgets.CompoundWidgetInterfaces;
import com.kenargo.compound_widgets.NotificationDialog;
import com.kenargo.compound_widgets.WidgetTitleAndSeekBar;
import com.kenargo.compound_widgets.WidgetTitleAndSeekBarEditText;
import com.kenargo.compound_widgets.widgetSpinner.WidgetSpinner;

public class ControlsJavaPreview extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.controls_preview);

        NotificationDialog.NotificationDialogTypes[] notificationDialogResourceIds = {
                NotificationDialog.NotificationDialogTypes.ONE_BUTTON,
                NotificationDialog.NotificationDialogTypes.ONE_BUTTON_NO_TITLE,
                NotificationDialog.NotificationDialogTypes.ONE_BUTTON_AND_PROGRESS,
                NotificationDialog.NotificationDialogTypes.ONE_BUTTONS_AND_EDIT_TEXT,
                NotificationDialog.NotificationDialogTypes.ONE_BUTTONS_AND_SEEKBAR,

                NotificationDialog.NotificationDialogTypes.TWO_BUTTONS,
                NotificationDialog.NotificationDialogTypes.TWO_BUTTONS_NO_TITLE,
                NotificationDialog.NotificationDialogTypes.TWO_BUTTONS_AND_PROGRESS,
                NotificationDialog.NotificationDialogTypes.TWO_BUTTONS_AND_EDIT_TEXT,
                NotificationDialog.NotificationDialogTypes.TWO_BUTTONS_AND_SEEKBAR,

                NotificationDialog.NotificationDialogTypes.THREE_BUTTONS,
                NotificationDialog.NotificationDialogTypes.THREE_BUTTONS_NO_TITLE,
                NotificationDialog.NotificationDialogTypes.THREE_BUTTONS_AND_PROGRESS,
                NotificationDialog.NotificationDialogTypes.THREE_BUTTONS_AND_EDIT_TEXT,
                NotificationDialog.NotificationDialogTypes.THREE_BUTTONS_AND_SEEKBAR
        };

        WidgetSpinner widgetSpinner = findViewById(R.id.widgetSpinner);

        widgetSpinner.setOnSelectedItemChangedListener(selectedItem -> {

            if (selectedItem != null) {
                new NotificationDialog.Builder(ControlsJavaPreview.this)
                        .setNotificationType(notificationDialogResourceIds[selectedItem])
                        .setNotificationIcon(NotificationDialog.NotificationDialogIcons.SUCCESS)
                        .setDescriptionText("Some Description Text")
                        .setPositiveButton("OK")
                        .setCheckBoxText("Some text in the check box")
                        .setOnDismissListener(dialog -> Toast.makeText(ControlsJavaPreview.this, "Notification dialog dismissed", Toast.LENGTH_SHORT).show())
                        .build()
                        .show(getSupportFragmentManager(), "fragment_edit_name");
            }
        });

        WidgetTitleAndSeekBar widgetTitleAndSeekBar = findViewById(R.id.widgetTitleAndSeekBar);

        widgetTitleAndSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        widgetTitleAndSeekBar.setOnValueUpdatedListener(new CompoundWidgetInterfaces.OnProgressValueUpdatedListener() {
            @Override
            public String onProgressValueUpdated(int value) {
                return ">>" + value;
            }
        });

        widgetTitleAndSeekBar.setProgress(50);

        WidgetTitleAndSeekBarEditText widgetTitleAndSeekBarEditText = findViewById(R.id.widgetTitleAndSeekBarEditText);

        widgetTitleAndSeekBarEditText.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        widgetTitleAndSeekBarEditText.setOnValueUpdatedListener(new CompoundWidgetInterfaces.OnValueUpdatedListener() {
            @Override
            public String onProgressValueUpdated(int value) {
                return ">>" + value;
            }

            @Override
            public int onUserInputChanged(String value) {
                try {
                    return Integer.parseInt(value.substring(2));
                } catch (NumberFormatException ignore) {

                    // returning MIN_VALUE will result in no change to progress
                    return Integer.MIN_VALUE;
                }
            }
        });

        widgetTitleAndSeekBarEditText.setProgress(50);
    }
}
