package Xecho;

import java.io.*;
import java.net.*;
import java.util.stream.*;
import java.util.*;

public class XechoClient {
  private Socket serverSocket;
  private PrintWriter out;
  private BufferedReader in;
  private boolean closed = true;

  public XechoClient(String hostName, int portNumber) {
    try {
      serverSocket = new Socket(hostName, portNumber);
      out =
        new PrintWriter(serverSocket.getOutputStream(), true);
      in =
        new BufferedReader(
          new InputStreamReader(serverSocket.getInputStream()));
      closed = false;
      System.out.println("Connection successful to " + hostName);
    } catch (Exception e) {
      System.err.println("Couldn't establish connection with " + hostName);
    }
  }

  public List<String> sendRequest(String userInput) {
    out.println(userInput);
    List<String> lines = new ArrayList<String>();
    try {
      lines.add(in.readLine());
      while (in.ready())
        lines.add(in.readLine());
      lines.remove(lines.size() - 1);
    } catch (IOException e) {
      System.err.println("Error in outputting lines");
    }
    if (lines.get(0).equals("EXIT"))
      close();
    return lines;
  }

  public void close() {
    try {
      out.println("exit");
      serverSocket.close();
      closed = true;
    } catch (IOException e) {
      System.err.println("cannot close socket");
    }
  }

  public boolean isClosed() {
    return closed;
  }

  public static void main(String[] args) throws IOException {
    if (args.length != 2) {
      System.err.println(
        "Usage: java XechoClient <host name> <port number>");
      System.exit(1);
    }

    String hostName = args[0];
    int portNumber = Integer.parseInt(args[1]);

    XechoClient xc = new XechoClient(hostName, portNumber);
    if (xc.isClosed())
      System.exit(1);

    BufferedReader stdIn =
      new BufferedReader(
        new InputStreamReader(System.in));

    String userInput;
    System.out.print("% ");

    Runtime.getRuntime().addShutdownHook(new Thread() {
      public void run() {
        xc.close();
      }
    });

    while ((userInput = stdIn.readLine()) != null) {
      List<String> lines = xc.sendRequest(userInput);
      if (xc.isClosed())
        break;
      for (String l : lines)
        System.out.println(l);
      System.out.print("% ");
    }
  }
}
