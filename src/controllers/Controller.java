package controllers;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import controllers.components.random.PolygonRandomPointGenerator;
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
    @FXML
    public void handleMainPaneClicked(){
//        TwoPolygonsSheet sheet = new TwoPolygonsSheet(new UnitSquareRandomNHullGenerator(20));
//        sheet.next();
//        sheet.drawToPanel(mainPain);
        GeometryFactory geometryFactory = new GeometryFactory();
        Coordinate[] coordinates = new Coordinate[]{
                new Coordinate(0, 0),
                new Coordinate(1, 0),
                new Coordinate(1, 1),
                new Coordinate(0.5, 1),
                new Coordinate(0.5, 0.5),
                new Coordinate(0, 0.5),
                new Coordinate(0, 0)
        };
        PolygonRandomPointGenerator randomPointGenerator = new PolygonRandomPointGenerator(geometryFactory.createPolygon(geometryFactory.createLinearRing(coordinates), null));
        randomPointGenerator.drawToPanel(mainPain);
    }
}
