# Compound Widgets Components Library

[![Crates.io](https://img.shields.io/crates/l/License)](https://www.apache.org/licenses/LICENSE-2.0.html)
[![MinSdk: 17](https://img.shields.io/badge/minSdk-17-green.svg)](https://developer.android.com/about/versions/android-4.0)
[![write: Kotlin](https://img.shields.io/badge/write-Kotlin-orange.svg)](https://kotlinlang.org/)
[![](https://jitpack.io/v/kenargo/compound_widgets.svg)](https://jitpack.io/#kenargo/compound_widgets)

This is a collection of UI compound components that you can use with applications.

## Installation
*Step 1.* Add the JitPack repository to your project `build.gradle` file
```gradle
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
*Step 2.* Add the dependency in the form
```gradle
	dependencies {
	        implementation 'com.github.kenargo:compound_widgets:${latest_version}'
	}

```

## Sample App Included shows Java and Kotlin use

- Full support of Android Dark Mode

### See example images below

#### NotificationDialog

A collection of dialog displaying an icon, title, message and expandable section for "more information"

- ONE_BUTTON
- ONE_BUTTON_NO_TITLE
- ONE_BUTTON_AND_PROGRESS
- ONE_BUTTONS_AND_EDIT_TEXT
- ONE_BUTTONS_AND_SEEKBAR
- TWO_BUTTONS
- TWO_BUTTONS_NO_TITLE
- TWO_BUTTONS_AND_PROGRESS
- TWO_BUTTONS_AND_EDIT_TEXT
- TWO_BUTTONS_AND_SEEKBAR
- THREE_BUTTONS
- THREE_BUTTONS_NO_TITLE
- THREE_BUTTONS_AND_PROGRESS
- THREE_BUTTONS_AND_EDIT_TEXT
- THREE_BUTTONS_AND_SEEKBAR

#### WidgetBackTitleForwardDelete

#### WidgetMinMaxSeekBar

#### WidgetPlayPauseControl

#### widgetSpin

#### WidgetTitleAndSeekBar

#### WidgetTitleAndSeekBarEditText

#### WidgetTitleAndSpinner

#### WidgetTitleAndSwitch

#### WidgetTitleAndSwitchSeekBar


![](https://github.com/kenargo/compound_widgets/blob/master/readmeImages/AllWidgets_Page_1.png)

![](https://github.com/kenargo/compound_widgets/blob/master/readmeImages/AllWidgets_Page_2.png)

## Credits:

## Credit for included source code:

- https://github.com/hakobast/DropdownTextView
    - Source in 'derivedWorks' because git version has a minSdkLevel=19, this library supports minSdkLevel=17
    - Used in NotificationDialog
    
-https://github.com/zcweng/SwitchButton
    - Source in 'derivedWorks' because git version doesn't display in designer view
    - Used in WidgetTitleAndSwitch and WidgetTitleAndSwitchSeekBar