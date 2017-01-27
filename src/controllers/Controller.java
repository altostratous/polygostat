package controllers;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import controllers.components.geomentry.Common;
import controllers.components.random.CircleRandomPointGenerator;
import controllers.components.random.UnitSquareRandomNHullGenerator;
import controllers.components.random.UnitSquareRandomPolygonGenerator;
import controllers.components.stat.TwoPolygonsSheet;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

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

    CircleRandomPointGenerator circleRandomPointGenerator = new CircleRandomPointGenerator(1, new Coordinate(0,0));
    @FXML
    public void handleMainPaneClicked(){
        Common.debugPain = mainPain;
        Common.debugPoint(circleRandomPointGenerator.nextCoordinate(), Color.RED);
    }
}
