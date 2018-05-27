package com.example.tomek.androidtetrisjava.database;

import java.io.Serializable;


/**
 * Represents single score
 */
public class TopScore implements Serializable {

    private String player_name;
    private String score;




    public TopScore(String score, String player_name) {
        this.player_name = player_name;
        this.score = score;
    }



    public String getPlayerName() {
        return player_name;
    }

    public String getScore() {
        return score;
    }
}
