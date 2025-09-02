module Demo {
    class Response {
        long responseTime;
        string value;
        double throughput;
        double unprocessRequestsRate;
    }

    interface Printer{
        Response printString(string s);
    }
}