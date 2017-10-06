package com.alex.impendeon.leaguesummonertracker;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        textView = (TextView) findViewById(R.id.sumname);
        getDataUpdateUITask task = new getDataUpdateUITask();
        task.execute();


    }

    public static Summoner initApi() throws RiotApiException{
        ApiConfig config = new ApiConfig().setKey("RGAPI-7b758041-5933-4bd1-a9d1-e65a9758094c");
        RiotApi api = new RiotApi(config);
        Summoner summoner = api.getSummonerByName(Platform.NA, "Impendeon");
        //textView.setText(Long.toString(summoner.getAccountId()));
        return summoner;

    }

//    public void meow(){
//        Handler handler = new Handler();
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        })
//    }
//    public void startThread(TextView textView){
//        Handler handler = new Handler(Looper.getMainLooper());
//        handler.post(new Runnable(){
//    tvLocation.setText(getaddress());
//    tvTime.setText(r.getStringDate());
//        });
//    }
    private class getDataUpdateUITask extends AsyncTask<Void, Void, Summoner>{
        @Override
        protected Summoner doInBackground(Void... voids) {
            Summoner ret = null;
            try{
                ret = initApi();
            }
            catch (RiotApiException e){
                e.printStackTrace();
            }
            return ret;
        }

        @Override
        protected void onPostExecute(Summoner summoner) {
            if(summoner != null)
            textView.setText(Long.toString(summoner.getAccountId()));
        }
    }
}
