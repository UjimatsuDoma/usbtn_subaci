
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
        Revision 2: Migrate from Compose `FontFamily` to manual `Typeface` injection for `MODERN` build, to make use of [Noto CJK Variable OTC](https://github.com/notofonts/noto-cjk) and eventually reducing APK file size. [Jul. 26, 2025 ~ Oct. 31, 2025]. Yet this causes app to break at random time, to be fixed in Milestone 3.
      - Revision 3: Optimize font management by utilizing 7-Zip ~and `fonttools` libraries~ Leave that for a separate repo [Nov. 4, 2025 ~ May 31. 2026]  
        [Note: see *Notes - about support for below Android 8* section]
      - Revision 4: Migrate Toasts to Snackbars [June 5, 2026 ~ TBA]
    - Milestone 4: Introduce animation and optimize UX [2026? ~ TBA]
- 1st Edition "Master Up": TBA
- 2nd Edition "SuiCYAN": TBA

# Features

- **Voices**: click to play, long press to add to playlist  
- **Sources**: list of YouTube live streams where the voices are clipped from  
- **Playlist**: create/rename/delete playlists, move single item up and down, click to play, supports loop playing  
- **Daily Random Voice**: self-check and update if needed, at launch time.  

# Notes

## Font

For typeface consistency, Noto Sans & Serif CJK font is globally used and embedded in this app. That's why the released APKs are large.

## About support for below Android 8

support for PRE_OREO Android versions are DEPRECATED on this version(maybe there'll be a separate edition of this app for this purpose), for Compose is now progressively deprecating these old Android versions(for instance, on 20260605, things in AndroidX stops working on Android 5.X), and mechanisms for font loading, etc. are so different that maintaining these two in a single repo has become a burden(elaborated below). I believe that those who would install this niche app on such archaic - I couldn't believe I would say something like this back in my junior high days - smartphones would very probably just accept what exists right there.

What makes PRE_OREO nasty the most for me is FONT: I need Noto CJK to be the app's global UI typeface, but since PRE_OREO Android have absolutely no support for variable fonts and in-app TTC, I have to figure out a way to turn variable fonts to static fonts, and I somehow got the versatile font processing library `fontTools` working on Android, but the performance is soooooooooo bad that it would just crash on a 2011 smartphone powered by an Android 7 custom ROM. Looking back, I'm still astonished that the development of this project essentially halted for a year or so because of THIS!

I assume I would build this separate app in Flutter because it said TTC support in its documentation - who knows? :)

## Things that would NOT be included

Due to request from author of original website, feature limitations are below: 

1. This app works completely offline. Voice data will **NOT** be fetched from server.
2. For the same reason, Limited Voices(available on special days) will **NOT** be accessible from this app.

## About "New" Voices

Milestone 1 to Milestone 3 Revision 1: "Added on 2023/12/08 and later"  
Milestone 3 Revision 2 and later: "Added on 2025/5/12 and later"

# Technologies

Jetpack Compose, Jetpack Navigation, Hilt, Kotlin Serialization, Kotlin DateTime API, Preferences DataStore, Room, etc.
