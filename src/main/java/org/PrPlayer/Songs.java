package org.PrPlayer;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;
import java.util.ArrayList;
/**
 *This class finds all the mp3 files in MyMusic folder and saves it in a list
 * @author peroze
 * @version 0.1
 */
public class Songs {
    private ArrayList<Track> songs;       // The list with the songs
    /**
     * This is the constructor of the Class
     */
    public Songs(){
        File directory;
        String username = System.getProperty("user.name");//The username of the windows account 
        directory=new File("C:\\Users\\"+username+"\\Music");//In this variable we save all the files in music folder
        songs=new ArrayList<>();
        File[] fList = directory.listFiles();
        for (File file:fList){
            if(file.getName().endsWith(".mp3")){            // We saperate the .mp3 files 
                Track temp=new Track(file);                 // For each mp3 we find we create a new Track(see the class Track)
                songs.add(temp);                            //And we add it in the songs list
            }
        }
    }
    
    /**
     * This method return the list with the songs 
     * @return The list with the songs
     */
    public ArrayList<Track> getSongs(){
       return songs;
     }
    
    /**
     * This method reuses the code of the constractor in order to update the list when a new file is added
     */
    public void UpdateSongs(){
        File directory;
        directory=new File("C:\\Users\\peroze\\Music");
        songs=new ArrayList<>();
        File[] fList = directory.listFiles();
        for (File file:fList){
            if(file.getName().endsWith(".mp3")){
                songs.add(new Track(file));
            }
        }
    }
    }

