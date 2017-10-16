package com.alex.impendeon.leaguesummonertracker;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
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

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import net.rithms.riot.api.endpoints.summoner.dto.Summoner;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    Context context;
    List<SummonerAccount>productList = new ArrayList<>();

    public MyAdapter(Context context, List<SummonerAccount> productList) {
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
        final SummonerAccount summonerAccount  = productList.get(position);

        holder.mTextView.setText(summonerAccount.summoner.getName());
        holder.mTextView2.setText(summonerAccount.getRank());
        if(summonerAccount.getRank().equals("DIAMOND")){
            holder.mImageView.setImageResource(R.drawable.diamond);
        }
        else if(summonerAccount.getRank().equals("PLATINUM")){
            holder.mImageView.setImageResource(R.drawable.platinum);
        }
        else if(summonerAccount.getRank().equals("GOLD")){
            holder.mImageView.setImageResource(R.drawable.gold);
        }
        else if(summonerAccount.getRank().equals("SILVER")){
            holder.mImageView.setImageResource(R.drawable.gold);
        }
        else if(summonerAccount.getRank().equals("BRONZE")){
            holder.mImageView.setImageResource(R.drawable.gold);
        }
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new MaterialDialog.Builder(context)
                        .title("Delete Summoner?")
                        .content("You must close the app for this to take effect")
                        .positiveText("Yes")
                        .negativeText("no")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                if(context instanceof MainActivity){
                                    ((MainActivity) context).deleteSummoner(summonerAccount.summoner);
                                    notifyDataSetChanged();
                                }
                            }
                        })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                            }
                        })
                        .show();
                return true;


            }
        });

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
            this.mImageView = (ImageView) itemView.findViewById(R.id.sumicon);
            this.mTextView = (TextView) itemView.findViewById(R.id.sumname);
            this.mTextView2 = (TextView) itemView.findViewById(R.id.title);
        }
    }
}