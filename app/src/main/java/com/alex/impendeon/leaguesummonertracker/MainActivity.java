package com.alex.impendeon.leaguesummonertracker;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import net.rithms.riot.api.ApiConfig;
import net.rithms.riot.api.RiotApi;
import net.rithms.riot.api.RiotApiException;
import net.rithms.riot.api.endpoints.summoner.dto.Summoner;
import net.rithms.riot.api.endpoints.match.dto.MatchList;
import net.rithms.riot.api.endpoints.match.dto.MatchReference;
import net.rithms.riot.constant.Platform;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        TextView textView = (TextView) findViewById(R.id.sumname);
        try{
           Summoner t = initApi();
            textView.setText(Long.toString(t.getAccountId()));
        }
        catch (RiotApiException e){
            e.printStackTrace();
        }


    }

    public static Summoner initApi() throws RiotApiException{
        ApiConfig config = new ApiConfig().setKey("RGAPI-0a7e579d-07d9-4f4f-a417-f1e0d122353c");
        RiotApi api = new RiotApi(config);
        Summoner summoner = api.getSummonerByName(Platform.NA, "Impendeon");
        return summoner;

    }
    public static void startThread(){
        Thread apiThread = new Thread(new Runnable() {
            @Override
            public void run() {
                initApi();
            }
        });
    }
}
