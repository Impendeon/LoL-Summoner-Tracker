package com.alex.impendeon.leaguesummonertracker;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by alex on 10/16/17.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.rithms.riot.api.endpoints.summoner.dto.Summoner;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    Context context;
    List<Summoner>productList = new ArrayList<>();

    public MyAdapter(Context context, List<Summoner> productList) {
        this.context = context;
        this.productList = productList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cardview_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Summoner summoner  = productList.get(position);

        holder.mTextView.setText(summoner.getName());
       // holder.mTextView2.setText(Long.toString(summoner.getAccountId()));
        //holder.mImageView.setImageResource(summoner.getProfileIconId());
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageView;
        TextView mTextView;
        TextView mTextView2;
        public ViewHolder(View itemView) {
            super(itemView);
            //this.mImageView = (ImageView) itemView.findViewById(R.id.sumicon);
            this.mTextView = (TextView) itemView.findViewById(R.id.sumname);
            //his.mTextView2 = (TextView) itemView.findViewById(R.id.showTitle);
        }
    }
}