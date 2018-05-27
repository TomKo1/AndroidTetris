package com.example.tomek.androidtetrisjava.threads;



import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.os.AsyncTask;
import com.example.tomek.androidtetrisjava.database.DatabaseDescription;
import com.example.tomek.androidtetrisjava.database.TopScore;


/**
 * Performs background(background thread) task saving to DB
 */
public class ScoreSaveThread extends AsyncTask<TopScore,Void,Boolean> {

    private final ContentResolver contentResolver;

    public ScoreSaveThread(ContentResolver contentResolver){
        this.contentResolver = contentResolver;

    }

    @Override
    protected Boolean doInBackground(TopScore[] objects) {
        for(TopScore score: objects){
            saveScoreToDb(score.getPlayerName(),score.getScore());
        }
        return true;
    }




    private boolean saveScoreToDb(String playerName, String score){
        // we create URI referrimng to whole table
        Uri uri= DatabaseDescription.Player.CONTENT_URI;



        ContentValues values=new ContentValues();


        values.put(DatabaseDescription.Player.COLUMN_PLAYER_NAME,playerName);
        values.put(DatabaseDescription.Player.COLUMN_SCORE,score);

        contentResolver.insert(uri,values);

        return true;
    }

}
