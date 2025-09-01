import Demo.Response;
import Demo.Printer;

import com.zeroc.Ice.Current;

public class PrinterI implements Printer
{
    public Response printString(String s, Current current) {
        System.out.println(s);
        return new Response(0, "Server response: " + s);
    }
}