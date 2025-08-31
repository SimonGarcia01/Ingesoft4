import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.Object;
import com.zeroc.Ice.Util;

public class Server {
    public static void main(String[] args) {
        try(Communicator communicator = Util.initialize(args)) {
            ObjectAdapter adapter = communicator.createObjectAdapterWithEndpoints("SimplePrinterAdapter", 
                            "default -h 192.168.131.101 -p 10000"); //default is TCP port 10000 
                            // SimplePrinterAdapter is the name of the adapter that will be used to create the object adapter
                            // No ip address means it will listen on all interfaces localhost
            Object object = new PrinterI();
            adapter.add(object, Util.stringToIdentity("SimplePrinter")); 
            adapter.activate();
            communicator.waitForShutdown();
        }
    }
}