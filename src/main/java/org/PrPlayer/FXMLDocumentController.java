package org.PrPlayer;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.ImageInput;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * This is the PrPlayer  
 * @version 0.1
 * @author peroze
 */
public class FXMLDocumentController implements Initializable {

    /* To do:
     Add the full Screen option
    */
    
    
     private double xOffset = 0;
     
     private double yOffset = 0; 
     
     private float bar;                    // The number of the bar
    
     private ArrayList<Track> songs;       // The list with every song
     
     private Integer plays;                // This the number of the playing song in the list
     
     private ArrayList<Label> buttons,dur; // These are the Lists which contain the labels with the name and the duration of each song
     
     private multi k;                      // This is the multi-threading which fills the progress bar 
     
     private newsong ne;                   // This is the multi-threading class which automaticly starts a new song when the one playing is finished
     
     private int playstop;                 // This variable=0 if no song is playing and 1 if a song is playing (It is used in order to change the play button with the pause button)
    @FXML
    private ScrollPane Scroll;
    @FXML
     private ProgressBar Bar;              // The progress bar
     @FXML
     private Button play;                  // The play/pause button
     @FXML
     private Button next;
    @FXML
    private Button prev;
    @FXML
     private Label  singer;                // The Label with the playing song's artist   
     @FXML
     private Label musics;                 // The label with the playing song's name
     @FXML
     private Button closebutton;           // The X button
     @FXML
     private GridPane title;               // The panel with the title 
     @FXML 
     private GridPane durat;               // The panel with the duration of its song   
     @FXML
     private Button mini;                  // The minimize button
     @FXML
     private AnchorPane pane;
     
     /**
      * This is the constructor 
      */
     public FXMLDocumentController(){
       Songs music=new Songs();
       buttons=new ArrayList<>();
       dur=new ArrayList<>();
       songs=music.getSongs();
       plays=-1;
       bar=0;
    }
     
     
     
