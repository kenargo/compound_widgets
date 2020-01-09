package com.kenargo.compoundwidgetsampleapp;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ashraf007.expandableselectionview.ExpandableSingleSelectionView;
import com.kenargo.compound_widgets.NotificationDialog;

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

        ExpandableSingleSelectionView widgetTitleAndSpinner = findViewById(R.id.expandableSingleSelectionView);

        widgetTitleAndSpinner.setOnSelectionChange(selectedItem -> {

            if (selectedItem != null) {
                new NotificationDialog.Builder(ControlsJavaPreview.this)
                        .setNotificationType(notificationDialogResourceIds[selectedItem])
                        .setNotificationIcon(NotificationDialog.NotificationDialogIcons.SUCCESS)
                        .setPositiveButton("OK")
                        .setOnDismissListener(dialog -> Toast.makeText(ControlsJavaPreview.this, "Notification dialog dismissed", Toast.LENGTH_SHORT).show())
                        .build()
                        .show(getSupportFragmentManager(), "fragment_edit_name");
            }
        });

    }
}
