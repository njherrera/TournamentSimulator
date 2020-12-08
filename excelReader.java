/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thesis;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author natha_000
 */
public class excelReader {
    
    private ArrayList<Player> tourneyRoster = new ArrayList<Player>();
    private String filePath;
    private XSSFWorkbook workbook;
    
    
    public excelReader(ArrayList<Player> startingRoster) throws IOException{
        this.tourneyRoster = startingRoster;
        filePath = "/Users/natha_000/Documents/2020AustralianOpenTop32.xlsx";
        workbook = new XSSFWorkbook(filePath);
    }
    
    public excelReader(ArrayList<Player> startingRoster, String path) throws IOException{
        this.tourneyRoster = startingRoster;
        this.filePath = path;
        workbook = new XSSFWorkbook(path);
    }
    
    public ArrayList<Player> readTop32(){
        DataFormatter formatter = new DataFormatter();
        XSSFSheet sheet = workbook.getSheetAt(0); // get first sheet of excel file
        int rowStart = 0; //set starting row to 0
        int rowEnd = sheet.getLastRowNum(); // set ending row to last row containing data
        System.out.println("rowEnd: " + rowEnd);
        for (int i = 0; i < rowEnd + 1; i++) { // interate through rows of sheet
            String nameValue = formatter.formatCellValue(sheet.getRow(i).getCell(1)); // get full name of player storted at 2nd index and convert it to a string
            Player newPlayer = new Player(((int)sheet.getRow(i).getCell(4).getNumericCellValue()), nameValue, " "); // grab ELO from 5th column, use name from before
            tourneyRoster.add(newPlayer); // add player to roster of players supplied in constructor
        }
        return this.tourneyRoster;
    }
    
    public ArrayList<Player> readMatchHistory(){
        DataFormatter formatter = new DataFormatter();
        XSSFSheet sheet = workbook.getSheetAt(0);
        int rowEnd = sheet.getLastRowNum();
        Player playerCellOne = null;
        Player playerCellTwo = null;
        int indexOfPlayerOne = 0;
        int indexOfPlayerTwo = 0;
        for (int i = 0; i < rowEnd + 1; i++) { // looping through each match in spreadsheet
            String nameValueOne = formatter.formatCellValue(sheet.getRow(i).getCell(0)); //converting value in first column (player one's name) to string
            String nameValueTwo = formatter.formatCellValue(sheet.getRow(i).getCell(1)); // converting value in second column (player two's name) to string
            boolean containsPlayerOne = false; // flags we use to mark when player already exists in tournament roster
            boolean containsPlayerTwo = false;
            for (Player player : tourneyRoster){
                if (player.getFirstName().equals(nameValueOne)){ // if player's name matches value at index 0 of row, mark that the tourney roster contains the player
                    containsPlayerOne = true;
                    playerCellOne = player;
                }
            }
                if(containsPlayerOne == false){ // if player at index 0 of current row wasn't found in tourney roster, make a new player
                    playerCellOne = new Player(1500, nameValueOne, "");
                    tourneyRoster.add(playerCellOne);
                }
            
            for (Player player: tourneyRoster){ // do same thing with player at index 1 of current row
                if(player.getFirstName().equals(nameValueTwo)){
                    containsPlayerTwo = true;
                    playerCellTwo = player;
                }
            }
            if(containsPlayerTwo == false){
                playerCellTwo = new Player (1500, nameValueTwo, "");
                tourneyRoster.add(playerCellTwo);
            }
            // now that we've established the players in the match, it's time to grab the winner of the match and update ELO scores
            String winnerName = formatter.formatCellValue(sheet.getRow(i).getCell(2)); // grab string value at winner spot
            Match newMatch = new Match(playerCellOne, playerCellTwo);
            for(Player player : newMatch.getPlayers()){
                if (player.getFirstName().equals(winnerName)){ // since player's full name is listed in one field on spreadsheet, full name is first name (could use split to split string up by space in middle)
                    newMatch.setMatchWinner(player);
                }
            }
            for (Player player : tourneyRoster) { // simple for loop that goes through tournament roster to find player matching player one's name
                if (player.getFirstName().equals(playerCellOne.getFirstName())){
                    indexOfPlayerOne = tourneyRoster.indexOf(player); // once we've found player matching name, assign index of that player to indexOfPlayerOne
                }
            }
            for(Player player : tourneyRoster){
                if(player.getFirstName().equals(playerCellTwo.getFirstName())){
                    indexOfPlayerTwo = tourneyRoster.indexOf(player);
                }
            }
            newMatch.updateELO(playerCellOne, playerCellTwo);
            tourneyRoster.set(indexOfPlayerOne, playerCellOne); //switch out old player data with new player data
            tourneyRoster.set(indexOfPlayerTwo, playerCellTwo);
        }
        
        return this.tourneyRoster;
    }
    
    public void setTourneyRoster(ArrayList<Player> newRoster){
        this.tourneyRoster = newRoster;
    }
        
}
