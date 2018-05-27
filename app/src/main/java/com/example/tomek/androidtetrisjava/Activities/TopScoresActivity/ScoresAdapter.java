package com.example.tomek.androidtetrisjava.Activities.TopScoresActivity;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tomek.androidtetrisjava.R;
import com.example.tomek.androidtetrisjava.database.TopScore;

import java.util.List;

public class ScoresAdapter extends RecyclerView.Adapter<ScoresAdapter.MyViewHolder>{

    private List<TopScore> topScoreList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView playerName, score;

        public MyViewHolder(View view) {
            super(view);
            playerName = (TextView) view.findViewById(R.id.player_name);
            score = (TextView) view.findViewById(R.id.score);
        }
    }


    public ScoresAdapter(List<TopScore> topScoreList) {
        this.topScoreList = topScoreList;
    }




    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        TopScore singleScore = topScoreList.get(position);

        holder.score.setText(singleScore.getScore());
        holder.playerName.setText(singleScore.getPlayerName());
    }

    @Override
    public int getItemCount() {
        return topScoreList.size();
    }


}
