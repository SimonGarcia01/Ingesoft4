//import java.io.*;

import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.Util;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.Object;

import java.util.List;
import java.util.ArrayList;

public class Server {

    // Attributes
    private static int numOfRequests = 0;
    private static int numResolvedRequests = 0;
    private static long timeToResponse = 0;

    public static void main(String[] args) {

        List<String> extraArgs = new ArrayList<String>();

        try(Communicator communicator = Util.initialize(args,"config.server", extraArgs))
        {
            if(!extraArgs.isEmpty())
            {
                System.err.println("too many arguments");
                for(String v:extraArgs){
                    System.out.println(v);
                }
            }
            
            ObjectAdapter adapter = communicator.createObjectAdapter("Printer");

            Object object = new PrinterI();

            adapter.add(object, Util.stringToIdentity("SimplePrinter"));

            adapter.activate();

            communicator.waitForShutdown();
        }
    }

    // -------------------- AUXILIARY METHODS -----------------------
    // For calculate QA attributes of Permorfance.

    // Getters and Setters
    public static int getNumOfRequests() {
        return numOfRequests;
    }

    public static void setNumOfRequests(int newNumOfRequests) {
        Server.numOfRequests = newNumOfRequests;
    }

    public static int getNumResolvedRequests() {
        return numResolvedRequests;
    }

    public static void setNumResolvedRequests(int newNumOfResolvedRequests) {
        Server.numResolvedRequests = newNumOfResolvedRequests;
    }

    public static long getTimeToResponse() {
        return timeToResponse;
    }

    public static void setTimeToResponse(long newResponseTime) {
        Server.timeToResponse = newResponseTime;
    }

}