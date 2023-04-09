package eventloop;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Task implements ITask{
    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    public Task(Socket client) {
        this.client = client;
        try {
            this.in = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
            this.out = new PrintWriter(this.client.getOutputStream());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void execute() {
        try {
            String line;
            while ((line = in.readLine()) != null) {
              if (line.equalsIgnoreCase("ping")) {
                this.out.write("+PONG\r\n");
                this.out.flush();
              }
            }
            this.client.close();
        } catch (IOException e) {
             e.printStackTrace();
        } finally {
            try {
              if (this.client != null) {
                this.client.close();
              }
            } catch (IOException e) {
                System.out.println("IOException: " + e.getMessage());
            }
        }
    }
}
