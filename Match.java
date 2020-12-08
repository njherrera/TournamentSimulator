/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thesis;

import java.util.ArrayList;

/**
 *
 * @author natha_000
 */
public class Match {
      
    private Player player1, player2, winner, loser, playerBetOn;
    private int matchNumber, roundNumber, bet, payout;
    private double player1WinProbablity;
    private boolean isBetting;
    
    public Match (Player p1, Player p2, int mNum, int rNum){
        this.player1 = p1;
        this.player2 = p2;
        this.matchNumber = mNum;
        this.roundNumber = rNum;
    }
    
    public Match (Player p1, Player p2){
        this.player1 = p1;
        this.player2 = p2;
    }
    
    public Player getPlayer1(){
        return this.player1;
    }
    
    public Player getPlayer2(){
        return this.player2;
    }
    
    public void setPlayer1(Player newPlayer){
        this.player1 = newPlayer;
    }
    
     public void setPlayer2(Player newPlayer){
        this.player2 = newPlayer;
    }
    
     public void setP1WinProbability(double probability){
         this.player1WinProbablity = probability;
     }
     
     public double getP1WinProbability(){
         return this.player1WinProbablity;
     }
     public void generateWinProbability(){
        double ELOdiff = player2.getELOscore() - player1.getELOscore();
        double ELODiffDivided = ELOdiff / 400;
        double finalStep = 1 + Math.pow(10, ELODiffDivided);
        double p1WinLikelihood = 1/finalStep; // p1winlikelihood is likelihood (out of 1) that player 1 wins
        setP1WinProbability(p1WinLikelihood);
     }
     

    public String randomPredictMatch (){
        generateWinProbability();
        int p1WinLikelihoodMultiplied = (int)(getP1WinProbability() * 100); // multiplying p1winlikelihood by 100 to get a whole number for use in RNG
        int newRandomNumber = (int) (Math.random() * 100);
        if (newRandomNumber > (100 - p1WinLikelihoodMultiplied)){
            setMatchWinner(player1);
            setMatchLoser(player2);
            int newELOP1 = (int) (player1.getELOscore() + (20 * (1 - getP1WinProbability()))); // calculating new ELO scores, 1 meaning player won match and 0 meaning player lost
            player1.setELOscore(newELOP1);
            int newELOP2 =(int) (player2.getELOscore() + (20 * (0 - (1 - getP1WinProbability())))); // subtracting p1winlikelihood from 1 yields likelihood that player 2 will win match
            player2.setELOscore(newELOP2);
            if(newELOP2 < 0){
                player2.setELOscore(0);
            }
            return player1.getName();
        } else {
            setMatchWinner(player2);
            setMatchLoser(player1);
            int newELOP1 =(int)  (player1.getELOscore() + (20 * (0 - getP1WinProbability())));
            player1.setELOscore(newELOP1);
            if(newELOP1 < 0){
                player1.setELOscore(0);
            }
            int newELOP2 = (int) (player2.getELOscore() + (20 * (1 - (1 - getP1WinProbability()))));
            player2.setELOscore(newELOP2);
            return player2.getName();
        }

    }
    
    public void updateELO(Player p1, Player p2){
        this.player1 = p1;
        this.player2 = p2;
        generateWinProbability(); // generating win probability so we can use it to adjust ELO ratings of players
        if(this.winner == this.player1){
            int newP1ELO = (int) (player1.getELOscore() + (20 * (1 - getP1WinProbability()))); // same thing as in randomPredictMatch, we calculate new elo score based on expected match result
            player1.setELOscore(newP1ELO);
            int newP2ELO = (int) (player2.getELOscore() + (20 * (0 - (1 - getP1WinProbability()))));
            player2.setELOscore(newP2ELO);
        } else if (this.winner == this.player2){
            int newP1ELO =(int) (player1.getELOscore() + (20 * (0 - getP1WinProbability())));
            player1.setELOscore(newP1ELO);
            int newP2ELO = (int) (player2.getELOscore() + (20 * (1 - (1 - getP1WinProbability()))));
            player2.setELOscore(newP2ELO);
        }
    }

    public void betOnMatch(int betAmount, Player playerChosen){
       isBetting = true;
       playerBetOn = playerChosen;
       bet = betAmount;
    }
    
    public void betOnPlayer(Player playerChosen){
        isBetting = true;
        playerBetOn = playerChosen;
    }
    
    public void placeBetAmount(int betAmount){
        isBetting = true;
        bet = betAmount;
    }

    public int payOutBet(){
        if(playerBetOn == getMatchWinner() && playerBetOn == player1){
            payout = (int)(bet / (getP1WinProbability()));
        } else if (playerBetOn == getMatchWinner() && playerBetOn == player2){
            payout = (int)(bet / (1 - getP1WinProbability()));
        } else {
            payout = 0;
        }
        return payout;
    }
    
    public void setMatchWinner(Player whoWon){
        this.winner = whoWon;
    }
    
    public void setMatchLoser(Player whoLost){
        this.loser = whoLost;
    }
    
    public Player getMatchWinner(){
        return this.winner;
    }
    
    public Player getMatchLoser(){
        return this.loser;
    }
    
    public void setPayout(int pay){
        this.payout = pay;
    }
    
    public int getPayout(){
        return this.payout;
    }
    
    public boolean checkIfBetting(){
       return this.isBetting;
    }
    public ArrayList<Player> getPlayers(){
     ArrayList<Player> playersInMatch = new ArrayList<Player>();
     playersInMatch.add(player1);
     playersInMatch.add(player2);
     return playersInMatch;
    }
    
    public String toString(){
        return player1.getName() + " " + player1.getELOscore() + " vs " + player2.getName() + " " + player2.getELOscore() + "\n";
    }
}
