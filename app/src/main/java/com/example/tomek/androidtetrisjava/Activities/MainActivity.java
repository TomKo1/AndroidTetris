package com.example.tomek.androidtetrisjava.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tomek.androidtetrisjava.Activities.TopScoresActivity.TopScoresActivity;
import com.example.tomek.androidtetrisjava.R;

public class MainActivity extends AppCompatActivity {

    private String playerName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String s = getIntent().getStringExtra("");

        configureNewGameBtn();
    }



    private void configureNewGameBtn(){
        Button newGameBtn = (Button)findViewById(R.id.new_game_btn);
        newGameBtn.setOnClickListener(
                (View v) -> {
                    Intent intent = new Intent(MainActivity.this,GameActivity.class);
                    showDialogForName(intent);
                }
        );

        Button settings = (Button)findViewById(R.id.game_settings_btn);
        settings.setOnClickListener(
                (View v) -> {
                    showAlerForChoosingDifficulty();
                }
        );

        findViewById(R.id.quit_game_btn).setOnClickListener(
                (view)-> {
                   //todo: doesn't work -> bad practice
                    System.exit(0);
                }

        );

        Button highestScores = (Button)findViewById(R.id.high_score_btn);
        highestScores.setOnClickListener(
                (view)->{
                    startActivity(new Intent(MainActivity.this,TopScoresActivity.class));
                }
        );

    }


    private void showAlerForChoosingDifficulty(){
        CharSequence colors[] = new CharSequence[] {"Easy", "Medium", "Hard"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick level");
        builder.setItems(colors,
                (DialogInterface dialogInterface, int i) -> {
                    switch(i){
                        case 0:
                            saveToSharedPreferences(1);
                            break;
                        case 1:
                            saveToSharedPreferences(2);
                            break;
                        case 2:
                            saveToSharedPreferences(3);
                            break;
                        default:
                            Toast.makeText(MainActivity.this,"No such option!",Toast.LENGTH_SHORT).show();
                    }
                }
        );
        builder.show();
    }


    private void saveToSharedPreferences(int level){
        Context context = getApplicationContext();

        SharedPreferences
                sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(R.string.level), level);
        editor.apply(); // handle in the background
    }



    private void showDialogForName(Intent intent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Please insert your name:");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", (dialog, which) -> {
            MainActivity.this.playerName =  input.getText().toString();
            intent.putExtra("PLAYER_NAME", playerName);
            startActivity(intent);
        });
        builder.setNegativeButton("Cancel",
                (dialog, which) ->

                {
                    dialog.cancel();
                }

        );

        builder.show();



    }




}
