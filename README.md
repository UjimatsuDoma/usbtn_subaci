
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
      - Revision 1: Introduce splash screen and optimize UI state save & load [Feb. 27?, 2025 ~ Apr. 5, 2025]  
        NOTE: merge `develop-mvvm` branch
      - Revision 2: Migrate to multi-module development [May 7, 2025 ~ TBA]<br>
        Revision 2: ~Migrate from Compose `FontFamily` to manual `Typeface` injection for `MODERN` build, to make use of [Noto CJK Variable OTC](https://github.com/notofonts/noto-cjk) and eventually reducing APK file size. [Jul. 26 ~ TBA]~ leave it for Milestone 5
      - Revision 3: TBA
    - Milestone 4: Introduce animation [TBA]
- 1st Edition "Master Up": TBA
- 2nd Edition "SuiCYAN": TBA

# Features

- **Voices**: click to play, long press to add to playlist  
- **Sources**: list of YouTube live streams where the voices are clipped from  
- **Playlist**: create/rename/delete playlists, move single item up and down, click to play, supports loop playing  
- **Daily Random Voice**: self-check and update if needed, at launch time.  

# Notes

For typeface consistency, Noto Sans & Serif CJK font is globally used and embedded in this app. That's why the released APKs are large.

Due to request from author of original website, feature limitations are below: 

1. This app works completely offline. Voice data will **NOT** be fetched from server.
2. For the same reason, Limited Voices(available on special days) will **NOT** be accessible from this app.

## About "New" Voices

Milestone 1 to Milestone 3 Revision 1: "Added on 2023/12/08 and later"  
Milestone 3 Revision 2 and later: "Added on 2025/5/12 and later"

# Technologies

Jetpack Compose, Jetpack Navigation, Hilt, Kotlin Serialization, Kotlin DateTime API, Preferences DataStore, Room, etc.
