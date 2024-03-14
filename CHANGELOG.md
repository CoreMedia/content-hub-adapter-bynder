Changelog
================================================================================

## 2.0.0

- Refactored adapter to CoreMedia Application Plugin
- Added thumbnail support
- Updated documentation
- Added handling of all Bynder base types (no import other than for images, though)
- Removed image and video search folders (they collided with type search)
- Removed unneeded code that distinguished between image and video searches
  Added wildcard search, if no query term is given
  Added type "all" to search
- Fixed preview images (especially for videos)
- Add column in bynder library view
- Compatible CoreMedia Content Cloud version: `2110.2`

## 1.0.0

### Initial Release 🥳

- Basic implementation of the Content Hub Adapter for Bynder.
- Compatible CoreMedia Content Cloud version: `2101.1`
