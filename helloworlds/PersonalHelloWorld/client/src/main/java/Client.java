import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.ObjectPrx;
import com.zeroc.Ice.Util;
import Demo.PrinterPrx;

public class Client {
    public static void main(String[] args)
    {
        try(Communicator communicator = Util.initialize(args))
        {
            ObjectPrx base = communicator.stringToProxy("SimplePrinter:default -p 10000");
            PrinterPrx printer = PrinterPrx.checkedCast(base);
            if(printer == null) {
                throw new Error("Invalid proxy");
            }
            printer.printString("Hello World!");
        }
    }
}