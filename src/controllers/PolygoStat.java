package controllers;

import com.vividsolutions.jts.geom.Coordinate;
import controllers.components.geomentry.Common;
import controllers.components.random.CircleRandomPointGenerator;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PolygoStat {
    @FXML
    private Pane mainPane;
    @FXML
    private TextField commandTextField;
    @FXML
    private TextArea logTextArea;
    @FXML
    private LineChart<Double,Double> lineChart;
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
                logTextArea.appendText(new String(new char[]{(char)b}));
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
                    try {
                        method.invoke(null, this, tokens);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    return;
                }
            }
            printStream.println("No such command.");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
