/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thesis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * what functionality goes in here, if anything? 
 * seems like we need a tournament constructor which takes in ArrayList of players (sorted?)
 * how much functionality do we want the constructor to have? 
 * also need a way to create the matches 
 *      - for loop that caps at number of matches to be played
 */
public class Tournament {
    private int numRounds, numPlayers;
    private int numLosers = 0;
    private int bet, payout;
    private int numFirstRoundMatches;
    private double betOnPlayerWinChance;
    private ArrayList<Player> winnersPlayers = new ArrayList<Player>();
    private ArrayList<Player> losersPlayers = new ArrayList<Player>();
    private ArrayList<Player> playerHoldingArea = new ArrayList<Player>();
    private ArrayList<Match> tourneyMatches = new ArrayList<Match>();
    private ArrayList<Match> winnersMatches = new ArrayList<Match>();
    private ArrayList<Match> losersMatches = new ArrayList<Match>();
    private Player winnersFinalsLoser = null;
    private Player winnersChampion, losersChampion, champion, playerBetOn, loserOfWinnersFinals;
    private String matchLog = "";
    private boolean isDoubleElim, isBetting;
    private boolean isFirstRound = true;
    private boolean isFirstTwoRounds = true;
    private boolean areThereFloatedPlayers;
    private ArrayList<Player> floatedWinners = new ArrayList<Player>();
    private List<Player> floatedLosers = new ArrayList<Player>();
    
    
    public Tournament(ArrayList<Player> players){
        Collections.sort(players); // sorting players by ELO so that we don't have to pass in a sorted list 
        winnersPlayers = players; // everyone starts out in winners bracket, so that's where we put all the players at first
        numPlayers = players.size();
    }
    
    public void setTourneyPlayers(ArrayList<Player> tourneyRoster){
       this.winnersPlayers = tourneyRoster; // simple setter method that also sorts list of players provided
       numPlayers = tourneyRoster.size();
       Collections.sort(winnersPlayers);
    }
    
    public void addPlayerToRoster (Player playerToAdd){
        winnersPlayers.add(playerToAdd);
        Collections.sort(winnersPlayers); // add player, then sort list of entrants by ELO
    }
    
    public Player getChampion(){
        return this.champion;
    }
    
    public void setPlayerBetOn(Player p){
        this.playerBetOn = p;
    }
    
    public void betOnTourney(Player playerChosen, int betAmount, double playerWinProbability){ // setting player user bet on, amount they bet, and likelihood of player winning
        this.isBetting = true;
        this.playerBetOn = playerChosen;
        this.bet = betAmount;
        this.betOnPlayerWinChance = playerWinProbability;
    }
    
    public int payOutBet(){
        if(this.champion.equals(playerBetOn)){
            return (int)(this.bet / betOnPlayerWinChance); // since likelihood of player winning will be a percentage of 1, dividing gets us the payout
        } else return 0;
    }
    
    public Player getPlayerBetOn(){
        return this.playerBetOn;
    } 
    
    public boolean getBettingStatus(){
        return this.isBetting;
    }
    
    public void setDoubleElim(boolean condition){
        if (condition == true){
            isDoubleElim = true;
        }
    }
    
    public boolean getDoubleElim(){
        if (isDoubleElim == true){
            return true;
        } else {return false;}
        }
    
    /**
     * for loop in generate matches that goes through winnersPlayers, caps at number of number of players / 2
     * how do we generate matches according to seeding?
     * create match with first index, thennnnnnn
     */
    public void generateUpperMatches(){ // goes through arrayList of players in tournamenet and generates pairings, with first paired with last, second with seceond-to-last, etc
        /** if winnersPlayers isn't a power of 2, float the next ??? number of entrants to next round of winners bracket
         *  once we're past the first round, number of players in winners should always be a power of 2
         */
        if (winnersChampion == null){
            double tourneyEntrants = winnersPlayers.size();
            double nRounds = Math.ceil(Math.log(tourneyEntrants)/Math.log(2)); // this gets us the number of rounds a given bracket will take to get to grands
            double numberEntrySlots = Math.pow(2, nRounds); // (two^rounds) entrants for each round, as 1 round means two players, 2 means four, 3 means 8, etc.
            if(numberEntrySlots != tourneyEntrants){
                areThereFloatedPlayers = true;
                int numberByes = (int) (numberEntrySlots - tourneyEntrants);
                for (int i = 0; i < numberByes; i++) {
                    floatedWinners.add(winnersPlayers.get(0));
                    winnersPlayers.remove(0);
                 }
                         }
                for (int i = 0; i < winnersPlayers.size() / 2; i++) {
                    Match newMatch = new Match(winnersPlayers.get(i), winnersPlayers.get(winnersPlayers.size() - i - 1));
                    winnersMatches.add(newMatch);
                 }  
        }
    }
    
