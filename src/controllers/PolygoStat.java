package controllers;

import com.vividsolutions.jts.geom.Coordinate;
import controllers.components.geomentry.Common;
import controllers.components.random.CircleRandomPointGenerator;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PolygoStat {
    @FXML
    private Pane mainPane;

    Timer timer;
    public PolygoStat(){
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
        Common.debugPain = mainPane;
        Common.debugPoint(circleRandomPointGenerator.nextCoordinate(), Color.RED);
//        FXImaging.saveImage(mainPane, "images/"+System.currentTimeMillis());
    }

    public void handleOnCloseRequest() {
        timer.stop();
    }

}
