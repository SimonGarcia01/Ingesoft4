import Demo.Response;
import Demo.Printer;

import com.zeroc.Ice.Current;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;


public class PrinterI implements Printer {
    public Response printString(String s, Current current) {

        // We star the timers for calculate time the response time
        long startTime = System.currentTimeMillis();
        long endTime = 0;
        long responseTime = 0;
        double throughput = 0;
        double unprocessRequestsRate = 0;

        // We increment the number of requests
        Server.setNumOfRequests(Server.getNumOfRequests() + 1);

        String[] sArray = s.split(" - ");

        // Control for client messages to server
        String clientId = sArray[0];

        String message = sArray[1];

        System.out.println("\n------------------------------------------------");
        System.out.println(clientId + " sent: " + message);

        // 2.b.)
        if (message.startsWith("listifs")) {

            StringBuilder result = new StringBuilder();
            result.append(clientId).append(" requested interfaces:\n");

            Object[] out = listInterfaces(result);
            String serverMessage = (String) out[0];
            Response responseToClient = (Response) out[1];

            // It prints in Server
            System.out.println(serverMessage);
            
            endTime = System.currentTimeMillis();

            // We increment the number of resolved requests
            Server.setNumResolvedRequests(Server.getNumResolvedRequests() + 1);

            // We set some attributes of QA performance
            responseTime = calculateResponseTime(startTime, endTime);
            throughput = calculateThroughput();
            unprocessRequestsRate = calculateUnprocessRequestsRate();

            responseToClient.responseTime = responseTime;
            responseToClient.throughput = throughput;
            responseToClient.unprocessRequestsRate = unprocessRequestsRate;

            // We accumm the total time of processing in Server
            Server.setTimeToResponse(Server.getTimeToResponse() + responseTime);

            // It's necessary that we return to Client the response, return not print.
            // We put -1 because it counts the Waiting message.. such an extra request
            System.out.println("Requests received: " + (Server.getNumOfRequests() - 1));
            System.out.println("Solved requests: " + Server.getNumResolvedRequests());
            return responseToClient;

        // 2.c.)
        } else if (message.startsWith("listports")) {

            Object[] out = listPorts(message, clientId);
            String serverMessage = (String) out[0];
            Response responseToClient = (Response) out[1];

            // It prints in Server
            System.out.println(serverMessage);

            endTime = System.currentTimeMillis();

            // We increment the number of resolved requests
            Server.setNumResolvedRequests(Server.getNumResolvedRequests() + 1);

            // We set some attributes of QA performance
            responseTime = calculateResponseTime(startTime, endTime);
            throughput = calculateThroughput();
            unprocessRequestsRate = calculateUnprocessRequestsRate();

            responseToClient.responseTime = responseTime;
            responseToClient.throughput = throughput;
            responseToClient.unprocessRequestsRate = unprocessRequestsRate;

            // We accumm the total time of processing in Server
            Server.setTimeToResponse(Server.getTimeToResponse() + responseTime);

            // It's necessary that we return to Client the response, return not print.
            System.out.println("Requests received: " + Server.getNumOfRequests());
            System.out.println("Solved requests: " + Server.getNumResolvedRequests());

            // It's necessary that we return to Client the response, return not print.
            return responseToClient;

        // 2.d.)
        } else if (message.startsWith("!")) {

            Object[] out = executeCommandInOS(message, clientId);
            String serverMessage = (String) out[0];
            Response responseToClient = (Response) out[1];

            // It prints in Server console
            System.out.println(serverMessage);

            endTime = System.currentTimeMillis();

            // We increment the number of resolved requests
            Server.setNumResolvedRequests(Server.getNumResolvedRequests() + 1);

            // We set some attributes of QA performance
            responseTime = calculateResponseTime(startTime, endTime);
            throughput = calculateThroughput();
            unprocessRequestsRate = calculateUnprocessRequestsRate();

            responseToClient.responseTime = responseTime;
            responseToClient.throughput = throughput;
            responseToClient.unprocessRequestsRate = unprocessRequestsRate;

            // We accumm the total time of processing in Server
            Server.setTimeToResponse(Server.getTimeToResponse() + responseTime);

            // It's necessary that we return to Client the response, return not print.
            System.out.println("Requests received: " + Server.getNumOfRequests());
            System.out.println("Solved requests: " + Server.getNumResolvedRequests());

            // It's necessary that we return to Client the response, return not print.
            return responseToClient;

    }
        // 2.a.)
        int num = tryParseInt(message);  // --> With tryParseInt, it returns false if the thing inside the parameter are not a number, for example, has a letter.
        // Condition
        if (num > 0) {

            List<Integer> fibComplete = new ArrayList<>();
            System.out.println("The first " + num + " Fibonacci numbers:");
            for (int i = 0; i < num; i++) {
                fibComplete.add(fibonacci(i));
            }

            System.out.println(fibComplete);

            List<Integer> fibPrimes = fibComplete.stream()
                .filter(PrinterI::isPrime)
                .toList();

            return new Response(1, "Prime Fibonacci numbers: " + fibPrimes, 0, 0);

        }

            System.out.println(s);
            endTime = System.currentTimeMillis();

            // We increment the number of resolved requests
            Server.setNumResolvedRequests(Server.getNumResolvedRequests() + 1);

            // We set some attributes of QA performance
            responseTime = calculateResponseTime(startTime, endTime);
            throughput = calculateThroughput();
            unprocessRequestsRate = calculateUnprocessRequestsRate();

            // We accumm the total time of processing in Server
            Server.setTimeToResponse(Server.getTimeToResponse() + responseTime);

            // It's necessary that we return to Client the response, return not print.
            System.out.println("Requests received: " + Server.getNumOfRequests());
            System.out.println("Solved requests: " + Server.getNumResolvedRequests());

            return new Response(responseTime, s, throughput, unprocessRequestsRate);
    }

