//Socket: Class for network communication.
//BufferedReader, IOException, InputStreamReader, PrintWriter: Classes for handling input/output streams and exceptions.
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
//various import statements for classes to be used.
class Client_socket {
    public static void main(String[] args) {
//        System.out.println("Client started..");
        // Try-with-resources statement to ensure the Socket resource is closed automatically.
        try(Socket soc = new Socket("127.0.0.1",2212);){
            // BufferedReader to read text from the input stream of the socket.
            BufferedReader in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
            // PrintWriter to write text to the output stream of the socket, with auto-flush enabled.
            PrintWriter out = new PrintWriter(soc.getOutputStream(),true);

            // Call the runClient method to handle the client-server communication.
            runClient(out, in);


        }catch(IOException e){ // Print any IOException that occurs during the socket connection or communication.
            System.out.println(e.getMessage());
        }

    }

    public static void runClient(PrintWriter out, BufferedReader in) throws IOException {
        // Declare variables for request and response strings.
        String request;
        String response;
        Scanner scanner = new Scanner(System.in);
        // Display the menu with available commands
        showMenu();
        outer:do{
            // Prompt the user for input.
            System.out.print("MATH BOT>>");
            // Read the user's input (request) from the console.
            request = scanner.nextLine();
            // above: Send the request to the server via the PrintWriter.
            out.println(request);//respose is from server
            //above:  If the request is "done", exit the loop.
            if(request.equalsIgnoreCase("done")){
                break;
            }

            // Inner loop to read and print the server's responses.
            do {
                // Read a line of response from the server.
                response = in.readLine();
                // Print the server's response to the console.
                System.out.println(response);//respose is from server
                // If the server's response is "logging out...", break the outer loop to exit.
                if(response.equals("logging out...")){
                    break outer;//when ""
                }

            } while (!response.isEmpty());
            // Continue reading until an empty response is received.


        }while(!request.equalsIgnoreCase("done"));
        // Continue until the user enters "done".

        scanner.close();  // Close the scanner.
        // Recursive call to runClient to restart the client (this might cause a stack overflow if not handled properly).
        runClient(out,in);
    }

    public static void showMenu() {
        // Define a multiline string with instructions for the user.
        String instructionSet1 = """
                                            MATHEMATICS ONLINE SYSTEM
                Commands to use:
                >register username firstname lastname email password DateOfBirth school_reg_no imageFile.png -> for registering.

                >login pupil / rep(representative) username password -> for logging into the system.

                >done -> to exit the system
            
                """;
        // Print the instruction set to the console.
        System.out.println(instructionSet1);

    }
}
