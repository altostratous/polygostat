package controllers;

import com.sun.corba.se.impl.naming.cosnaming.NamingUtils;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.index.quadtree.DoubleBits;
import controllers.components.geomentry.Common;
import controllers.components.random.*;
import controllers.components.stat.TwoPolygonsSheet;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.paint.Color;
import views.CommandAnnotation;
import views.FXImaging;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by HP PC on 1/28/2017.
 */
public class Commands {
    @CommandAnnotation(help = "\t\t\tShows available commands and their help.")
    public static void help(PolygoStat controller, String[] args) throws ClassNotFoundException {
        ClassLoader classLoader = Commands.class.getClassLoader();
        Class<Commands> commands = (Class<Commands>) classLoader.loadClass("controllers.Commands");
        Method[] methods = commands.getMethods();
        controller.getPrintStream().println("Available methods:");
        for (Method method :
                methods) {
            CommandAnnotation annotation = method.getAnnotation(CommandAnnotation.class);
            if (annotation != null)
                controller.getPrintStream().println("\t" + method.getName() + ":\t\t\t\t\t\t" + annotation.help());
        }
        controller.getPrintStream().println("Each method puts output in a folder with a name same as the method name.");
    }

    @CommandAnnotation(help = "params square/circle/triangle, n")
    public static void phase_1_task_1(PolygoStat controller, String[] args){
        RandomNHullGenerator randomNHullGenerator = null;
        int n = Integer.parseInt(args[2]);
        switch (args[1]){
            case "square":
                randomNHullGenerator = new UnitSquareRandomNHullGenerator(n);
                break;
            case "circle":
                randomNHullGenerator = new UnitCircleRandomNHullGenerator(n);
                break;
            case "triangle":
                randomNHullGenerator = new UnitTriangleRandomNHullGenerator(n);
                break;
        }
        TwoPolygonsSheet sheet = new TwoPolygonsSheet(randomNHullGenerator);
        sheet.next();
        sheet.drawToPanel(controller.getMainPane());
    }
    @CommandAnnotation(help = "param square/circle/triangle, n | Plots distribution for n-Hulls")
    public static void phase_1_task_2(PolygoStat controller, String[] args){
        RandomNHullGenerator randomNHullGenerator = null;
        int n = Integer.parseInt(args[2]);
        switch (args[1]){
            case "square":
                randomNHullGenerator = new UnitSquareRandomNHullGenerator(n);
                break;
            case "circle":
                randomNHullGenerator = new UnitCircleRandomNHullGenerator(n);
                break;
            case "triangle":
                randomNHullGenerator = new UnitTriangleRandomNHullGenerator(n);
                break;
        }
        TwoPolygonsSheet sheet = new TwoPolygonsSheet(randomNHullGenerator);
        sheet.next();
        double[] distro = new double[100];
        for (int i = 0; i < 100; i += 1) {
            distro[i] = 0;
        }
        for (int i = 0; i< 10000; i++){
            sheet.next();
            double area = sheet.getOverLappingArea();
            int key = (int)Math.floor(area / 4 * 100);
            distro[key] += 0.0001;
        }
        HashMap<Number,Number> chatData = new HashMap<>();
        for (int i = 0; i < 100; i++) {
            chatData.put(i * 0.04, distro[i]);
        }
        controller.drawChart(chatData);
//        controller.saveChart("phase_1_task_3/chart_"+args[1]+"_"+args[2]+".png");
    }
    @CommandAnnotation(help = "param square/circle/triangle, n | Performs NGon generation from N = 5 to n. You can" +
            "view distributions and generated polygons as output")
    public static void phase_1_task_3(PolygoStat controller, String[] args){
        RandomPointGenerator pointGenerator = null;
        GeometryFactory factory = new GeometryFactory();
        int n = Integer.parseInt(args[2]);
        switch (args[1]) {
            case "square":
                pointGenerator = new ConvexPolygonRandomPointGenerator(factory.createPolygon(factory.createLinearRing(
                        new Coordinate[]{
                                new Coordinate(0, 0),
                                new Coordinate(0, 1),
                                new Coordinate(1, 1),
                                new Coordinate(1, 0),
                                new Coordinate(0, 0)
                        }
                ), null));
                break;
            case "circle":
                pointGenerator = new CircleRandomPointGenerator(1, new Coordinate(0, 0));
                break;
            case "triangle":
                pointGenerator = new ConvexPolygonRandomPointGenerator(factory.createPolygon(factory.createLinearRing(
                        new Coordinate[]{
                                new Coordinate(-0.5, 0),
                                new Coordinate(+0.5, 0),
                                new Coordinate(0, Math.signum(0.75)),
                                new Coordinate(-0.5, 0)
                        }
                ), null));
                break;
        }
        ArrayList<Integer> numberOfGons = new ArrayList<>();
        ArrayList<Double> areas = new ArrayList<>();
        Coordinate firstPoint = pointGenerator.nextCoordinate();
        Polygon convex = factory.createPolygon(factory.createLinearRing(
                new Coordinate[]{
                        firstPoint,
                        pointGenerator.nextCoordinate(),
                        pointGenerator.nextCoordinate(),
                        firstPoint
                }
        ), null);
        convex = Common.convexHull(convex);
        numberOfGons.add(convex.getNumPoints() - 1);
        areas.add(convex.getArea());
        int lastn = 0;
        controller.getPrintStream().print("Started generation");
        while (convex.getNumPoints() - 1 < n){
            Coordinate coordinate = pointGenerator.nextCoordinate();
            while (convex.contains(factory.createPoint(coordinate)))
                coordinate = pointGenerator.nextCoordinate();
            convex = Common.addPointToConvexHull(convex, coordinate);
            numberOfGons.add(convex.getNumPoints() - 1);
            areas.add(convex.getArea());
            if (lastn < convex.getNumPoints() - 1){
                lastn = convex.getNumPoints() - 1;
                controller.getPrintStream().print(", " + lastn);
//                Common.drawPolygonToPanel(convex, controller.getMainPane(), Color.GREEN, Color.DEEPSKYBLUE);
//                controller.savePane("phase_1_task_3/" + lastn + "gon.jpeg");
            }
        }
        controller.getPrintStream().println("\nGeneration finished for one iteration.");
        double[] areaSums =new double[lastn + 1];
        int[] counts = new int[lastn + 1];
        for (int i = 0; i < lastn+1; i++) {
            areaSums[i] = counts[i] = 0;
        }
        for (int i = 0; i < numberOfGons.size(); i++){
            areaSums[numberOfGons.get(i)] += areas.get(i);
            counts[numberOfGons.get(i)]++;
        }
        for (int i = 0; i < lastn+1; i++) {
            areaSums[i] /= counts[i];
        }
        HashMap<Number,Number> chartData = new HashMap<>();
        for (int i = 0; i < lastn +1; i++) {
            controller.getPrintStream().println(i+ " " + areaSums[i]);
            if (counts[i] > 0)
                chartData.put(Math.log((double)i), areaSums[i]);
        }
        controller.drawChart(chartData);
//        controller.saveChart("phase_1_task_3/chart_"+args[1]+"_"+args[2]+".png");
    }

    @CommandAnnotation(help = "Fits Observations.txt with a degree M polynomial")
    public static void phase_2_task_1_1(PolygoStat controller, String[] arg) throws FileNotFoundException {
        File file = new File("phase_2_task_1_1/Observations.txt");
        FileInputStream fileInputStream = new FileInputStream(file);
        Scanner scanner = new Scanner(fileInputStream);
        HashMap<Number,Number> chartData = new HashMap<>();

        while (scanner.hasNext()){
            chartData.put(scanner.nextDouble(), scanner.nextDouble());
        }
        controller.drawChart(chartData);
        scanner.close();
    }

    @CommandAnnotation(help = "Cancels operation.")
    public static void cancel(PolygoStat controller, String[] args){
        controller.getPrintStream().println("\nCancelled operation.");
    }


}
