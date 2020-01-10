package com.kenargo.compoundwidgetsampleapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.ashraf007.expandableselectionview.ExpandableSelectionViewInterfaces
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

//    companion object {
        val savedDarkModeStateID = "SavedDarkModeState"
        var currentDarkModeTheme = 0
//    }

    override fun onCreate(savedInstanceState: Bundle?) {

        savedInstanceState?.let {
            currentDarkModeTheme = it.getInt(savedDarkModeStateID)
        }

        // https://blog.iamsuleiman.com/daynight-theme-android-tutorial-example/
        //
        // MODE_NIGHT_AUTO – The app switches to night theme based on the time. It makes use of location APIs
        //  (if necessary permissions granted) for accuracy. Otherwise falls back to using system time.
        // MODE_NIGHT_FOLLOW_SYSTEM – (DO NOT USE) This is more of an app-wide setting for night mode. Uses the system
        //  Night Mode setting to determine if it is night or not.
        // MODE_NIGHT_NO – Tells the app to never use night mode, regardless of time / location.
        // MODE_NIGHT_YES –  Tells app to use night mode always, regardless of time / location.

        val newDarkModeTheme: Int

        when (currentDarkModeTheme) {
            0 -> {
                newDarkModeTheme = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            }
            1 -> {
                newDarkModeTheme = AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
            }
            2 -> {
                newDarkModeTheme = AppCompatDelegate.MODE_NIGHT_YES
            }
            3 -> {
                newDarkModeTheme = AppCompatDelegate.MODE_NIGHT_NO
            }
            else -> {
                newDarkModeTheme = AppCompatDelegate.MODE_NIGHT_NO
            }
        }

        AppCompatDelegate.setDefaultNightMode(newDarkModeTheme)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonCommonJavaControls.setOnClickListener { startActivity(Intent(this, ControlsJavaPreview::class.java)) }

        buttonCommonKotlinControls.setOnClickListener { startActivity(Intent(this, ControlsKotlinPreview::class.java)) }

        singleSelectionView.selectIndex(currentDarkModeTheme)

        singleSelectionView.setOnSelectionChange(ExpandableSelectionViewInterfaces.SelectedItemChanged {
            Toast.makeText(this, "SelectedIndex is $it", Toast.LENGTH_SHORT).show()

            currentDarkModeTheme = it?.also {
                if (currentDarkModeTheme != it) {
                    recreate()
                }
            }!!
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(savedDarkModeStateID, currentDarkModeTheme)
    }
}
