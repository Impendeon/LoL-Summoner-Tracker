package com.alex.impendeon.leaguesummonertracker;

import android.util.Log;

import net.rithms.riot.api.RiotApiException;
import net.rithms.riot.api.endpoints.league.dto.LeagueItem;
import net.rithms.riot.api.endpoints.league.dto.LeagueList;
import net.rithms.riot.api.endpoints.match.dto.MatchList;
import net.rithms.riot.api.endpoints.match.dto.MatchReference;
import net.rithms.riot.api.endpoints.summoner.dto.Summoner;
import net.rithms.riot.api.endpoints.league.methods.GetLeagueBySummonerId;
import net.rithms.riot.api.endpoints.league.constant.LeagueQueue;

import java.util.List;

/**
 * Created by alex on 10/16/17.
 */
/*
TODO:
 */

public class SummonerAccount {
    public Summoner summoner;
    public List<LeagueList> leagueListList;
    public String link;
    public String server;

    public LeagueItem getLeagueItem() {
        return leagueItem;
    }

    public void setLeagueItem(LeagueItem leagueItem) {
        this.leagueItem = leagueItem;
    }

    public LeagueItem leagueItem;
    public MatchList matchList;

    public SummonerAccount(Summoner summoner, List<LeagueList> leagueLists, MatchList matchList){
        this.summoner = summoner;
        this.leagueListList = leagueLists;
        this.matchList = matchList;
        this.server = server;
        this.link = server+".op.gg/summoner/username=" + summoner.getName();
    }

    public SummonerAccount(Summoner summoner, List<LeagueList> leagueLists){
        this.summoner = summoner;
        this.leagueListList = leagueLists;
    }

    public void setMatchList(MatchList matchList){
        this.matchList = matchList;
    }

    public String getRank(){
        for(LeagueList l : leagueListList){
            if(l.getQueue().equals(LeagueQueue.RANKED_SOLO_5x5.name())){
                return l.getTier();
            }
        }
        return "none";
    }
    public String getLP(){
        return leagueItem.getRank() + " " + leagueItem.getLeaguePoints();

    }
    public String getEloDecay() {
        if(getRank().equals("GOLD") || getRank().equals("SILVER") || getRank().equals("BRONZE") || getRank().equals("PROVISIONAL"))
            return "N/A";
        boolean master = false;
        if(getRank().equals("MASTER") || getRank().equals("CHALLENGER"))
            master = true;
        long time = 0;
        if (matchList.getMatches() != null) {
            for (MatchReference match : matchList.getMatches()) {
               if(match.getQueue() == 420) {
                   time = match.getTimestamp();
                   break;
               }

            }
        }
        long tsLong = System.currentTimeMillis();
        long difference = Math.abs(tsLong) - Math.abs(time);
        int days = (int) (difference / (1000*60*60*24));
        int daysTillDecay = 28 - days;
        if(master)
            daysTillDecay = 7 - days;

        return "You will decay in " + daysTillDecay + " days";
    }
}
