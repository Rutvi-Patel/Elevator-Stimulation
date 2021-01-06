package Logging;

import src.Simulation;

public class StandardOutLogger extends Logger {
    public StandardOutLogger(Simulation sim){
        super(sim);

    }
    public void logString(String message){
        System.out.println(message);
    }
}
