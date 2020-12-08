/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

// functionality: limited, mostly just housing ELO scores right now

package thesis;

/**
 *
 * @author natha_000
 */
public class Player implements Comparable<Player> {
    
    private int ELOscore;
    private int originalELOscore;
    private String firstName, lastName;
    private char handedness;
    private boolean isBetOn;
   // other characteristics: age, former top 10 status, court, MATCH HISTORY
    
    public Player (int Escore, String first, String last){
        this.ELOscore = Escore;
        this.originalELOscore = Escore;
        this.firstName = first;
        this.lastName = last;
    }
    
    public Player(int EScore, String first, String last, char hand){
        this.ELOscore = EScore;
        this.originalELOscore = EScore;
        this.firstName = first;
        this.lastName = last;
        this.handedness = hand;
    }
    
    public Player (int EScore){
        this.ELOscore = EScore;
    }
    
    public void setELOscore (int newELO){
        this.ELOscore = newELO;
    }
    
    public int getELOscore(){
    return ELOscore;
    }
    
    public void setELOToOriginal(){
        this.ELOscore = originalELOscore;
    }
    
    public void setName (String first, String last){
        this.firstName = first;
        this.lastName = last;
    }
    
    public String getName(){
      return "" + this.firstName + " " + this.lastName;
    }
    
    public String getFirstName(){
        return "" + this.firstName;
    }
    
    public String getLastName(){
        return "" + this.lastName;
    }
    
    public void setHandedness (char h){
        if (h == 'r'){
            this.handedness = 'r';
        } if (h == 'l'){
            this.handedness = 'l';
        } else System.out.println("Input 'l' for left, 'r' for right");
    }
    
    public char getHandedness(){
    return this.handedness;
}
    
    public void setBetOnStatus(boolean status){
        this.isBetOn = status;
    }
    
    public boolean getBetOnStatus(){
        return isBetOn;
    }

    @Override
    public int compareTo(Player otherPlayer) {
        return Integer.compare(otherPlayer.ELOscore, this.ELOscore);
    }
    
    @Override
    public boolean equals(Object o){
        if(o instanceof Player){
            Player p = (Player) o;
            return this.firstName.equals(p.getFirstName()) && this.lastName.equals(p.getLastName());
        } else {
            return false;
        }
    }
    
    @Override
    public int hashCode(){
        int id = this.ELOscore;
        id += this.firstName.length();
        id += this.lastName.length();
        return id;
    }
    
    @Override
    public String toString(){
        return this.firstName + " " + this.lastName + ", ELO Score: " + this.ELOscore +  "\n";
    }
   
}
