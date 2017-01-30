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
import views.FXImaging;

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
                                e.printStackTrace();
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
    public void drawCharts(HashMap<String,HashMap<Number,Number>> chartDatas){
        final NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("");
        final NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("");
        LineChart<Number,Number> lineChart = new LineChart<Number, Number>(xAxis, yAxis);
        for (String label :
                chartDatas.keySet()) {
            HashMap<Number, Number> chartData = chartDatas.get(label);XYChart.Series<Number, Number> series = new XYChart.Series<>();
            for (Number key :
                    chartData.keySet()) {
                series.getData().add(new XYChart.Data<Number, Number>(key, chartData.get(key)));
            }
            series.setName(label);
            lineChart.getData().add(series);
        }

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                lineChartContainer.getChildren().clear();
                lineChartContainer.getChildren().add(lineChart);
                AnchorPane.setTopAnchor(lineChart,0.);
                AnchorPane.setBottomAnchor(lineChart,0.);
                AnchorPane.setLeftAnchor(lineChart,0.);
                AnchorPane.setRightAnchor(lineChart,0.);
            }
        });
    }
    public void drawChart(HashMap<Number, Number> chartData, HashMap<Number,Number> chartDataTrend) {

        final NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("");
        final NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("");

        LineChart<Number,Number> lineChart = new LineChart<Number, Number>(xAxis, yAxis);
        if (chartData != null) {
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            for (Number key :
                    chartData.keySet()) {
                series.getData().add(new XYChart.Data<Number, Number>(key, chartData.get(key)));
            }
            series.setName("Scatter");
            lineChart.getData().add(series);
        }
        XYChart.Series<Number, Number> trend = new XYChart.Series<>();
        for (Number key :
                chartDataTrend.keySet()) {
            trend.getData().add(new XYChart.Data<Number, Number>(key, chartDataTrend.get(key)));
        }
        trend.setName("Line");
        lineChart.getData().add(trend);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                lineChartContainer.getChildren().clear();
                lineChartContainer.getChildren().add(lineChart);
                AnchorPane.setTopAnchor(lineChart,0.);
                AnchorPane.setBottomAnchor(lineChart,0.);
                AnchorPane.setLeftAnchor(lineChart,0.);
                AnchorPane.setRightAnchor(lineChart,0.);
            }
        });
    }
    public void drawChart(HashMap<Number, Number> chartData) {
        drawChart(null, chartData);
    }

    public void saveChart(String s) {
        Platform.runLater(new Runnable() {
                              @Override
                              public void run() {
                                  FXImaging.saveImage(lineChartContainer, s);
                              }
                          });
    }

    public void savePane(String s) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                FXImaging.saveImage(mainPane, s);
            }
        });
    }
}
