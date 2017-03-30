import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * Credit: https://bitbucket.org/mgbckr/code-android-usb-socket-connection
 *
 */
public class ExampleClient {

    private Socket socket;
    private PrintWriter out;
    private Scanner sc;

    /**
     * Initialize connection to the phone
     *
     */
    public void initializeConnection(){
        //Create socket connection
        try{
            socket = new Socket("localhost", 38300);
            out = new PrintWriter(socket.getOutputStream(), true);
            //in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            sc = new Scanner(socket.getInputStream());

            // add a shutdown hook to close the socket if system crashes or exists unexpectedly
            Thread closeSocketOnShutdown = new Thread() {
                public void run() {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.err.println("Socket shut down unexpectedly");
                }
            };

            Runtime.getRuntime().addShutdownHook(closeSocketOnShutdown);

        } catch (UnknownHostException e) {
            System.err.println("Socket connection problem (Unknown host)" + e.getStackTrace());
        } catch (IOException e) {
        	System.out.println(e);
            System.err.println("Could not initialize I/O on socket " + e.getStackTrace());
        }
    }

    public static void main(String[] args) {

        ExampleClient t = new ExampleClient();
        t.initializeConnection();

        while(t.sc.hasNext()) {
        	System.out.println(System.currentTimeMillis() + " / " + t.sc.nextLine());
        }
        System.out.println("DONE READING FROM SOCKET.");
    }
}