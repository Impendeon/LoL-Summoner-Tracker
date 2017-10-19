package com.alex.impendeon.leaguesummonertracker;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by alex on 10/16/17.
 */

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final SummonerAccount summonerAccount  = productList.get(position);

        holder.mTextView.setText(summonerAccount.summoner.getName());
//        holder.mTextView.setClickable(true);
//        holder.mTextView.setMovementMethod(LinkMovementMethod.getInstance());
//        holder.mTextView.setText(Html.fromHtml( "<a href='"+summonerAccount.link+"'>"+summonerAccount.summoner.getName()+"</a>"));
        holder.mTextView2.setText(summonerAccount.getEloDecay());
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
        else if(summonerAccount.getRank().equals("CHALLENGER")){
            holder.mImageView.setImageResource(R.drawable.challenger);
        }
        else if(summonerAccount.getRank().equals("MASTER")){
            holder.mImageView.setImageResource(R.drawable.master);
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
                                    ((MainActivity) context).deleteSummoner(summonerAccount, summonerAccount.server);
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
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                new MaterialDialog.Builder(context).title("Login Info").customView(R.layout.login_info_dialog, false).show();
            }
        });
        //SAVE COLOR

    }


    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageView;
        TextView mTextView;
        TextView mTextView2;
        CardView cardView;
        public ViewHolder(View itemView) {
            super(itemView);
            this.mImageView = (ImageView) itemView.findViewById(R.id.tier);
            this.mTextView = (TextView) itemView.findViewById(R.id.sumname);
            this.mTextView2 = (TextView) itemView.findViewById(R.id.title);
            this.cardView = (CardView) itemView.findViewById(R.id.card_view);
        }

    }
}