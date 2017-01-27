package controllers;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.index.ArrayListVisitor;
import controllers.components.geomentry.Common;
import controllers.components.random.CircleRandomPointGenerator;
import controllers.components.random.UnitSquareRandomNHullGenerator;
import controllers.components.random.UnitSquareRandomPolygonGenerator;
import controllers.components.stat.TwoPolygonsSheet;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXML;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import views.FXImaging;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Observable;

public class Controller {
    @FXML
    private Pane mainPain;
    Timer timer;
    public Controller(){
//        timer = new Timer(1, new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                Platform.runLater(new Runnable() {
//                                      @Override
//                                      public void run() {
//                                          handleMainPaneClicked();
//                                      }
//                                  });
//            }
//        });
//        timer.start();
    }

    CircleRandomPointGenerator circleRandomPointGenerator = new CircleRandomPointGenerator(1, new Coordinate(0,0));
    @FXML
    public void handleMainPaneClicked(){
        Common.debugPain = mainPain;
        Common.debugPoint(circleRandomPointGenerator.nextCoordinate(), Color.RED);
        FXImaging.saveImage(mainPain);
    }
}
