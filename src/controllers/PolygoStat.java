package controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

public class PolygoStat {
    @FXML
    private TextField commandTextField;
    @FXML
    private TextArea logTextArea;
    @FXML
    private AnchorPane lineChartContainer;
    @FXML
    private Pane mainPane;

    public PolygoStat(){
    }

    public PrintStream getPrintStream() {
        return printStream;
    }

    private PrintStream printStream;

    public void initializeComponents(){
        printStream = new PrintStream(new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                Platform.runLater(new Runnable() {
                                      @Override
                                      public void run() {
                                          logTextArea.appendText(new String(new char[]{(char) b}));
                                      }
                                  });
            }
        });
    }

    public void handleOnCloseRequest() {

    }

    @FXML
    public void handleCommandTextFieldKeyPressed(KeyEvent event){
        if (event.getCode() == KeyCode.ENTER){
            String command = commandTextField.getText();
            runCommand(command);
            commandTextField.setText("");
        }
    }

    Thread worker;
    private void runCommand(String command) {
        ClassLoader classLoader = getClass().getClassLoader();
        try {
            Class<Commands> commands = (Class<Commands>) classLoader.loadClass("controllers.Commands");
            String[] tokens = command.split(" ");
            if (tokens.length < 1)
                return;
            String methodName = tokens[0];
            Method[] methods = commands.getMethods();
            for (Method method :
                    methods) {
                if (method.getName().equals(methodName)){
                    if (worker != null){
                        if (worker.isAlive()) {
                            if (methodName.equals("cancel")) {
                                try {
                                    worker.stop();
                                }catch (Exception ex){}
                            }
                        }
                    }
                    worker = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                method.invoke(null, PolygoStat.this, tokens);
                            } catch (IllegalAccessException e) {

                            } catch (InvocationTargetException e) {

                            }
                        }
                    });
                    worker.start();
                    return;
                }
            }
            printStream.println("No such command.");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Pane getMainPane() {
        return mainPane;
    }

    public void cancelCommand() {
        if (worker != null)
            if (worker.isAlive())
                try {
                    worker.stop();
                }catch (Exception ex){}
    }

    public void drawChart(HashMap<Number, Number> chartData) {
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        LineChart<Number,Number> lineChart = new LineChart<Number, Number>(xAxis, yAxis);
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        for (Number key :
                chartData.keySet()) {
            series.getData().add(new XYChart.Data<Number, Number>(key, chartData.get(key)));
        }
        lineChart.getData().add(series);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                lineChartContainer.getChildren().clear();
                lineChartContainer.getChildren().add(lineChart);
            }
        });
    }
}
