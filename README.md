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

### Sample App Included shows Java and Kotlin use

- Full support of Android Dark Mode

## Credits:

- https://github.com/AliAzaz/Edittext-Library
    - Used in WidgetTitleAndSeekBarEditText

- https://github.com/zcweng/SwitchButton
    - Used in WidgetTitleAndSwitch
    - Used in WidgetTitleAndSwitchSeekBar

- https://github.com/kenargo/ExpandableSelectionView
    - Derived from https://github.com/ashrafDoubleO7/ExpandableSelectionView
    - Fixed support for Java language callbacks
    - Adding support for all Android DarkModes
    - Update to attr to support standard 'android:background' rather than custom 'app:background'
    - Updated to latest gradle and latest Kotlin versions
    - Added attribute 'android:entries' and 'android:hint' allowing list and hint text to be added using XML

## Credit for included source code:

- https://github.com/hakobast/DropdownTextView
    - source in 'derivedWorks' because git version has a minSdkLevel=19, this library supports minSdkLevel=17