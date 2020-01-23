package com.kenargo.compound_widgets;

import android.view.View;

public class CompoundWidgetInterfaces {

    ////////////////////////////////////////////////////////////////////
    // TODO: Is there really no way to have one callback to satisfy both??
    //  OK, there is but not until Kotlin 1.4; then I can use 'fun interface'
    //  see https://stackoverflow.com/questions/59525086/java-kotlin-callback-syntax-do-i-really-need-2-callback-definitions-after-conv/59527416#59527416
    //  MUST KEEP INTERFACES IN JAVA UNTIL KOTLIN 1.4
    ////////////////////////////////////////////////////////////////////

    public interface OnSelectedItemChanged {
        void onSelectionChange(Integer selectedItem);
    }

    public interface OnCheckedChangeListener {
        void onCheckedChanged(View view, boolean isChecked);
    }

    public interface OnProgressValueUpdatedListener {
        String onProgressValueUpdated(int value);
    }

    public interface OnValueUpdatedListener {
        String onProgressValueUpdated(int value);
        int onUserInputChanged(String value);
    }

    /**
     * Interface used to allow the creator of a dialog to run some code when the
     * dialog is dismissed.
     */
    public interface OnDismissListener {
        /**
         * This method will be invoked when the dialog is dismissed.
         *
         * @param dialog the dialog that was dismissed will be passed into the
         *               method
         */
        void onDismiss(NotificationDialog dialog);
    }

    public interface OnClickListener {
        /**
         * This method will be invoked when a button in the dialog is clicked.
         *
         * @param dialog the dialog that received the click
         * @param which the button that was clicked
         */
        void onClick(NotificationDialog dialog, int which);
    }
}
