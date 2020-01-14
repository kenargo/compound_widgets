# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).


## [1.2.3] - 2020-1-7
### Added
- Added 'immediate' to the setProgress method allowing progress changes to bypass animation

## [1.2.2] - 2020-1-7
### Changed
- Renamed setValueText -> setUnitsText

## [1.2.1] - 2020-1-7
### Changed
- Renamed setUnitsTitle -> setValueText (less confusion)

## [1.2.0] - 2020-1-7
### Added
- OnValueUpdatedListener to WidgetTitleAndSeekBar, WidgetTitleAndSwitchSeekBar and WidgetTitleAndSeekBarEditText to support value conversions
### Changed
- Removed unneeded imports from library

## [1.1.0] - 2020-1-7
### Added
- WidgetSpinner
### Changed
- New Spinner implementation; I didn't like the 3rd party implementation as it takes up too much UI space.

## [1.0.0] - 2020-1-7
### Added
- Initial public release
