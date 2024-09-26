# Changelog

## 2.0.3

- Adapter can now also import videos from Bynder
  - when importing a video, the video data is copied to CoreMedia and a `CMVideo` content is created
- Compatible CoreMedia Content Cloud version: `2401.3`

## 2.0.2

- Use Java 17 SDK
- Use Node 18.x
- Compatible CoreMedia Content Cloud version: `2401.3`

## 2.0.1

- Introduced subfolders `Assets`, `Collections` and `Tags`
  - `Assets`: allows to browse and search across all assets
  - `Collections`: shows all public collections and the containing assets
  - `Tags`: shows all tags and the assets that are tagged with the tag
- Refactored search functionality
- Compatible CoreMedia Content Cloud version: `2110.2`

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
