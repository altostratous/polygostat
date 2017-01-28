package controllers;

import controllers.components.random.*;
import controllers.components.stat.TwoPolygonsSheet;
import views.CommandAnnotation;

import java.lang.reflect.Method;

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
}
