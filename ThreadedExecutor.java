package LanScanner;

import java.net.InetAddress;
import java.net.Socket;

public class ThreadedExecutor extends Thread
{
  Generator gen;
  int port;
  int tout;
  
  public ThreadedExecutor(Generator gen, int p, int t)
  {
    port = p;
    tout = t;
    this.gen = gen;
  }
  
  public void run() {
    String ipadr = null;
    while (((ipadr = gen.next()) != null) && (!isInterrupted())) {
      try {
        Socket soc = new Socket();
        soc.connect(new java.net.InetSocketAddress(ipadr, port), tout);
        gen.scannerGui.appendHost(ipadr, InetAddress.getByName(ipadr).getHostName());
        soc.close();
      }
      catch (java.io.IOException localIOException) {}
    }
  }
}
