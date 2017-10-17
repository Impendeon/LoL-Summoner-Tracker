package com.alex.impendeon.leaguesummonertracker;

import android.util.Log;

import net.rithms.riot.api.RiotApiException;
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
Return the differnece in the data in EPOCH TIME and convert it to a date
 */

public class SummonerAccount {
    public Summoner summoner;
    public List<LeagueList> leagueListList;
    public MatchList matchList;

    public SummonerAccount(Summoner summoner, List<LeagueList> leagueLists, MatchList matchList){
        this.summoner = summoner;
        this.leagueListList = leagueLists;
        this.matchList = matchList;
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
    public String getEloDecay() {
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

        return "You will decay in " + daysTillDecay + " days";
    }
}
