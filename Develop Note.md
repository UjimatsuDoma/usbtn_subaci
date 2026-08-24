# Develop Notes

## String Resource

resource for every feature separates into three parts:

1. Strings used in UI layer and nothing else - plain UI components, descriptions
2. Strings used in UI layer and stored in objects - preference items
3. Strings used in one-time messages - error/info messages

## Software Layered Architecture

- Model layer
  - Data source(database, res, assets, file system)
  - Repository(transform raw data to business model)
- Domain layer(optional, reusable data handling)
- UI layer
  - ViewModel(map business data to UI state)
  - Screens and Composables