    public void generateLowerMatches(){   
        System.out.println("number of players in losers bracket: " + losersPlayers.size());
        if (isDoubleElim == true && losersPlayers.isEmpty() == false && losersPlayers.size() > 1){
            if (areThereFloatedPlayers == true){
                System.out.println("we have byes");
                double losersEntrants = losersPlayers.size();
                double nRounds = Math.ceil(Math.log(losersEntrants)/Math.log(2));
                double numPlayerSlots = Math.pow(2, nRounds);
                int numByes = (int)(numPlayerSlots - losersEntrants);
                for (int i = 0; i < numByes; i++) {
                    floatedLosers.add(losersPlayers.get(0));
                    losersPlayers.remove(0);
                }
                for (int i = 0; i < losersPlayers.size() / 2; i++) {
                     Match newLosersMatch = new Match(losersPlayers.get(i), losersPlayers.get(losersPlayers.size() - i - 1));
                     losersMatches.add(newLosersMatch);
                }
            }
            if(areThereFloatedPlayers == false){
            for (int i = 0; i < losersPlayers.size() / 2; i++) {
                Match newLosersMatch = new Match(losersPlayers.get(i), losersPlayers.get(losersPlayers.size() - 1 -i));
                System.out.println("in the generate losers matches loop");
                losersMatches.add(newLosersMatch);
            }
            }
            System.out.println("losers matches: " + losersMatches.toString());
        }
    }
    
    public void playUpperMatches(){
        ArrayList<Player> nextRoundWinners = new ArrayList<>();
        for (int i = 0; i < floatedWinners.size(); i++) {
            nextRoundWinners.add(floatedWinners.get(i));
        }
        floatedWinners.clear();
        if(this.isDoubleElim == true){
            matchLog = matchLog.concat("\n Round of " + (winnersPlayers.size() + losersPlayers.size() + nextRoundWinners.size())+ ":" + "\n");
        } else {
            matchLog = matchLog.concat("\n Round of " + winnersPlayers.size() + ":" + "\n");
        }
        matchLog = matchLog.concat("In winners: \n");
        for (int i = 0; i < winnersMatches.size(); i++) {
            winnersMatches.get(i).randomPredictMatch();
            if (winnersMatches.size() == 1){
                winnersFinalsLoser = winnersMatches.get(i).getMatchLoser();
                System.out.println("winners finals loser: " + winnersFinalsLoser.toString());
            } else if (isFirstTwoRounds == true){
                losersPlayers.add(winnersMatches.get(i).getMatchLoser());
            } else if (isFirstTwoRounds == false){
                playerHoldingArea.add(winnersMatches.get(i).getMatchLoser());
            }
            nextRoundWinners.add(winnersMatches.get(i).getMatchWinner()); // progressing winner of match i to the next round of winners' bracket    
            matchLog = matchLog.concat(winnersMatches.get(i).getMatchWinner().getName() + " beat " + winnersMatches.get(i).getMatchLoser().getName() + "\n");
        }
        int sumOfNextRoundLosersAndLosers = losersPlayers.size() + playerHoldingArea.size();
        if ((sumOfNextRoundLosersAndLosers & (sumOfNextRoundLosersAndLosers - 1)) == 0){
            for (int i = 0; i < playerHoldingArea.size(); i++) {
                losersPlayers.add(playerHoldingArea.get(i));
            }
            playerHoldingArea.clear();
        }
        winnersMatches.clear(); // clearing winnersMatches of the previous round of matches so we can start fresh next time we go through generateWinnersMatches
        winnersPlayers = nextRoundWinners; // setting winnersPlayers to players in next round of winners so we can generate next round's matches in generateWinnersMatches
        if(winnersPlayers.size() == 1){
            setWinnersChampion(winnersPlayers.get(0));
        }
    }
    
    public void playLowerMatches(){
        ArrayList<Player> nextRoundLosers = new ArrayList<Player>();
        for (int i = 0; i < floatedLosers.size(); i++) {
            nextRoundLosers.add(floatedLosers.get(i));
        }
        floatedLosers.clear();
        System.out.println("at start of playLowerMatches method");
        System.out.println("nextRoundLosers: " + nextRoundLosers.toString());
        if(matchLog.isEmpty() == false){
             matchLog = matchLog.concat("In losers: \n");
        }
        for (int i = 0; i < losersMatches.size(); i++) {
            losersMatches.get(i).randomPredictMatch();
            nextRoundLosers.add(losersMatches.get(i).getMatchWinner()); // progressing winner of match i to next round of losers' bracket
            System.out.println(losersMatches.get(i).getMatchWinner().getName() + " beat " + losersMatches.get(i).getMatchLoser().getName() + " in losers");  
            matchLog = matchLog.concat(losersMatches.get(i).getMatchWinner().getName() + " beat " + losersMatches.get(i).getMatchLoser().getName() + "\n");
        } // loops through each match in losers, then progresses winners to next round of losers
        losersMatches.clear(); // clears losers matches of previous round of matches so we can start fresh before adding next round of matches to it
        System.out.println("nextRoundLosers: " + nextRoundLosers.toString());
        for (int i = 0; i < playerHoldingArea.size(); i++) {
            System.out.println("adding player from holding area");
            nextRoundLosers.add(playerHoldingArea.get(i));
        }
        playerHoldingArea.clear();
        losersPlayers = nextRoundLosers; // losersPlayers used in spots where we need number of players in losers' bracket and to generate matches with
        System.out.println("losersPlayers: " + losersPlayers.toString());
        System.out.println(" (playLowerMatches) losersPlayers: " + losersPlayers.toString());
        System.out.println(losersPlayers.size() + "in playlosers"); // debugging tool
    }
    
