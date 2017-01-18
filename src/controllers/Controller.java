package controllers;

import controllers.components.random.UnitSquareRandomNHullGenerator;
import controllers.components.random.UnitSquareRandomPolygonGenerator;
import controllers.components.stat.TwoPolygonsSheet;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller {
    @FXML
    private Pane mainPain;
    Timer timer;
    public Controller(){
        timer = new Timer(1, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Platform.runLater(new Runnable() {
                                      @Override
                                      public void run() {
                                          handleMainPaneClicked();
                                      }
                                  });
            }
        });
        timer.start();
    }
    @FXML
    public void handleMainPaneClicked(){
        TwoPolygonsSheet sheet = new TwoPolygonsSheet(new UnitSquareRandomNHullGenerator(20));
        sheet.next();
        sheet.drawToPanel(mainPain);
    }
}
