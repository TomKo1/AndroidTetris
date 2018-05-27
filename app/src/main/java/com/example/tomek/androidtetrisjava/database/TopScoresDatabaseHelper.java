package com.example.tomek.androidtetrisjava.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;




/**
 * Manages database creation and version management
 */
public class TopScoresDatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Tetris.db";

    public TopScoresDatabaseHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }


    // executes appropriate method to create the table
    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE_SQL_="CREATE TABLE "+ DatabaseDescription.Player.TABLE_NAME_+" ("+
                DatabaseDescription.Player._ID+" integer primary key autoincrement, "+
                DatabaseDescription.Player.COLUMN_PLAYER_NAME+" TEXT, "+
                DatabaseDescription.Player.COLUMN_SCORE+" TEXT );";

        db.execSQL(CREATE_TABLE_SQL_);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //do nothing...
    }
}
