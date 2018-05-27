package com.example.tomek.androidtetrisjava.Activities.TopScoresActivity;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.tomek.androidtetrisjava.R;
import com.example.tomek.androidtetrisjava.database.TopScore;
import com.example.tomek.androidtetrisjava.database.DatabaseDescription;

import java.util.ArrayList;
import java.util.List;


/**
 * This activity shows all scores from previous games
 */
public class TopScoresActivity extends AppCompatActivity {

    private List<TopScore> scoresList;
    private ContentResolver contentResolver;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scores_list);

        scoresList=new ArrayList<>();
        contentResolver=getContentResolver();
        fetchDataFromDb();

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        //setting the adapter
        recyclerView.setAdapter(new ScoresAdapter(scoresList));


        //setting the layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);


        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

    }


    // fetches data from database
    private void fetchDataFromDb(){

        Uri uri= DatabaseDescription.Player.CONTENT_URI;

        //query for ContentResolver
        Cursor cursor=contentResolver.query(uri,null,null,null,null,null);
        //todo: NullPointer!
        while(cursor.moveToNext()){
            populateList(cursor);
        }
    }

    // reads next data from cursor
    private void populateList(Cursor cursor){
        String playerName=cursor.getString(cursor.getColumnIndex(DatabaseDescription.Player.COLUMN_PLAYER_NAME));
        String score=cursor.getString(cursor.getColumnIndex(DatabaseDescription.Player.COLUMN_SCORE));
        //Integer id=cursor.getInt(cursor.getColumnIndex(DatabaseDescription.Player._ID));

        scoresList.add(new TopScore(score,playerName));
    }



}
