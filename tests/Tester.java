package Xecho;

import java.util.*;

class Test extends Thread {
	private XechoClient xc;
	private static int counter = 0;
	private int myid;

	public Test() {
		myid = counter++;
		xc = new XechoClient("127.0.0.1", 6277);
	}

	public void run() {
		xc.sendRequest("29");
		xc.sendRequest("exit");
		xc.close();
	}
}

public class Tester {
	public static void main(String[] args) {
		// Makes sure it can handle the loads
		// Thinking about doing 1000 simultaneous connections,
		// all sending a message of "29" one after each other

		List<Thread> a = new ArrayList<>();
		for (int i = 0; i < 1000; ++i) {
			a.add(new Test());
		}
		for (Thread t : a) {
			t.start();
		}
	}
}
