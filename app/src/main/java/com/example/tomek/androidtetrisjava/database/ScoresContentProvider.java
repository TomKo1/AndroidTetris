package com.example.tomek.androidtetrisjava.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.tomek.androidtetrisjava.R;

public class ScoresContentProvider extends ContentProvider {


    // in order to get access to the db
    private TopScoresDatabaseHelper dbHelper;
    private String errorMessage;

    //to recognize requests
    private static final UriMatcher uriMatcher=new UriMatcher(UriMatcher.NO_MATCH);

    // consts used to recognize the operation
    public static final int ONE_PLAYER =1;
    public static final int WHOLE_TABLE=2;


    static{
        uriMatcher.addURI(DatabaseDescription.AUTHORITY,DatabaseDescription.Player.TABLE_NAME_+"/#", ONE_PLAYER);
        uriMatcher.addURI(DatabaseDescription.AUTHORITY,DatabaseDescription.Player.TABLE_NAME_,WHOLE_TABLE);
    }


    @Override
    public boolean onCreate() {
        dbHelper=new TopScoresDatabaseHelper(getContext());
        errorMessage=getContext().getString(R.string.unsupportedQuery);
        return true;
    }





    /*
     *
     * methods below are used for database managment
     *
     */
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        SQLiteQueryBuilder queryBuilder=new SQLiteQueryBuilder();

        queryBuilder.setTables(DatabaseDescription.Player.TABLE_NAME_);


        switch(uriMatcher.match(uri)){
            case ONE_PLAYER:

                queryBuilder.appendWhere(DatabaseDescription.Player.TABLE_NAME_+"="+uri.getLastPathSegment());

                break;
            case WHOLE_TABLE:
                // we can take here additional steps for query related to whole table
                // in this case we doesn't append any WHERE in the SQL
                break;
            default:
                unsupportedOperation();
        }


        Cursor cursor=queryBuilder.query(dbHelper.getReadableDatabase(),projection,selection,selectionArgs,null,null,sortOrder);


        cursor.setNotificationUri(getContext().getContentResolver(),uri);

        return cursor;
    }


    private void unsupportedOperation(){

        Toast.makeText(getContext(),errorMessage, Toast.LENGTH_SHORT).show();

        throw new UnsupportedOperationException(errorMessage);

    }



    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        Uri newPlayerUri=null;


        switch(uriMatcher.match(uri)){
            case WHOLE_TABLE:

                // insert
                long insRowId=dbHelper.getWritableDatabase().insert(DatabaseDescription.Player.TABLE_NAME_,null,contentValues);


                if(insRowId>0){
                    newPlayerUri=DatabaseDescription.Player.createUri(insRowId);


                    // notify listeners that database was modified
                    getContext().getContentResolver().notifyChange(uri,null);
                }else{
                    throw new SQLException(getContext().getString(R.string.insert_failure));
                }

                break;
            default:
                unsupportedOperation();
        }


        return  newPlayerUri;


    }

    // this method is not required so far but need to be implemented as it is abstract
    @Override
    public int delete(@NonNull Uri uri, @Nullable String where, @Nullable String[] whereArgs) {
        return 0;
    }


    //this method is not required so far but need to be implemented as it is abstract
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {

        return 0;
    }

    //not required in this app
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }


}
