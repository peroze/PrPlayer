package org.PrPlayer;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


/**
 * This is the main class
 * @author peroze
 * @version 0.1
 */
public class PrPlayer extends Application {
    double xOffset,yOffset;
    @Override
    public void start(Stage stage) throws Exception {
        //Image image=new Image(getClass().getResourceAsStream("Images/headphones-icon-250px.png"));
        //stage.getIcons().add(image);
        stage.setTitle("PrPlayer");
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("org.PrPlayer/FXMLDocument.fxml"));
          root.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });
        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            }
        });
        Scene scene1 = new Scene(root);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene1);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
