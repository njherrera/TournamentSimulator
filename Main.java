/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thesis;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 *
 * @author natha_000
 */
public class Main {
    public static void main(String[] args) throws IOException{
        
        GUI myGUI = new GUI();
        myGUI.setVisible(true);

        ArrayList<Player> testList = new ArrayList<Player>();
        Player one = new Player(1600, "Burt", "Hutchingson");
        Player two = new Player(3000, "Hutch", "Burtmeister");
        Player three = new Player(2450, "Butch", "Hurtingson");
        Player four = new Player(2500, "Starsky", "Hutch");
        Player five = new Player(2550, "Butch", "Cassidy");
        Player six = new Player(2400, "Sean", "Plott");
        Player seven = new Player(1775, "Tasteless", "Artosis");
        Player eight = new Player(1500, "Rick", "James");
        testList.add(one);
        testList.add(two);
        testList.add(three);
        testList.add(four);
        testList.add(five);
        testList.add(six);
        testList.add(seven);
        testList.add(eight);
        Collections.sort(testList);
        Tournament testTournament = new Tournament(testList);
        double tourneyEntrants = testList.size();
        double nRounds = Math.ceil(Math.log(tourneyEntrants)/Math.log(2));
        System.out.println(nRounds);
    }

}
