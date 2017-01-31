# polygostat
This project is done for Engineering Probability and Statistics course at Sharif University of Technology. This project also consists of a good kit to perform polygonal statistic researches and good GUI to report results.

You can see the project definition in this file: [EPS-Fall2016-Project.pdf](https://github.com/altostratous/polygostat/blob/master/EPS-Fall2016-Project.pdf)

## Features
 1- Good GUI to report charts and Polygons.
 2- Method of moment and Minimum Mean Squared Error function estimation methods.
 3- Good random object generators; such as CircleRandomPoint generator which generates coordinates within a circle uniformly.

## Installation
It requires JavaFX, jts-1.10 and Jama-1.0.3 libraries. It is written on Java 1.8.

## Usage
You will find many project specific things in this repository such as phase_1_task_1 etc. But you can find reusable things in [components](https://github.com/altostratous/polygostat/blob/master/src/controllers/components).
Of course you can use the GUI as simple as the fallowing:

In src/controllers/Commands just add a function like this to add a command to GUI:
    
    @CommandAnnotation(help = "[Help for the command goes here.]")
    public static void cancel(PolygoStat controller, String[] args){
        // printing sth in the GUI console 
        controller.getPrintStream().println("\nCancelled operation.");
        // for showing charts and polygons or save them as image please see how other commands are implemented
    }
