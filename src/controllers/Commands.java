package controllers;

import views.CommandAnnotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Created by HP PC on 1/28/2017.
 */
public class Commands {
    @CommandAnnotation(help = "Shows available commands and their help.")
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
}
