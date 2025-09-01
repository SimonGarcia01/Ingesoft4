import Demo.Response;
import Demo.PrinterPrx;

import java.util.Scanner;

import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.ObjectPrx;
import com.zeroc.Ice.Util;

import java.net.InetAddress;
import java.io.IOException;


public class Client {
    public static void main(String[] args) {
        java.util.List<String> extraArgs = new java.util.ArrayList<>();

        try(Communicator communicator = Util.initialize(args, extraArgs)) {
            
            ObjectPrx base = communicator.stringToProxy("SimplePrinter:default -p 9099");

            PrinterPrx service = PrinterPrx.checkedCast(base);
            
            Response response = null;
            
            if(service == null) {
                throw new Error("Invalid proxy");
            }

            Scanner scanner = new Scanner(System.in);

            String username = System.getProperty("user.name");
            String hostname = "unknown";

            try {
                hostname = InetAddress.getLocalHost().getHostName();
            } catch (IOException e) {
                System.err.println("Unable to get hostname: " + e.getMessage());
            }


            while(true){
                System.out.println("Waiting for message...");
                String message = scanner.nextLine();
                response = service.printString(username+":"+hostname+" - "+message);
                System.out.println("Server Response: " + response.value + ", " + response.responseTime);

                if(message.equals("exit")){
                    break;
                }
            }

            scanner.close();
        }
    }
}