    /**
     * This class is uses multi-threading in order to update the progress bar
     */ 
    public class multi extends Thread{
    float p;
    public multi(){
        p=0;
    }
    public multi(float x){
        p=x;
        Bar.setProgress(p);
    }
    public float getp(){
        return p;
    }
    @Override
    public void run(){
        try {
            playstop=1;
            float p=0;
            double total=songs.get(plays).getLenght();
            double step=(total/100)*1000;
            double count=0;
            while (count<total*1000){
                Thread.sleep((long)step);
                p+=0.01;
                Bar.setProgress(p);
                count=count+step;
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
    }
    /**
     * This method is used in order to minimize the window
     * @param e The press of the button
     */
    @FXML
    public void minimize(ActionEvent e){
      ((Stage)((Button)e.getSource()).getScene().getWindow()).setIconified(true);
    }
    /**
     * 
     * This method closes the program when X is pressed
     * @param e When X is clicked
     */
    @FXML
    public void closewin(ActionEvent e){
        Stage stage = (Stage) ((Button) e.getSource()).getScene().getWindow();
        for (int i=0;i<songs.size();i++){
            songs.get(i).stop();
        }
        stage.close();
        System.exit(0);

    }
    
    /**
     * This method is used in order to change the text of the labels
     */
    public void changeLabel(){
         String k=(String)(songs.get(plays).getSinger());
         singer.setText(k);
         String l=(String)songs.get(plays).getTitle();
         musics.setText(l);
         buttons.get(plays).setFont(Font.font("System",FontWeight.BOLD,18));
         dur.get(plays).setFont(Font.font("System",FontWeight.BOLD,18));
    }
    
    
    /**
     * This method changes the play button with the pause button
     */
    public void pause(){
        Image image=new Image(this.getClass().getClassLoader().getResourceAsStream("org.PrPlayer/Images/if_Pause_2001889_55x55.png"));
        play.setEffect(new ImageInput(image));
        playstop=1;
    }
    
    
    /**
     * This method changes the pause button with the play button
     */
    public void play(){
        Image image=new Image(this.getClass().getClassLoader().getResourceAsStream("org.PrPlayer/Images/if_Play_2001879_55x55.png"));
        play.setEffect(new ImageInput(image));
        playstop=0;
    }
    
    public void reset(){
        buttons.get(plays).setFont(Font.font("System",13));
        dur.get(plays).setFont(Font.font("System",13));
    }
    
     /**
     * This method is used to start playing a song 
     * @param e When the Button is clicked
     */
    @FXML
    public void handleButtonAction(ActionEvent e){
       if (plays==-1){
          reset();
          plays=0;
          changeLabel();
          pause();
          songs.get(plays).play();
          k=new multi(bar);
          k.start();
          ne=new newsong();
          ne.start();
          return;
      }
       else {
           if (playstop==1){
               play();
               songs.get(plays).pause();
               bar=k.getp();
               return;
           }
       }
      pause();
      songs.get(plays).play();
      k.interrupt();
      k=new multi(bar);
      k.start();
      ne.interrupt();
      ne=new newsong();
      ne.start();
    }
    
    /**
     * This method is used to play the previous song
     * @param e  When the button is clicked
     */
     @FXML
     public void handleButtonAction1(ActionEvent e){
       if (plays==-1){
          plays=0;
          pause();
          changeLabel();
          songs.get(plays).play();
          k=new multi();
          k.start();
          ne=new newsong();
          ne.start();
          return;
      }
      else {
          songs.get(plays).stop();
          reset();
          plays+=1; 
       if (plays>=songs.size()){
          plays=0;
       }
      }
      pause();
      changeLabel();
      songs.get(plays).play(); 
      k.interrupt();
      k=new multi();
      k.start();
      ne.interrupt();
      ne=new newsong();
      ne.interrupt();
    }
     
    /**
     * This method is used to play the next song
     * @param e  When the button is clicked
     */ 
    @FXML
    public void handleButtonAction2(ActionEvent e){
      if (plays==-1){
          plays=songs.size()-1;
          pause();
          changeLabel();
          songs.get(plays).play();
          k=new multi();
          k.start();
          ne=new newsong();
          ne.start();
          return;
      }
      else {
          reset();
          songs.get(plays).stop();
          reset();
          plays-=1;
          if (plays==-1){
             System.out.print("mpike");
             plays=songs.size()-1;
          }
      }
      pause();
      changeLabel();
      songs.get(plays).play(); 
      k.interrupt();
      k=new multi();
      k.start();
      ne.interrupt();
      ne=new newsong();
      ne.start();
    }
    
    
    /**
     * This is the listener of the labels.When you press a label it starts playing the song 
     */
    public class listener implements EventHandler<MouseEvent>{

        @Override
        public void handle(MouseEvent event) {
             if (plays!=-1){
                songs.get(plays).stop();
            }
            Label x;
            x=(Label) event.getSource();
            if(plays!=-1){
                ne.interrupt();
                k.interrupt();
                reset();
            }
            plays=Integer.valueOf(x.getId());
            changeLabel();
            pause();
            songs.get(plays).play();
            k=new multi();
            k.start();
            ne=new newsong();
            ne.start();
        }  
    }
    
    
    /**
     * This class is used to auto start the next song when the one playing is finished
     */
    public class newsong extends Thread{
        @Override
        public void run(){
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (songs.get(plays).hasEnded()){
                songs.get(plays).stop();
                reset();
                plays=plays+1;
                if(plays==songs.size()){
                    plays=0;
                }
                changeLabel();
                songs.get(plays).play();
                k.interrupt();
                k=new multi();
                k.start();
                ne.interrupt();
                ne=new newsong();
                ne.start();
            }
        }
    }
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Image image=new Image(this.getClass().getClassLoader().getResourceAsStream("org.PrPlayer/Images/if_Rewind_2001873_55x55.png"));
        Image image2=new Image(this.getClass().getClassLoader().getResourceAsStream("org.PrPlayer/Images/if_Fast_Forward_2001867_55x55.png"));
        Image image3=new Image(this.getClass().getClassLoader().getResourceAsStream("org.PrPlayer/Images/if_Play_2001879_55x55.png"));
        play.setEffect(new ImageInput(image3));
        next.setEffect(new ImageInput(image2));
        prev.setEffect(new ImageInput(image));
        listener list=new listener();
        for (int i=0;i<songs.size();i++){ 
            Label temp=new Label(songs.get(i).getTitle());
            double seconds=songs.get(i).getLenght();
            int min= (int)(seconds/60);
            int second=(int) seconds%60;
            Label temp2=new Label(String.format("%02d",min)+":"+String.format("%02d", second));
            temp.setId(String.valueOf(i));
            temp2.setTextFill( Paint.valueOf("#f83e3e"));
            temp2.setFont(Font.font("System",13));
            temp.setTextFill( Paint.valueOf("#f83e3e"));
            temp.setCursor(Cursor.HAND);
            temp.setFont(Font.font("System",13));
            temp.setOnMouseClicked(list);
            buttons.add(temp);
            dur.add(temp2);
            durat.addRow(i,dur.get(i));
            title.addRow(i,buttons.get(i));
        }
    }    
    
}
