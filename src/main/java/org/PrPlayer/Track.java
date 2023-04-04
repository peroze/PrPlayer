package org.PrPlayer;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import org.farng.mp3.MP3File;
import org.farng.mp3.TagException;
import org.farng.mp3.id3.AbstractID3v2;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import org.tritonus.share.sampled.file.TAudioFileFormat;



/**
 * This is the class which contains the song,the player for it (Javazoom) and the songs basic information(title,artist,duration) 
 * @author peroze
 * @version 0.1
 */
public class Track {
     private AbstractID3v2 tag;     // The songs tag
     private  Player p;             // The player(javazoom)
     private multi mult;            // The multi-threading class which starts the song
     private File file;             // The song's file
     private FileInputStream fil;   // The FileInputSystem type of the file (It is needed in javazoom)
     private int total;             // The total frames of the song
     private int current;           // The current frame of the songs (it is used in pause())
     private Thread t;
     /**
      * The constractor which opens the song, creates the tag and creates the player
      * @param file  The song 
      */
     public Track(File file){
            try {
             current=0;
             this.file=file;
             mult=new multi();
             t=new Thread(mult);                         // We initialize the multi-threading class
             fil = new FileInputStream(file);           //In order to create a player we need a FileInputStream variable 
             total=fil.available();
             p=new Player(fil);                         //We create the player 
             MP3File mp3=new MP3File(file);
             if (mp3.hasID3v2Tag()) {
               tag = mp3.getID3v2Tag();         //We create the Tag
             }
             }
             catch (IOException ex) {
                 Logger.getLogger(Track.class.getName()).log(Level.SEVERE, null, ex);
             }
             catch (TagException ex) {
                 Logger.getLogger(Track.class.getName()).log(Level.SEVERE, null, ex);
             } catch (JavaLayerException ex) {
             Logger.getLogger(Track.class.getName()).log(Level.SEVERE, null, ex);
             }
         
        }
        
     /**
      * This method returns the title of the song
      * @return The title of the song
      */   
    public String getTitle(){
       return tag.getSongTitle();
    }
        
    /**
     * This method returns the name of the artist
     * @return The name of the artist
     */    
    public String getSinger(){
        return tag.getLeadArtist();
    }
        
    /**
     * This method return the lenght of the song in seconds
     * @return The lenght of the song in seconds
     */    
    public Double getLenght(){
     try {
             AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(file);
             if (fileFormat instanceof TAudioFileFormat){
                 String key = "duration";
                 Map<?, ?> properties = ((TAudioFileFormat) fileFormat).properties();
                 Long microseconds = (Long) properties.get(key);
                 return ((microseconds/1000000.0));
                 }
         } catch (UnsupportedAudioFileException ex) {
             Logger.getLogger(Track.class.getName()).log(Level.SEVERE, null, ex);
         } catch (IOException ex) {
             Logger.getLogger(Track.class.getName()).log(Level.SEVERE, null, ex);
        }
           return null;
    }
       /**
        * This class is used in order to play the song in a second thread
        */ 
        public class multi implements  Runnable{
            @Override
            public void run(){
               
                    try {
                        p.play();
                        return;
                    } 
                    catch (JavaLayerException ex) {
                        Logger.getLogger(Track.class.getName()).log(Level.SEVERE, null, ex);
                    }
                
            }
        }
        
    public boolean hasEnded(){
        return p.isComplete();
    }
    /**
     * This method starts the song
     */
    public void play(){
        t.start();
    }
    
    
    /**
     * This method pauses the song
     */
    public void pause(){
         try {
             current=fil.available();           //We save the remaining frames in order to complete the song
             current=current+3000;              // Because there is a delay we add some frames in order to stop in a 'better' posistion
             p.close();                         // We close the player and the Thread and then we start it again
             t.interrupt();
             mult=new multi();
             t=new Thread(mult);
             fil = new FileInputStream(file);
             total=fil.available();
             fil.skip(total-current);           // With the deference that the new file contains only the remaining frames
             p=new Player(fil);
             
         } catch (IOException ex) {
             Logger.getLogger(Track.class.getName()).log(Level.SEVERE, null, ex);
         } catch (JavaLayerException ex) {
             Logger.getLogger(Track.class.getName()).log(Level.SEVERE, null, ex);
         }
    }
    
    /**
     * This method is used to stop perminatly the song
     */
    public void stop(){
         try {
             p.close();
             t.interrupt();
             mult=new multi();
             t=new Thread(mult);
             fil = new FileInputStream(file);
             total=fil.available();
             p=new Player(fil);
         } catch (FileNotFoundException ex) {
             Logger.getLogger(Track.class.getName()).log(Level.SEVERE, null, ex);
         } catch (JavaLayerException | IOException ex) {
             Logger.getLogger(Track.class.getName()).log(Level.SEVERE, null, ex);
         }
         
         }
}