
public class Client {
    public static void main(String[] args) {
        try (com.zeroc.Ice.Communicator communicator = com.zeroc.Ice.Util.initialize(args, "client.cfg")) {

            Demo.PrinterPrx chatManagerPrx = Demo.PrinterPrx
                    .checkedCast(communicator.propertyToProxy("Printer.Proxy"));

            try {

                chatManagerPrx.printString("Hello World");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
