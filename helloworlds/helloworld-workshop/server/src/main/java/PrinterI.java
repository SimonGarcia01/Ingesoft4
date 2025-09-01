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
import java.util.stream.Collectors;

public class PrinterI implements Printer {
    public Response printString(String s, Current current) {

        long startTime = System.currentTimeMillis();

        String[] sArray = s.split(" - ");

        String clientId = sArray[0];

        String message = sArray[1];

        System.out.println(clientId + " sent: " + message);

        if (message.startsWith("listifs")) {

            StringBuilder result = new StringBuilder();

            result.append(clientId).append(" requested interfaces:\n");

            try {
                Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
                while (interfaces.hasMoreElements()) {
                    NetworkInterface ni = interfaces.nextElement();
                    result.append(ni.getName()).append(" - ").append(ni.getDisplayName()).append("\n");
                }
            } catch (SocketException e) {
                result.append("Error retrieving interfaces: ").append(e.getMessage());
            }

            System.out.println(result.toString());

            return new Response(0, result.toString());

        } else if (message.startsWith("listports")) {

            String[] parts = message.split(" ");
            if (parts.length < 2) {
                return new Response(0, "Invalid format. Use: listports <IPv4>");
            }

            String ip = parts[1];
            StringBuilder result = new StringBuilder();
            result.append(clientId).append(" requested open ports for ").append(ip).append(":\n");

            for (int port = 1; port <= 50; port++) {
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

            System.out.println(output);
            return new Response(0, output);
        } else if (message.startsWith("!")) {
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
        System.out.println(output); // Print to server console

        return new Response(0, output);
    }

        int num = tryParseInt(message);
        if (num > 0) {
            List<Integer> fibComplete = new ArrayList<>();
            System.out.println("The first " + num + " Fibonacci numbers:");
            for (int i = 0; i < num; i++) {
                fibComplete.add(fibonacci(i));
            }

            System.out.println(fibComplete.toString());

            List<Integer> fibPrimes = fibComplete.stream()
                .filter(PrinterI::isPrime)
                .collect(Collectors.toList());

            return new Response(1, "Prime Fibonacci numbers: " + fibPrimes.toString());
        }

            System.out.println(s);
            return new Response(0, s);
    }

    private static int tryParseInt(String input) {
        try {
            return (int) Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private int fibonacci(int n) {

        // n cant be negative
        if(n < 2) {
            return n;
        }
        else {
            return fibonacci(n-1) + fibonacci(n-2);
        
        }
    }

    private static boolean isPrime(int num) {
        if (num <= 1) return false;

        if (num == 2) return true;

        if (num % 2 == 0) return false;

        for (int i = 3; i <= Math.sqrt(num); i += 2) {
            if (num % i == 0) return false;
        }

        return true;
    }
 
}