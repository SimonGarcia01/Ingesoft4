import Demo.Response;
import Demo.PrinterPrx;

import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.ObjectPrx;
import com.zeroc.Ice.Util;

public class Client {
    public static void main(String[] args) {
        java.util.List<String> extraArgs = new java.util.ArrayList<>();

        try(Communicator communicator = Util.initialize(args,"config.client",extraArgs))
        {
            ObjectPrx base = communicator.stringToProxy("Printer.Proxy");

            PrinterPrx service = PrinterPrx.checkedCast(base);
            
            Response response = null;
            
            if(service == null) {
                throw new Error("Invalid proxy");
            }

            response = service.printString("Hello World from a remote client!");

            System.out.println("Respuesta del server: " + response.value + ", " + response.responseTime);
        }
    }
}