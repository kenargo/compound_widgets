package com.kenargo.compound_widgets;

import android.view.View;

public class CompoundWidgetInterfaces {

    ////////////////////////////////////////////////////////////////////
    // TODO: Is there really no way to have one callback to satisfy both??
    //  OK, there is but not until Kotlin 1.4; then I can use 'fun interface'
    //  see https://stackoverflow.com/questions/59525086/java-kotlin-callback-syntax-do-i-really-need-2-callback-definitions-after-conv/59527416#59527416
    //  MUST KEEP INTERFACES IN JAVA UNTIL KOTLIN 1.4
    ////////////////////////////////////////////////////////////////////

    public interface SelectedItemChanged {
        void onSelectionChange(Integer selectedItem);
    }

    public interface OnCheckedChangeListener{
        void onCheckedChanged(View view, boolean isChecked);
    }
}
