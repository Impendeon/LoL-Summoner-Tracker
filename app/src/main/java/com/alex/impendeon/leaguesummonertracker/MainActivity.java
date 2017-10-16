package com.alex.impendeon.leaguesummonertracker;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import net.rithms.riot.api.ApiConfig;
import net.rithms.riot.api.RiotApi;
import net.rithms.riot.api.RiotApiException;
import net.rithms.riot.api.endpoints.league.dto.LeagueList;
import net.rithms.riot.api.endpoints.league.methods.GetLeagueBySummonerId;
import net.rithms.riot.api.endpoints.summoner.dto.Summoner;
import net.rithms.riot.constant.Platform;


import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

//Git add -u
//git commit -m
//git push origin master
/*
TODO:
Finish making cards inflaterlayout when i want to add a new one.
Make sure it loads already added summoners.
Return a visable error if the summoner cant be found.
Make sure iceford isnt always being made.
 */
public class MainActivity extends AppCompatActivity {
    private ArrayList<SummonerAccount> summoners;
    private LayoutInflater inflater;
    private String name;
    private RecyclerView recyclerView;
    private MyAdapter myAdapter;
    private CoordinatorLayout coordinatorLayout;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Set<String> accountnames;
    final private static String RIOTKEY = "RGAPI-28814495-bbdf-4171-ace7-d62dc79bc427";
    Context context;
    NestedScrollView nestedScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        summoners = new ArrayList<SummonerAccount>();
        context = getApplicationContext();
        pref = getSharedPreferences("Accounts", MODE_PRIVATE);
        editor = pref.edit();
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinator);

        nestedScrollView = (NestedScrollView) findViewById(R.id.scrollview);

         recyclerView = (RecyclerView) findViewById(R.id.list);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

         myAdapter = new MyAdapter(this,summoners);
        recyclerView.setAdapter(myAdapter);

        accountnames = pref.getStringSet("Accounts", null);


        if(accountnames != null) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (String s : accountnames) {
                        try {
                            loadSummoner(s);
                        } catch (RiotApiException e) {
                        }

                    }
                }
            });
            thread.start();
//           MaterialDialog.Builder builder = new MaterialDialog.Builder(this).title("Getting Summoner").progress(true, 0);
//            MaterialDialog dialog = builder.build();
//            dialog.show();
            try {
                thread.join();
                myAdapter.notifyDataSetChanged();
                //dialog.dismiss();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        else if(accountnames == null){
            accountnames = new HashSet<String>();
        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Add New Summoner");
                inflater = getLayoutInflater();
                View dialoglayout = inflater.inflate(R.layout.add_summoner_dialog, null);
                final EditText editText = (EditText) dialoglayout.findViewById(R.id.addSummonerText);

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        name = editText.getText().toString();
                        Thread t = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    initApi(name);
                                    makeToast(true);
                                    editor.remove("Accounts");
                                    editor.commit();
                                    editor.putStringSet("Accounts", accountnames);
                                    editor.commit();
                                }
                                catch (RiotApiException e) {
                                    makeToast(false);
                                }
                            }
                        });
                       t.start();
                       try {
                           t.join();
                       }
                       catch (InterruptedException e){
                           e.printStackTrace();
                       }
                       myAdapter.notifyDataSetChanged();

                    }
                });

                builder.setView(dialoglayout);
                builder.show();
            }
        });




    }

    public void loadSummoner(String account) throws RiotApiException{
        ApiConfig config = new ApiConfig().setKey(RIOTKEY);
        RiotApi api = new RiotApi(config);
        Summoner summoner = api.getSummonerByName(Platform.NA, account);
        SummonerAccount summonerAccount = new SummonerAccount(summoner,
                api.getLeagueBySummonerId(Platform.NA, summoner.getId()));
        summonerAccount.setMatchList(api.getMatchListByAccountId(Platform.NA,summoner.getAccountId()));
        summonerAccount.getEloDecay();
        summoners.add(summonerAccount);
    }

    public void deleteSummoner(Summoner summoner){
        editor.remove("Accounts");
        editor.commit();
        accountnames.remove(summoner.getName());
        editor.putStringSet("Accounts", accountnames);
        editor.commit();
        summoners.remove(summoner);
    }


    public Summoner initApi(String account) throws RiotApiException{
        ApiConfig config = new ApiConfig().setKey(RIOTKEY);
        RiotApi api = new RiotApi(config);
        Summoner summoner = api.getSummonerByName(Platform.NA, account);
        SummonerAccount summonerAccount = new SummonerAccount(summoner,
                api.getLeagueBySummonerId(Platform.NA, summoner.getId()));
        summonerAccount.setMatchList(api.getMatchListByAccountId(Platform.NA,summoner.getAccountId()));
        summoners.add(summonerAccount);
        accountnames.add(summonerAccount.summoner.getName());
        return summoner;

    }

    public void makeToast(boolean b){
        if(b){
            Snackbar snackbar = Snackbar.make(coordinatorLayout, "Success", Snackbar.LENGTH_LONG);

            snackbar.show();
        }
        else {
            Snackbar snackbar = Snackbar.make(coordinatorLayout, "Error Account Not Added", Snackbar.LENGTH_LONG);

            snackbar.show();
        }

    }


}
