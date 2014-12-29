package Xecho;

import java.net.*;
import java.io.*;

public class XechoMini extends Thread {
  private Socket clientSocket;
  private PrintWriter out;
  private BufferedReader in;
  private XechoInterface xi;

  public XechoMini(Socket s, XechoInterface x) {
    super("XechoMini");
    clientSocket = s;
    try {
      out = new PrintWriter(clientSocket.getOutputStream(), true);
      in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
      xi = x;
    } catch (IOException e) {
      System.err.println("Error occured making PrintWriter, BufferedReader");
    }
  }

  public void run() {
    try {
      func();
    } catch (IOException e) {
      System.err.println("Error occured running Xecho module");
    }
  }

  public void func() throws IOException {
    String inputLine;
    String sa = clientSocket.getRemoteSocketAddress().toString();
    while (!xi.isFinished() && (inputLine = in.readLine()) != null) {
      System.out.println("received " + sa);
      out.println(xi.send(inputLine));
    }

    clientSocket.close();
    System.out.println("socket " + sa + " has closed");
  }
}