    // -----------------------------------  AUXILIARY METHODS ------------------------------------

    private static int tryParseInt(String input) {
        try {
            return (int) Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    // Fibonacci method
    private int fibonacci(int n) {

        // base case, if n is 1, in fibonacci we put the same one because is not sum with anything yet.
        if(n < 2) {
            return n;
        }
        else {
            // if exist at least 2 elements, we can sum them, the last two
            return fibonacci(n-1) + fibonacci(n-2);
        
        }
    }

    // This method is for identity what element of Fibonacci serie are primes
    private static boolean isPrime(int num) {
        if (num <= 1) return false;

        if (num == 2) return true;

        if (num % 2 == 0) return false;

        for (int i = 3; i <= Math.sqrt(num); i += 2) {
            if (num % i == 0) return false;
        }

        return true;
    }

    // List interfaces method
    private static Object[] listInterfaces(StringBuilder result) {

        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

            while (interfaces.hasMoreElements()) {
                NetworkInterface ni = interfaces.nextElement();
                result.append(ni.getName()).append(" - ").append(ni.getDisplayName()).append("\n");
            }

        } catch (SocketException e) {
            result.append("Error retrieving interfaces: ").append(e.getMessage());
        }

        // Returns
        Object[] output = {result.toString(), new Response(0, result.toString(), 0, 0)};

        // It's sended to Client
        return output;

    }

    // Listports method
    private static Object[] listPorts(String message, String clientId) {

        StringBuilder result = new StringBuilder();
        Object[] outputToReturn;

        String[] parts = message.split(" ");
        if (parts.length < 2) {
            // This is for follow the format listports 127.0.0.1 --> for example for localhost
            outputToReturn = new Object[]{result.toString(), new Response(0, "Invalid format. Use: listports <IPv4> (e.g. listports 127.0.0.1)", 0, 0)};
            return outputToReturn;
        }

        String ip = parts[1];
        result.append(clientId).append(" requested open ports for ").append(ip).append(":\n");

        // This duration might be considerable
        for (int port = 1; port <= 9100; port++) {
            try (Socket socket = new Socket()) {
                socket.connect(new InetSocketAddress(ip, port), 50); // 50ms timeout
                result.append("Port ").append(port).append(" is OPEN\n");
            } catch (IOException ignored) {
                // Skip closed ports silently
            }
        }

        String output = result.toString();

        if (output.endsWith(":\n")) {
            output += "No open ports found.";
        }

        outputToReturn = new Object[] {output, new Response(0, output, 0, 0)};
        return outputToReturn;

    }

    // Execute command in O.S (Operative System)
    private static Object[] executeCommandInOS(String message, String clientId) {

        String command = message.substring(1).trim(); // Remove "!" and trim

        StringBuilder result = new StringBuilder();
        result.append(clientId).append(" executed command: ").append(command).append("\n");

        try {
            Process process = Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", command});
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream())
            );
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line).append("\n");
            }
            reader.close();

            // Optionally read stderr too
            BufferedReader errorReader = new BufferedReader(
                    new InputStreamReader(process.getErrorStream())
            );
            while ((line = errorReader.readLine()) != null) {
                result.append("ERR: ").append(line).append("\n");
            }
            errorReader.close();
        } catch (Exception e) {
            result.append("Error executing command: ").append(e.getMessage()).append("\n");
        }

        String output = result.toString();
        Object[] outputToReturn = {output, new Response(0, output, 0, 0)};

        return outputToReturn;

    }

    // ------------------- AUXILIARY METHODS FOR PERFORMANCE -----------------------
    private static long calculateResponseTime(long startTime, long endTime) {
        return endTime - startTime;
    }

    private static double calculateThroughput() {

        double throughput = 0;

        if(Server.getTimeToResponse() == 0) {
            return throughput;
        }
        else {
            // The 1000.0 es convert millis in seconds
            throughput = (Server.getNumOfRequests()-1) / ((double) Server.getTimeToResponse() / 1000.0);
            return throughput;
        }

    }

    // This method calculate the rate of unprocessed requests as percentage
    private static double calculateUnprocessRequestsRate() {

        double unprocessRate = (((double) Server.getNumOfRequests() - Server.getNumResolvedRequests()) / (double) Server.getNumOfRequests()) * 100;
		return unprocessRate;

    }

}