package com.example.tomek.androidtetrisjava.database;


import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * this class provides info needed by
 * ContentProvider object
 */
public class DatabaseDescription {


        public static final String AUTHORITY="com.example.tomek.androidtetrisjava.database";


        private static final Uri BASE_CONTENT_URI= Uri.parse("content://"+AUTHORITY);

        /**
         * nested class describing the content of database
         *
         * BaseColumns provides names for the very commin _ID and _COUNT columns
         */
        public static final class Player implements BaseColumns {



            public static final String TABLE_NAME_ = "Scores";


            public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME_).build();



            public static final String COLUMN_PLAYER_NAME  = "player_name";
            public static final String COLUMN_SCORE = "score";



            public static Uri createUri(long id) {

                return ContentUris.withAppendedId(CONTENT_URI, id);
            }
        }
}
