package Xecho;

import java.net.*;
import java.io.*;
import java.util.*;
import java.lang.reflect.*;

public class XechoServer {
  // TODO: kill dead connections
  public static void main(String[] args) throws IOException {
    if (args.length != 1) {
      System.err.println("Usage: java XechoServer <port number>");
      System.exit(1);
    }

    int portNumber = Integer.parseInt(args[0]);
    Scanner in = new Scanner(System.in);
    System.out.print("Which class: ");
    String mod = in.next();
    in.close();

    run(portNumber, mod);
  }

  private static void run(int portNumber, String mod) {
    ClassLoader classLoader = XechoServer.class.getClassLoader();
    ServerSocket serverSocket = null;

    // Make server
    try {
      serverSocket = new ServerSocket(portNumber);
    } catch (IOException e) {
      System.err.println("Exception while listening to port " + portNumber);
    }

    // Load module
    Class modclass = null;
    try {
      modclass = classLoader.loadClass(mod);
    } catch (Exception e) {
      e.printStackTrace();
    }

    // Accept connections
    for (;;) {
      try {
        Socket clientSocket = serverSocket.accept();
        XechoMini mini = new XechoMini(clientSocket, (XechoInterface)modclass.newInstance());
        mini.start();
      } catch (IOException e) {
        System.out.println("Error creating clientSocket");
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
