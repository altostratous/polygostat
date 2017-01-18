package controllers;

import controllers.components.random.RandomConvexPolygonGenerator;
import controllers.components.random.UnitSquareRandomConvexPolygonGenerator;
import controllers.components.stat.TwoPolygonsSheet;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

public class Controller {
    @FXML
    private Pane mainPain;
    public Controller(){
    }
    @FXML
    public void handleMainPaneClicked(){
        TwoPolygonsSheet sheet = new TwoPolygonsSheet(new UnitSquareRandomConvexPolygonGenerator(50));
        sheet.next();
        sheet.drawToPanel(mainPain);
    }
}
