package com.alex.impendeon.leaguesummonertracker;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import net.rithms.riot.api.ApiConfig;
import net.rithms.riot.api.RiotApi;
import net.rithms.riot.api.RiotApiException;
import net.rithms.riot.api.endpoints.summoner.dto.Summoner;
import net.rithms.riot.constant.Platform;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.FadeInDownAnimator;

public class MainActivity extends AppCompatActivity {
    private ArrayList<SummonerAccount> summoners;
    private LayoutInflater inflater;
    private String name;
    private String password;
    private String username;
    private RecyclerView recyclerView;
    private MyAdapter myAdapter;
    private CoordinatorLayout coordinatorLayout;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Set<String> accountnames;
    final private static String RIOTKEY = BuildConfig.API_KEY;
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
        recyclerView.setItemAnimator(new FadeInDownAnimator());
        recyclerView.setAdapter(new AlphaInAnimationAdapter(myAdapter));



        accountnames = pref.getStringSet("Accounts", null);


        if(accountnames != null) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (String s : accountnames) {
                        try {
                            String server = s.substring(s.indexOf("*") + 1, s.indexOf("#"));
                            String accname = s.substring(0, s.indexOf("*"));
                            String username = s.substring(s.indexOf("#")+1, s.indexOf("&"));
                            String password = "";
                            if(!(s.indexOf("&") == s.length() - 1))
                                password = s.substring(s.indexOf("&")+1, s.length());
                            loadSummoner(accname,username,password, server); //FIX
                        } catch (RiotApiException e) {
                        }

                    }
                }
            });
            thread.start();
            try {
                thread.join();
                myAdapter.notifyDataSetChanged();
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
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                builder.setTitle("Add New Summoner");
                inflater = getLayoutInflater();
                View dialoglayout = inflater.inflate(R.layout.add_summoner_dialog, null);
                final EditText editTextSummoner = (EditText) dialoglayout.findViewById(R.id.addSummonerText);
                final EditText editTextUsername = (EditText) dialoglayout.findViewById(R.id.username);
                final EditText editTextPassword = (EditText) dialoglayout.findViewById(R.id.password);
                final Spinner spinner = dialoglayout.findViewById(R.id.serverlist);

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        name = editTextSummoner.getText().toString();
                        username = editTextUsername.getText().toString();
                        password = editTextPassword.getText().toString();
                        final String server = spinner.getSelectedItem().toString();
                        Thread t = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    initApi(name, username, password, server);
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
                           myAdapter.notifyItemInserted(accountnames.size()-1);
                       }
                       catch (InterruptedException e){
                           e.printStackTrace();
                       }
                    }
                });

                builder.setView(dialoglayout);
                builder.show();
            }
        });




    }

    public void loadSummoner(String account, String username, String password, String platform) throws RiotApiException{
        ApiConfig config = new ApiConfig().setKey(RIOTKEY);
        RiotApi api = new RiotApi(config);
        Summoner summoner = api.getSummonerByName(Platform.getPlatformByName(platform), account);
        SummonerAccount summonerAccount = new SummonerAccount(summoner,
                api.getLeagueBySummonerId(Platform.getPlatformByName(platform), summoner.getId()));
        summonerAccount.setMatchList(api.getMatchListByAccountId(Platform.getPlatformByName(platform),summoner.getAccountId()));
        summonerAccount.getEloDecay();
        summonerAccount.username = username;
        summonerAccount.password = password;
        summoners.add(summonerAccount);
        myAdapter.notifyItemInserted(summoners.size());
    }

    public void deleteSummoner(SummonerAccount summoner, String platform){
        editor.remove("Accounts");
        editor.commit();
        accountnames.remove(summoner.summoner.getName() + "*" + platform + "#" + summoner.username + "&" + summoner.password);
        editor.putStringSet("Accounts", accountnames);
        editor.commit();
        summoners.remove(summoner);
        myAdapter = new MyAdapter(this,summoners);
        recyclerView.setAdapter(myAdapter);
    }


    public Summoner initApi(String account, String username, String password, String platform) throws RiotApiException{
        ApiConfig config = new ApiConfig().setKey(RIOTKEY);
        RiotApi api = new RiotApi(config);
        Summoner summoner = api.getSummonerByName(Platform.getPlatformByName(platform), account);
        SummonerAccount summonerAccount = new SummonerAccount(summoner,
                api.getLeagueBySummonerId(Platform.getPlatformByName(platform), summoner.getId()));
        summonerAccount.setMatchList(api.getMatchListByAccountId(Platform.getPlatformByName(platform),summoner.getAccountId()));
        if(username == null)
            username = "";
        if(password == null)
            password = "";
        summonerAccount.username = username;
        summonerAccount.password = password;
        summoners.add(summonerAccount);
        accountnames.add(summonerAccount.summoner.getName() + "*" + platform + "#" + username + "&" + password);
        return summoner;

    }

    public void makeToast(boolean b){
        String s = "Error Account Not Added";
        if(b){
            s = "Success";
        }
            Snackbar snackbar = Snackbar.make(coordinatorLayout, s, Snackbar.LENGTH_LONG);
            snackbar.show();

    }


}