    public void playSingleElimTournament(){
        isDoubleElim = false;
        while(winnersPlayers.size() != 1){ // simulates until we have one player remaining, and therefore our champion
            generateUpperMatches();
            playUpperMatches();
       }
        this.champion = winnersPlayers.get(0);
        System.out.println("The winner is:" + winnersPlayers.get(0).getName());
        matchLog = matchLog.concat("Your champion is: " + winnersPlayers.get(0).getName());
    }
    
    public void playDoubleElimTournament(){
        isDoubleElim = true;
        while(winnersPlayers.size() > 1){ // while there's more than one player in either bracket, simulate the bracket
            playDoubleElimRound();
        }
       losersPlayers.add(winnersFinalsLoser);
        while(losersPlayers.size() > 1){
           generateLowerMatches(); // generating and making one more round of losers matches because upon completion of while loop, we have two players in losers': loser of winners finals and winner of losers semis
           playLowerMatches(); 
        }
        setLosersChampion(losersPlayers.get(0));
        playDoubleElimGrands();
    }
    
    public void playDoubleElimGrands(){
        int winnerSideWins = 0;
        int loserSideWins = 0;
        matchLog = matchLog.concat("Grand finals: " + this.winnersChampion.getName() + " vs " + this.losersChampion.getName() + "\n");
        Match grandFinals = new Match(this.winnersChampion, this.losersChampion);
        while(loserSideWins != 2 && winnerSideWins != 1){ // losers bracket champ needs to win two sets, winners bracket champ only needs to win one
             grandFinals.randomPredictMatch();
             if (grandFinals.getMatchWinner() == this.winnersChampion){
                 winnerSideWins++;
             } else {
                 loserSideWins++;
             }
             System.out.println("set count: " + winnersChampion.getName() + " " + winnerSideWins + ", " + losersChampion.getName() + " " + loserSideWins);
        }
        if (winnerSideWins == 1){ //since loser is coming from lower bracket, they need to win once to knock player from winners side down to losers, then win again to win tournament
            System.out.println("The winner is: " + this.winnersChampion.getName());
            this.champion = winnersChampion;
            matchLog = matchLog.concat(this.winnersChampion.getName() + " is your champion!");
        } else if (loserSideWins == 2){
            this.champion = losersChampion;
            System.out.println("The winner is: " + this.losersChampion.getName());
            matchLog = matchLog.concat(this.losersChampion.getName() + " is your champion!");
        }

        // if match winner is winners champ, increment by one, same for losers champ
        // make while loop, while winner's wins not at 1 and loser's wins not at 2 play match
        // if winner's counter is at 1, they win series, if loser's counter is at 2, they win seriess
    }
    
    public void playRound(){
        playUpperMatches();
        System.out.println("Next round matches: " + winnersPlayers.toString());
    }
    
    public void playDoubleElimRound(){
        generateLowerMatches();
        playLowerMatches();
        System.out.println("playing first round of lower matches");
        if(isFirstTwoRounds == true){
            generateUpperMatches();
            playUpperMatches();
            isFirstTwoRounds = false;
        }
        if(areThereFloatedPlayers == true){
            generateUpperMatches();
            playUpperMatches();
        }
        if (areThereFloatedPlayers == true){
            System.out.println("aretherefloatedplayers = true, generating losers' matches");
            generateLowerMatches();
            playLowerMatches();
            areThereFloatedPlayers = false;
        }
        generateLowerMatches();
        playLowerMatches();
        System.out.println("playing second round of lower matches");
        generateUpperMatches();
        playUpperMatches();
    }
    
    public void betOnMatch(){
        
    }    
    public void setWinnersChampion(Player champ){
        this.winnersChampion = champ;
    }
    
    public void setLosersChampion(Player champ){
        this.losersChampion = champ;
    }
    
    public ArrayList<Match> getTourneyMatches(){
        return this.tourneyMatches;
    }
    
    public ArrayList<Match> getWinnersMatches(){
        return this.winnersMatches;
    }
    
    public ArrayList<Match> getLosersMatches(){
        return this.losersMatches;
    }
    
    public void setMatchLog (String newLog){
        this.matchLog = newLog;
    }
    
    public String getMatchLog(){
        return this.matchLog;
    }
    
    public boolean checkIfEntrantsIsPower(int entrants){
        return (entrants != 0) && ((entrants &(entrants -1)) == 0); 
    }
    
    public String toString(){  
        return winnersMatches.toString() + "\n In losers: " + losersMatches.toString();
    }
}
