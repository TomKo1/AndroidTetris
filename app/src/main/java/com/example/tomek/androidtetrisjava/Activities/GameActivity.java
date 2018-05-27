package com.example.tomek.androidtetrisjava.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tomek.androidtetrisjava.GameLogic.GameValues;
import com.example.tomek.androidtetrisjava.GameLogic.GameView;
import com.example.tomek.androidtetrisjava.R;
import com.example.tomek.androidtetrisjava.database.TopScore;
import com.example.tomek.androidtetrisjava.threads.ScoreSaveThread;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity {


    private GameView gameView;
    private TextView textView_score;


    // The state of the current game
    private boolean isStarted = false;
    private boolean isGoing = false;

    private String playerName = "Default Player";

    private int currentScore = 0;

    // for scheduling background task to execute later
    private Timer timer;

    private int miliSecondsLevel;

    private Button playAgainBtn;
    // it of the next block to show
    private int nextBlockType;

    /**
     * Handler interacts with UIThread (because it is created here -> in UIThread) when the message
     * is received he do some 'UIThread' work.
     */
    private Handler refreshHandler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case GameValues.NORMAL_DOWN:

                    // check if the current block can go down and if yes move it down
                    if (GameActivity.this.gameView.canDown()) {
                        GameActivity.this.gameView.normalDown();
                    } else {
                        //current block can't move down - check if there are any rows to be
                        //removed & remove them and whether the game is over

                        int count = GameActivity.this.gameView.checkForRowsElimination();
                        GameActivity.this.addPoints(count * 10);

                        GameActivity.this.textView_score
                                .setText(Integer.toString(GameActivity.this.getCurrentScore()));

                        // For the Timer
                        GameActivity.this.timer.cancel();
                        GameActivity.this.timer.purge();

                        // check if the game is over or if we can continue
                        if (GameActivity.this.gameView
                                .isGameOver(GameActivity.this.nextBlockType)) {

                            // run thread saving to Db
                            String score = textView_score.getText().toString();
                           // if (!score.equals("0")) {
                                TopScore topScore = new TopScore(score, playerName);
                                ScoreSaveThread asyncSaver = new ScoreSaveThread(GameActivity.this.getContentResolver());
                                asyncSaver.execute(topScore);
                            //}
                            GameActivity.this.reset();
                        } else {
                            // next block
                            GameActivity.this.pushNextBlock();

                            GameActivity.this.setNewTimer(miliSecondsLevel);

                        }
                    }

                    break;

                case GameValues.DROP_DOWN:
                    if (GameActivity.this.gameView.canDown()) {
                        // perform normal down movement
                        GameActivity.this.gameView.normalDown();
                    } else {
                        // we can't move down - check the rows
                        GameActivity.this.gameView.checkForRowsElimination();
                    }

                    break;

                case GameValues.LEFT:
                    // perform move to the left if possible
                    if (GameActivity.this.gameView.canLeft()) {

                        GameActivity.this.gameView.moveLeft();
                    }

                    break;

                case GameValues.RIGHT:
                    // perform move to the right if possible
                    if (GameActivity.this.gameView.canRight()) {
                        GameActivity.this.gameView.moveRight();
                    }

                    break;

                case GameValues.ROTATE:
                    // rotate if possible
                    if (GameActivity.this.gameView.canRotate()) {
                        GameActivity.this.gameView.rotate();
                    }

                    break;

                case GameValues.START:
                    // Change the state of the game
                    GameActivity.this.isStarted = true;
                    GameActivity.this.isGoing = true;

                    // generate the block to show
                    GameActivity.this.generateNextBlock();

                    // push new block and generate new
                    GameActivity.this.pushNextBlock();

                    // Set the timer to perform normal down of the block in the MainView
                    //todo: difficulty of the game
                    GameActivity.this.setNewTimer(miliSecondsLevel);


                    break;

                case GameValues.RESUME:


                    // Change the state of the game
                    GameActivity.this.isStarted = true;
                    GameActivity.this.isGoing = true;

                    // Set the timer to perform normal down of the block in the MainView
                    //todo: difficulty
                    GameActivity.this.setNewTimer(miliSecondsLevel);


                    break;

                case GameValues.RESET:
                    // Reset the state of the game
                    GameActivity.this.isStarted = false;
                    GameActivity.this.isGoing = false;


                    // For the timer
                    GameActivity.this.timer.cancel();
                    GameActivity.this.timer.purge();

                    // Show the game over animation in the MainView
                    GameActivity.this.clearAllCellsAndShowToast("Now you can play again!");
                    GameActivity.this.setPlayAgainBtnEnable();

                    // Reset the current score of the game
                    GameActivity.this.setCurrentScore(0);
                    GameActivity.this.textView_score.setText("0");

                    break;

                case GameValues.SET_ROW_ALL_FILLED:
                    GameActivity.this.gameView.setRowAllFilledAt(message.getData().getInt("row"));

                    break;

                case GameValues.RESET_MAIN_VIEW:
                    GameActivity.this.gameView.reset();

                    break;

                default:
                    break;
            }
        }
    };

    private void makeAppropriateLevel() {
        Context context = getApplicationContext();

        SharedPreferences
                sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        int level = sharedPref.getInt(getString(R.string.level), 1);

        switch (level) {
            case 1:
                miliSecondsLevel = 1000;
                break;
            case 2:
                miliSecondsLevel = 500;
                break;
            case 3:
                miliSecondsLevel = 250;
                break;
            default:
                //for safety
                miliSecondsLevel = 1000;
        }
    }


    //todo: implement behaviour when the view is no longer visaible and so on..


    /**
     * Just overwritten onCreateMode - initializes views.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //get game view reference
        this.gameView = (GameView) findViewById(R.id.game_view);

        this.playerName = getIntent().getStringExtra("PLAYER_NAME");

        configButtons();


        // configurate game buttons and their onClickListeners
        configGameButtons();

        makeAppropriateLevel();

    }

    //todo: implement
    @Override
    protected void onResume() {
        super.onResume();
        refreshHandler.sendEmptyMessage(GameValues.START);
    }

    //todo: implement
    @Override
    protected void onPause() {
        super.onPause();
        //this.refreshHandler.sendEmptyMessage(GameValues.RE)
    }

    private void configButtons() {

        this.textView_score = (TextView) findViewById(R.id.text_view_score_main_activity);

        this.playAgainBtn = (Button) findViewById(R.id.button_play_again);

        playAgainBtn.setOnClickListener(
                (view) -> {
                    GameActivity.this.refreshHandler.sendEmptyMessage(GameValues.START);
                    GameActivity.this.playAgainBtn.setEnabled(false);
                    GameActivity.this.playAgainBtn.setClickable(false);
                    playAgainBtn.setBackgroundColor(Color.parseColor("#D3D3D3"));
                }
        );

        // Set the OnClickListeners
        this.findViewById(R.id.button_reset_main_activity).setOnClickListener(
                (view) -> {
                    if (isStarted) {
                        reset();
                    }
                }
        );
        //todo: change this for different navigation
        Button backToMenu = (Button) findViewById(R.id.button_back_to_menu);
        backToMenu.setOnClickListener(
                (view) -> {
                    startActivity(new Intent(GameActivity.this, MainActivity.class));
                }
        );

    }


    /**
     * Helper for onCreate method which initializes buttons with
     * appropriate lambdas representing onClick methods.
     */
    private void configGameButtons() {
        Button leftBtn = (Button) findViewById(R.id.image_button_left_main_activity);
        Button rotateBtn = (Button) findViewById(R.id.image_button_rotate_main_activity);
        Button rightBtn = (Button) findViewById(R.id.image_button_right_main_activity);
        Button downBtn = (Button) findViewById(R.id.image_button_down_main_activity);
        // config with lambdas
        leftBtn.setOnClickListener(
                (view) -> {
                    if (isGoing) moveLeft();
                }
        );

        rotateBtn.setOnClickListener(
                (view) -> {
                    if (isGoing) rotate();

                }
        );

        rightBtn.setOnClickListener(
                (view) -> {
                    if (isGoing) moveRight();
                }
        );

        downBtn.setOnClickListener(
                (view) -> {
                    if (isGoing) goDown();

                }
        );

    }

    /**
     * ********************************************************************************
     * Methods creating move of the block. Basically they just send
     * 'message' to the Handler. Is generated as a Thread (for efficiency) because of the Handler.
     * The thread just send a message to Handler which interacts with UI.
     */

    public void moveRight() {
        new Thread(() -> {
            GameActivity.this.refreshHandler.sendEmptyMessage(GameValues.RIGHT);
        }).start();
    }

    public void moveLeft() {
        new Thread(() -> {
            GameActivity.this.refreshHandler.sendEmptyMessage(GameValues.LEFT);
        }).start();
    }

    public void rotate() {
        new Thread(() -> {
            GameActivity.this.refreshHandler.sendEmptyMessage(GameValues.ROTATE);
        }).start();
    }

    public void goDown() {
        new Thread(() -> {
            GameActivity.this.refreshHandler.sendEmptyMessage(GameValues.DROP_DOWN);
        }).start();
    }

    /**
     * Sends message to the Handler to perform reset of the game.
     */
    public void reset() {
        new Thread(() -> {
            GameActivity.this.refreshHandler.sendEmptyMessage(GameValues.RESET);
        }).start();
    }


    /**
     * Generates next block and initializbonus bgc es nextBlockType field
     * and nextBlockTruthTable with id of a block and 'truth table'
     * for it.
     */
    public void generateNextBlock() {
        Random random = new Random();
        int randomNum = random.nextInt(7);
        this.nextBlockType = randomNum;
    }


    /**
     * The thread normally performs 'way down' of the block.
     *
     * @param milliseconds amount of time after which the thread will launch repeatedly
     */
    public void setNewTimer(int milliseconds) {
        this.timer = new Timer();
        // schedule a task to be performed repeatedly on fixed-rate
        this.timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                GameActivity.this.refreshHandler.sendEmptyMessage(GameValues.NORMAL_DOWN);
            }
        }, 1 * 1000, milliseconds);
    }

    /**
     * Shedules new TimerTask which sends a message to handler to
     * clear the whole view and reset the game.
     */
    public void clearAllCellsAndShowToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();

        this.timer = new Timer();
        this.timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {

                GameActivity.this.refreshHandler.sendEmptyMessage(GameValues.RESET_MAIN_VIEW);

                GameActivity.this.timer.cancel();
                GameActivity.this.timer.purge();

            }
        }, 0, 200);

    }


    /**
     * Pushes next block to the GameView and generates new next block
     */
    public void pushNextBlock() {

        this.gameView.pushBlock(nextBlockType);

        // redraw the MainView
        this.gameView.invalidate();

        this.generateNextBlock();
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public synchronized void addPoints(int pointsToAdd) {
        currentScore += pointsToAdd;
    }

    public synchronized void setCurrentScore(int currentScore) {
        this.currentScore = currentScore;
    }

    public void setPlayAgainBtnEnable() {
        playAgainBtn.setEnabled(true);
        playAgainBtn.setClickable(true);
        playAgainBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
    }



}
