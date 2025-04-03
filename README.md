
# SUBACI ういボタンC 羽音按钮

Shigure Ui Button App Compose Implementation  
inspired by [しぐれういボタン](https://leiros.cloudfree.jp/usbtn/usbtn.html)(credit: [LeirosSkyly](https://x.com/LSkyly))

# Development stage

- Milestone: All functionalities of the original website, plus improved app architecture.  [Nov. 24, 2024 - TBA]
    - Milestone 0: things to comply with "copyright requirements".  [Nov. 24, 2024 - Nov. 27, 2024]
    - Milestone 1: Basic implementation of features.  [Nov. 28, 2024 - Dec. 3, 2024]
      (see `develop` branch)
    - Milestone 2: Complete rewrite of Milestone 1, migrate to MVVM [Dec. 3, 2024 - Dec. 22, 2024]
      - Revision 1: Bug fixes [Dec. 23, 2024 ~ Dec. 27, 2024]
      - Revision 2: Introduce landscape layout and optimize navigation logic [Dec. 27, 2024 ~ Dec. 28, 2024]
      - Revision 3: Complete implementation of Playlist screen core features [Dec. 28, 2024 ~ Dec. 30, 2024]
    - Milestone 3: Re-arrange pages - merge All Voices screen and Categories screen, and implement Settings screen [Jan. 1, 2025 ~ Feb. 26, 2025]
      - Revision 1: Introduce splash screen and optimize UI state save & load [Feb. 27?, 2025 ~ Apr. 4, 2025]
      - Revision 2: migrate to multi-module development [TBA]
    - Milestone 4: Introduce animation [TBA]
- 1st Edition "Master Up": TBA
- 2nd Edition "SuiCYAN": TBA

# Features
- **Voices**: click to play, long press to add to playlist  
- **Sources**: list of YouTube live streams where the voices are clipped from  
- **Playlist**: create/rename/delete playlists, move single item up and down, click to play, supports loop playing  
- **Daily Random Voice**: self-check and update if needed, at launch time.  

# Notes

Limited Voice will **NOT** be implemented, due to request from author of original website.

# Technologies

Jetpack Compose, Jetpack Navigation, Hilt, Kotlin Serialization, Kotlin DateTime API, Preferences DataStore, Room, etc.
