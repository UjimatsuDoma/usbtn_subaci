package prac.tanken.shigure.ui.subaci.data.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class PlaylistDatabaseHelper(
    context: Context,
    name: String,
    version: Int
): SQLiteOpenHelper(context, name, null, version) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            create table Playlists (
                id integer primary key autoincrement,
                name text not null,
                voices_string text not null
            )
        """.trimIndent())
    }

    override fun onUpgrade(
        db: SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int
    ) {
        //
    }
}