package com.alex.impendeon.leaguesummonertracker;

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

import net.rithms.riot.api.ApiConfig;
import net.rithms.riot.api.RiotApi;
import net.rithms.riot.api.RiotApiException;
import net.rithms.riot.api.endpoints.summoner.dto.Summoner;
import net.rithms.riot.constant.Platform;


import java.io.File;
import java.util.ArrayList;

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
    private ArrayList<Summoner> summoners;
    private LayoutInflater inflater;
    private String name;
    private RecyclerView recyclerView;
    private MyAdapter myAdapter;
    private CoordinatorLayout coordinatorLayout;
    final private static String RIOTKEY = "RGAPI-28814495-bbdf-4171-ace7-d62dc79bc427";
    Context context;
    NestedScrollView nestedScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        summoners = new ArrayList<Summoner>();
        context = getApplicationContext();
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinator);

        nestedScrollView = (NestedScrollView) findViewById(R.id.scrollview);

         recyclerView = (RecyclerView) findViewById(R.id.list);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

         myAdapter = new MyAdapter(this,summoners);
        recyclerView.setAdapter(myAdapter);

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


    public Summoner initApi(String account) throws RiotApiException{
        ApiConfig config = new ApiConfig().setKey(RIOTKEY);
        RiotApi api = new RiotApi(config);
        Summoner summoner = api.getSummonerByName(Platform.NA, account);
        summoners.add(summoner);
//        Log.d("MainActivity", "headFdsa");
//       SharedPreferences accounts = getSharedPreferences("UserInfo", 0);
//       SharedPreferences.Editor editor = accounts.edit();
//        editor.putString("Username",summoner.getName());
//        editor.commit();
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
