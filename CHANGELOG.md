# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.5.3]
### Changed
- Updated Android Studio (3.6.1), Gradle, etc.
### Changed
- Changed background colors on play/pause widget to make it more visible in map and darker surfaces

## [1.5.2]
### Changed
- Updated Android Studio (3.6), Gradle, etc.

## [1.5.1]
### Added
- New WidgetTitleAndEditText
- Added subtitle to all controls containing titles

## [1.4.1]
### Added
- Added 'NONE' for header icon to cover cases where no icon is required

## [1.4.0]
### Changed
- Changed to library's callbacks for NotificationDialog so values can be read by caller
### Added
- CheckBox added to some NotificationDialogs and exposed access in builder for same
- Exposed properties of MinMaxSeekBar and added ability to read user inputs from NotificationDialogs
- SeekBar EditText UI variant for NotificationDialog
- Ability to set width is inner spinner widget: widgetTitleAndSpinnerWidth
### Fixed
- Fixed drop-down to correctly size width
- Play, Pause widget showing incorrect state images

## [1.3.1]
### Changed
- Default for animate progress bar changed to false (was true)

## [1.3.0]
### Changed
- Increased code efficiency
###Fixed
- Corrected issues when some controls are displayed in designer

## [1.2.7]
## Fixed
- Corrected issue where using +/- in WidgetTitleAndSeekBarEditText would not update EditText view

## [1.2.5]
## Added
- Added attribute to control how many items appeared in WidgetSpinner

## [1.2.4]
### Changed
- Changed default to setProgress::immediate to true (no animation)

## [1.2.3]
### Added
- Added 'immediate' to the setProgress method allowing progress changes to bypass animation

## [1.2.2]
### Changed
- Renamed setValueText -> setUnitsText

## [1.2.1]
### Changed
- Renamed setUnitsTitle -> setValueText (less confusion)

## [1.2.0]
### Added
- OnValueUpdatedListener to WidgetTitleAndSeekBar, WidgetTitleAndSwitchSeekBar and WidgetTitleAndSeekBarEditText to support value conversions
### Changed
- Removed unneeded imports from library

## [1.1.0]
### Added
- WidgetSpinner
### Changed
- New Spinner implementation; I didn't like the 3rd party implementation as it takes up too much UI space.

## [1.0.0]
### Added
- Initial public release
