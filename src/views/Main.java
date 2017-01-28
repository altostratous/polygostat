package views;

import controllers.PolygoStat;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("PolygoStat.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("PolygoStat");
        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("/resources/icon.png")));
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                                           @Override
                                           public void handle(WindowEvent event) {
                                               ((PolygoStat)fxmlLoader.getController()).handleOnCloseRequest();
                                               Platform.exit();
                                           }
                                       });
        primaryStage.setScene(new Scene(root));
        //primaryStage.setMaximized(true);
        primaryStage.show();
        ((PolygoStat)fxmlLoader.getController()).initializeComponents();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
