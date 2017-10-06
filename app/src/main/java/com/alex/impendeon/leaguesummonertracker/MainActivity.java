package com.alex.impendeon.leaguesummonertracker;

import android.os.AsyncTask;
import android.os.Bundle;
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
import net.rithms.riot.constant.Platform;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

//Git add -u
//git commit -m
//git push origin master

public class MainActivity extends AppCompatActivity {
    private TextView textView;
    private File internalStorage;
    private ArrayList<Summoner> summoners;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        summoners = new ArrayList<Summoner>;

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
        storageRunnable runnable = new storageRunnable();
        runnable.run();


    }

    public Summoner initApi(String account) throws RiotApiException{
        ApiConfig config = new ApiConfig().setKey("RGAPI-7b758041-5933-4bd1-a9d1-e65a9758094c");
        RiotApi api = new RiotApi(config);
        Summoner summoner = api.getSummonerByName(Platform.NA, account);
        summoners.add(summoner);
        return summoner;

    }

    public void writeData(Summoner summoner){
        File save = new File(internalStorage, "Data.txt");
        try{
            FileWriter w = new FileWriter(save);
            w.append(summoner.getName() + "|" + summoner.getAccountId());
            w.flush();
            w.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }


    }


    private class getDataUpdateUITask extends AsyncTask<String, Void, Summoner>{
        @Override
        protected Summoner doInBackground(String... strings) {
            Summoner ret = null;
            try{
                ret = initApi(strings[0]);
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

    public class storageRunnable implements Runnable{
        @Override
        public void run() {
            internalStorage = getFilesDir();
            File accounts = new File(internalStorage, "Accounts");
            if(!accounts.exists()){
                accounts.mkdirs();
            }
            writeData(summoners.get(0));
        }
    }
}
