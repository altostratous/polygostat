package controllers;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;
import controllers.components.geomentry.Common;
import controllers.components.random.*;
import controllers.components.stat.MethodOfMomentsEstimator;
import controllers.components.stat.PolynomialRegression;
import controllers.components.stat.TwoPolygonsSheet;
import views.CommandAnnotation;

import javax.xml.transform.Transformer;
import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
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
    public static void phase_1_task_1_1(PolygoStat controller, String[] args){
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
        controller.savePane("phase_1_task_1/"+ args[1] +n+"Hull.png");
    }

    @CommandAnnotation(help = "params square/circle/triangle, n")
    public static void phase_1_task_1_2(PolygoStat controller, String[] args) throws InterruptedException {
        RandomNHullGenerator randomNHullGenerator = null;
        int n = Integer.parseInt(args[2]);
        switch (args[1]){
            case "square":
                randomNHullGenerator = new UnitSquareRandomNGonGenerator(n);
                break;
            case "circle":
                randomNHullGenerator = new UnitCircleRandomNGonGenerator(n);
                break;
            case "triangle":
                randomNHullGenerator = new UnitTriangleRandomNGonGenerator(n);
                break;
        }
        TwoPolygonsSheet sheet = new TwoPolygonsSheet(randomNHullGenerator);
        sheet.next();
        sheet.drawToPanel(controller.getMainPane());
        controller.savePane("phase_1_task_1/"+ args[1] +n+"Gon.png");
    }

    @CommandAnnotation(help = "param square/circle/triangle, n | Plots distribution for n-Hulls")
    public static void phase_1_task_2(PolygoStat controller, String[] args) throws FileNotFoundException {
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
        int smoothness = 80;
        double[] distro = new double[smoothness];
        for (int i = 0; i < smoothness; i += 1) {
            distro[i] = 0;
        }
        int sampleSize = (int)(1000 / Math.log10(n));
        for (int i = 0; i< sampleSize; i++){
            sheet.next();
            controller.getPrintStream().print(i + " ");
            double area = sheet.getOverLappingArea();
            int key = (int)Math.floor(area / 4 * smoothness);
            distro[key] += 1.0/sampleSize;
        }
        HashMap<Number,Number> chatData = new HashMap<>();
        controller.getPrintStream().println();
        FileOutputStream fileOutputStream = new FileOutputStream("phase_1_task_2/" + args[1] + "_" + args[2] + ".txt");
        PrintStream fileOutputPrintStream = new PrintStream(fileOutputStream);
        fileOutputPrintStream.println("0.0 4.0");
        for (int i = 0; i < smoothness; i++) {
            chatData.put(i * (4.0 / smoothness), distro[i]);
            controller.getPrintStream().println(i * (4.0 / smoothness) + " " + distro[i]);
            fileOutputPrintStream.println(i * (4.0 / smoothness) + " " + distro[i]);
        }
        fileOutputPrintStream.close();
        controller.drawChart(chatData);
        controller.saveChart("phase_1_task_2/" + args[1] + "_" + args[2] + ".png");
    }

    @CommandAnnotation(help = "param square/circle/triangle, n | Performs NGon generation from N = 5 to n. You can" +
            "view distributions and generated polygons as output")
    public static void phase_1_task_3(PolygoStat controller, String[] args) throws FileNotFoundException {
        RandomPointGenerator pointGenerator = null;
        GeometryFactory factory = new GeometryFactory();
        int n = Integer.parseInt(args[2]);
        int its = 1;
        if (args.length > 3)
            its = Integer.parseInt(args[3]);
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
//        Coordinate firstPoint = pointGenerator.nextCoordinate();
//        Polygon convex = factory.createPolygon(factory.createLinearRing(
//                new Coordinate[]{
//                        firstPoint,
//                        pointGenerator.nextCoordinate(),
//                        pointGenerator.nextCoordinate(),
//                        firstPoint
//                }
//        ), null);
//        convex = Common.convexHull(convex);
//        numberOfGons.add(convex.getNumPoints() - 1);
//        areas.add(convex.getArea());
//        int lastn = 0;
        controller.getPrintStream().print("Started generation");
        FileOutputStream fileOutputStream = new FileOutputStream("phase_1_task_3/" + args[1] + "_" + args[2] + ".txt");
        PrintStream fileOutputPrintStream = new PrintStream(fileOutputStream);
        fileOutputPrintStream.println("0.0 " + Math.log(n));
        int lastn = 0;
        for (int i = 0; i < its; i++) {
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
            lastn = 0;

            int counter = 0;
            while (convex.getNumPoints() - 1 < n) {
                Coordinate coordinate = pointGenerator.nextCoordinate();
                while (convex.contains(factory.createPoint(coordinate)))
                    coordinate = pointGenerator.nextCoordinate();
                convex = Common.addPointToConvexHull(convex, coordinate);
                numberOfGons.add(convex.getNumPoints() - 1);
                areas.add(convex.getArea());
                if (lastn < convex.getNumPoints() - 1) {
                    lastn = convex.getNumPoints() - 1;
                    controller.getPrintStream().print(", " + lastn);
//                Common.drawPolygonToPanel(convex, controller.getMainPane(), Color.GREEN, Color.DEEPSKYBLUE);
//                controller.savePane("phase_1_task_3/" + lastn + "gon.jpeg");
                }

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
            if (counts[i] > 0) {
                chartData.put(Math.log((double) i), areaSums[i]);
                fileOutputPrintStream.println( Math.log((double) i) + " " + areaSums[i]);
            }
        }
        controller.drawChart(chartData);
        controller.saveChart("phase_1_task_3/chart_"+args[1]+"_"+args[2]+".png");
    }


    @CommandAnnotation(help = "param square/circle/triangle, n | Performs NGon generation from N = 5 to n. You can" +
            "view distributions and generated polygons as output")
    public static void phase_1_task_4(PolygoStat controller, String[] args){
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
        int lastn = 0;
        controller.getPrintStream().print("Started generation");
        for (int i = 0; i < 1000; i++) {
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
            while (convex.getNumPoints() - 1 < n) {
                Coordinate coordinate = pointGenerator.nextCoordinate();
                while (convex.contains(factory.createPoint(coordinate)))
                    coordinate = pointGenerator.nextCoordinate();
                convex = Common.addPointToConvexHull(convex, coordinate);
                if (lastn < convex.getNumPoints() - 1) {
                    lastn = convex.getNumPoints() - 1;
                    controller.getPrintStream().print(", " + lastn);
//                Common.drawPolygonToPanel(convex, controller.getMainPane(), Color.GREEN, Color.DEEPSKYBLUE);
//                controller.savePane("phase_1_task_3/" + lastn + "gon.jpeg");
                }
                if (lastn == n) {
                    numberOfGons.add(convex.getNumPoints() - 1);
                    areas.add(convex.getArea());
                }
            }
        }


        int smoothness = 80;
        double[] distro = new double[smoothness];
        for (int i = 0; i < smoothness; i += 1) {
            distro[i] = 0;
        }
        for (int i = 0; i < areas.size(); i++){
            int key = (int)Math.floor(areas.get(i) / 4 * smoothness);
            distro[key] += 1.0/areas.size();
        }

        controller.getPrintStream().println();
        for (int i = 0; i < smoothness; i++) {
            controller.getPrintStream().println(i * (4.0 / smoothness) + " " + distro[i]);
        }

        controller.getPrintStream().println("\nGeneration finished for one iteration.");

    }


    @CommandAnnotation(help = "Fits Observations.txt with a degree M polynomial MMSE")
    public static void phase_2_task_1_1(PolygoStat controller, String[] arg) throws Exception {
        File file = new File("phase_2/"+arg[1]+".txt");
        FileInputStream fileInputStream = new FileInputStream(file);
        Scanner scanner = new Scanner(fileInputStream);
        HashMap<Number,Number> chartData = new HashMap<>();
        HashMap<Number,Number> chartDataTrend = new HashMap<>();
        scanner.nextDouble();
        scanner.nextDouble();
        while (scanner.hasNext()){
            chartData.put(scanner.nextDouble(), scanner.nextDouble());
        }
        double[] x = new double[chartData.size()], y = new double[chartData.size()];
        int counter = 0;
        double minKey = 4, maxKey = 0;
        for (Number key :
                chartData.keySet()) {
            if ((double)key > maxKey)
                maxKey = (double) key;
            if ((double)key < minKey)
                minKey = (double) key;

            x[counter] = (double) key;
            y[counter] = (double) chartData.get(key);

            counter++;
        }
         PolynomialRegression regression = new PolynomialRegression(x, y, 3);
//        controller.getPrintStream().println(regression.getPolynomialCoefficients());
        for (double i = minKey; i < maxKey; i+= 0.04) {
            chartDataTrend.put(i, regression.predict(i));
        }

        controller.drawChart(chartDataTrend, chartData);
        controller.saveChart("phase_2/"+arg[1]+"_MMSE.png");
        scanner.close();
    }

    @CommandAnnotation(help = "Generate data.")
    public static void phase_2_task_2(PolygoStat controller, String[] arg) throws Exception {
        File exponentialFile = new File("phase_2_task_1_1/Exponential.txt");
        File uniformFile = new File("phase_2_task_1_1/Uniform.txt");
        FileOutputStream exponentialFileInputStream = new FileOutputStream(exponentialFile);
        FileOutputStream uniformFileInputStream = new FileOutputStream(uniformFile);
        PrintStream exponentialPrintStream = new PrintStream(exponentialFileInputStream);
        PrintStream uniformPrintStream = new PrintStream(uniformFileInputStream);
        exponentialPrintStream.println("0.0 1.0");
        uniformPrintStream.println("0.0 1.0");
        for (double i = 0; i < 1; i+= 0.05) {
            uniformPrintStream.println(i + " " + 1.0);
            exponentialPrintStream.println(i + " " + Math.exp(-i));
        }
        exponentialPrintStream.close();
        uniformPrintStream.close();
    }
    @CommandAnnotation(help = "Fits Observations.txt with a degree M polynomial Method of moments")
    public static void phase_2_task_1_2(PolygoStat controller, String[] arg) throws Exception {
        File file = new File("phase_2/"+arg[1]+".txt");
        FileInputStream fileInputStream = new FileInputStream(file);
        Scanner scanner = new Scanner(fileInputStream);
        HashMap<Number,Number> chartData = new HashMap<>();
        HashMap<Number,Number> chartDataTrend = new HashMap<>();
        double minX = scanner.nextDouble();
        double maxX = scanner.nextDouble();
        while (scanner.hasNext()){
            chartData.put(scanner.nextDouble(), scanner.nextDouble());
        }
        double[] x = new double[chartData.size()], y = new double[chartData.size()];
        int counter = 0;
        double minKey = 4, maxKey = 0;
        for (Number key :
                chartData.keySet()) {
            if ((double)key > maxKey)
                maxKey = (double) key;
            if ((double)key < minKey)
                minKey = (double) key;

            x[counter] = (double) key;
            y[counter] = (double) chartData.get(key);

            counter++;
        }
        MethodOfMomentsEstimator regression = new MethodOfMomentsEstimator(x, y, 4, minX, maxX);
//        controller.getPrintStream().println(regression.getPolynomialCoefficients());
        for (double i = minKey; i < maxKey; i+= 0.04) {
            chartDataTrend.put(i, regression.predict(i));
        }

        controller.drawChart(chartDataTrend, chartData);
        controller.saveChart("phase_2/" +  arg[1] + "_MM.png");
        scanner.close();
    }

    @CommandAnnotation(help = "Fits Observations.txt with a degree M polynomial Method of moments")
    public static void phase_3_inner(PolygoStat controller, String[] arg) throws Exception {
        File file = new File(arg[1]);
        FileInputStream fileInputStream = new FileInputStream(file);
        Scanner scanner = new Scanner(fileInputStream);
        HashMap<Number,Number> chartData = new HashMap<>();
        HashMap<Number,Number> chartDataTemp = new HashMap<>();
        HashMap<Number,Number> chartDataTrend1 = new HashMap<>();
        HashMap<Number,Number> chartDataTrend2 = new HashMap<>();

        ArrayList<Double> allKeys = new ArrayList<>();

        ArrayList<Double> xs = new ArrayList<>();
        ArrayList<Double> ys = new ArrayList<>();
        scanner.nextDouble();
        scanner.nextDouble();
        double e = 0;
        while (scanner.hasNext()){
            double x = scanner.nextDouble();
            double y = scanner.nextDouble();
            xs.add(x);
            ys.add(y);
            if (y > e)
                e = y;
        }
        e /= 10;
        for (int i = xs.size() - 1; i > 0; i--) {
            if (Math.abs(ys.get(i - 1)) <= e && Math.abs( ys.get(i)) <= e){
                ys.remove(i);
                xs.remove(i);
            }else
            {
                break;
            }
        }
        int i = 0;
        for (i = 0; i < xs.size() - 1; i++) {
            if (Math.abs(ys.get(i + 1)) > e || Math.abs( ys.get(i)) > e){
                break;
            }
        }

        for(; i  < xs.size(); i++){
            chartDataTemp.put(xs.get(i), ys.get(i));
            allKeys.add(xs.get(i));
        }

        ArrayList<Double> keyList = new ArrayList<>();

        int istep = allKeys.size() / 5;
        istep = Math.max(istep, 1);
        for (i = 0; i < allKeys.size(); i+=istep) {
            keyList.add(allKeys.get(i));
        }

        for (Number key :
                chartDataTemp.keySet()) {
            if (keyList.contains(key))
                chartData.put(key, chartDataTemp.get(key));
        }

        double[] x = new double[chartData.size()], y = new double[chartData.size()];
        int counter = 0;
        double minKey = 4, maxKey = 0;
        for (Number key :
                chartData.keySet()) {
            if ((double)key > maxKey)
                maxKey = (double) key;
            if ((double)key < minKey)
                minKey = (double) key;

            x[counter] = (double) key;
            y[counter] = (double) chartData.get(key);

            counter++;
        }
        MethodOfMomentsEstimator regression = new MethodOfMomentsEstimator(x, y,3, minKey, maxKey);
        PolynomialRegression mmseRegression = new PolynomialRegression(x,y,3);

        controller.getPrintStream().println(mmseRegression);
        double step = (maxKey - minKey) / 100;
        for (double j = minKey; j < maxKey; j+= step) {
            chartDataTrend1.put(j, regression.predict(j));
            chartDataTrend2.put(j, mmseRegression.predict(j));
        }
        HashMap<String, HashMap<Number, Number>> data = new HashMap<>();
        data.put("1. Main", chartDataTemp);
        data.put("2. Scatter", chartData);
        data.put("3. MMSE", chartDataTrend2);
        controller.drawCharts(data);
        Thread.sleep(500);
        controller.saveChart("phase_3/"+file.getName()+".MMSE.png");
        data.remove("3. MMSE");
        data.put("3. MLM", chartDataTrend1);
        controller.drawCharts(data);
        Thread.sleep(500);
        controller.saveChart("phase_3/"+file.getName()+".MLM.png");
        data.remove("3. MLM");
        scanner.close();
    }

    @CommandAnnotation(help = "Estimates all data.")
    public static void phase_3(PolygoStat controller, String[] args) throws Exception {
        File task_2 = new File("phase_1_task_2");
        File task_3  = new File("phase_1_task_3");
        File[] files_2 = task_2.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if (name.endsWith(".txt"))
                    return true;
                else
                    return false;
            }
        });
        File[] files_3 = task_3.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if (name.endsWith(".txt"))
                    return true;
                else
                    return false;
            }
        });
        File[] files = new File[files_2.length + files_3.length];
        System.arraycopy(files_2, 0, files, 0, files_2.length);
        System.arraycopy(files_3, 0, files, files_2.length, files_3.length);
        for (File file :
                files) {
            phase_3_inner(controller, new String[]{"phase_3_inner", file.getParentFile().getName() + "/" + file.getName()});
            Thread.sleep(500);
        }
    }

    @CommandAnnotation(help = "Cancels operation.")
    public static void cancel(PolygoStat controller, String[] args){
        controller.getPrintStream().println("\nCancelled operation.");
    }


}
