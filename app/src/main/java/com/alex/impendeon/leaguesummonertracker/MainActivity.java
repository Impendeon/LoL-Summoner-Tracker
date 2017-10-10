package com.alex.impendeon.leaguesummonertracker;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
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
import java.io.FileWriter;
import java.io.IOException;
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
    private TextView textView;
    private File internalStorage;
    private ArrayList<Summoner> summoners;
    private ArrayList<Cards> cards;
    private int cardsMade;
    final private static String RIOTKEY = "RGAPI-f1f38954-50f8-441f-a37d-0ec68396064f";
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
        cardsMade = 0;
        NestedScrollView nestedScrollView = (NestedScrollView) findViewById(R.id.scrollview);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Add New Summoner");
                final EditText editText = (EditText) findViewById(R.id.addSummonerText);

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                       getDataUpdateUITask t = new getDataUpdateUITask();
                       t.execute("iceford");
                    }
                });
                LayoutInflater inflater = getLayoutInflater();
                View dialoglayout = inflater.inflate(R.layout.add_summoner_dialog, null);
                builder.setView(dialoglayout);
                builder.show();
            }
        });


        textView = (TextView) findViewById(R.id.sumname);
//        SharedPreferences accounts = getSharedPreferences("UserInfo", 0);//Gets data from shareaccount and returns "" if it does not exist
//        textView.setText(accounts.getString("Username", ""));
        getDataUpdateUITask task = new getDataUpdateUITask();
        task.execute("Impendeon");


    }

    public void createCardView(){
       CardView cardview = new CardView(context);

        LinearLayout.LayoutParams layoutparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        cardview.setLayoutParams(layoutparams);

        cardview.setRadius(15);

        cardview.setPadding(25, 25, 25, 25);

        cardview.setCardBackgroundColor(Color.MAGENTA);

        cardview.setMaxCardElevation(30);

        cardview.setMaxCardElevation(6);

        Cards card = cards.get(cardsMade);
        card.setTextView(new TextView(context));

        card.textView.setLayoutParams(layoutparams);

        card.textView.setText("CardView Programmatically");

        card.textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);

        cardiew.setTextColor(Color.WHITE);

        textview.setPadding(25,25,25,25);

        textview.setGravity(Gravity.CENTER);

        cardview.addView(textview);

        nestedScrollView.addView(cardview);
    }

    public Summoner initApi(String account) throws RiotApiException{
        ApiConfig config = new ApiConfig().setKey(RIOTKEY);
        RiotApi api = new RiotApi(config);
        Summoner summoner = api.getSummonerByName(Platform.NA, account);
        summoners.add(summoner);
        SharedPreferences accounts = getSharedPreferences("UserInfo", 0);
        SharedPreferences.Editor editor = accounts.edit();
        editor.putString("Username",summoner.getName());
        editor.commit();
        return summoner;

    }

//    public void writeData(Summoner summoner){
//        File save = new File(internalStorage, "Data.txt");
//        try{
//            FileWriter w = new FileWriter(save);
//            w.append(summoner.getName() + "|" + summoner.getAccountId());
//            w.flush();
//            w.close();
//        }
//        catch(IOException e){
//            e.printStackTrace();
//        }
//
//
//    }


    private class getDataUpdateUITask extends AsyncTask<String, Void, Summoner>{
        @Override
        protected Summoner doInBackground(String... strings) {
            Summoner ret = null;
            try{
                for(int i = 0; i < strings.length; i++){
                   ret = initApi(strings[i]);
                }
            }
            catch (RiotApiException e){
                e.printStackTrace();
            }
            return ret;
        }

        @Override
        protected void onPostExecute(Summoner summoner) {
            if(summoners != null)
            for(int i = 0; i < summoners.size(); i++){
                textView.setText(Long.toString(summoners.get(i).getAccountId()));
            }
        }
    }

//    public class storageRunnable implements Runnable{
//        @Override
//        public void run() {
//            internalStorage = getFilesDir();
//            File accounts = new File(internalStorage, "Accounts");
//            if(!accounts.exists()){
//                accounts.mkdirs();
//            }
//            writeData(summoners.get(0));
//        }
//    }
}
