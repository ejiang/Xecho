package Xecho;

import java.net.*;
import java.io.*;

public class Fibo implements XechoInterface {
  private int[] memo = new int[30];
  private boolean finished = false;

  public Fibo() {
    for (int i = 0; i < memo.length; ++i)
      memo[i] = -1;
    memo[0] = 1; // ??
    memo[1] = 1;
    memo[2] = 1;
  }

  public String send(String mesg) {
    if (mesg.equals("exit")) {
      finished = true;
      return "EXIT\n";
    }
    try {
      int n = Integer.parseInt(mesg);
      fibo(n);
      return make(n);
    } catch (Exception e) {
      return "Bad number\n";
    }
  }

  public boolean isFinished() {
    return finished;
  }

  private String make(int n) {
    StringBuilder sb = new StringBuilder();
    for (int i = 1; i <= n; ++i) {
      sb.append("memo[" + i + "] = " + memo[i]);
      sb.append("\n");
    }
    return sb.toString();
  }

  private int fibo(int n) {
    if (memo[n] > 0)
      return memo[n];
    memo[n] = fibo(n - 1) + fibo(n - 2);
    return memo[n];
  }
}
