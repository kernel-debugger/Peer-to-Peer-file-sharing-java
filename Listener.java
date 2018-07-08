package dataTransferCore;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class Listener
  implements Runnable
{
  int LISTEN_PORT = 1337;
  Thread th;
  DefaultTableModel tmodel;
  int id = 0;
  Socket validatorsoc = null;
  boolean flag = false;
  int command;
  
  public Listener(DefaultTableModel mod) { tmodel = mod;
    th = new Thread(this, "receiving-thread");
    th.start();
  }
  
  public void run() {
    System.out.println("listening started");
    ServerSocket srvsoc = null;
    try {
      srvsoc = new ServerSocket(LISTEN_PORT, 50);
      for (;;) {
        validatorsoc = srvsoc.accept();
        try {
          command = validatorsoc.getInputStream().read();
          if (command == 32) {
            DataInputStream dis = new DataInputStream(validatorsoc.getInputStream());
            String header = dis.readUTF();
            int a = JOptionPane.showConfirmDialog(null, "Host " + validatorsoc.getInetAddress().getHostName() + " Is  Sending File {" + header.split("\\?")[0] + "} [near " + (Integer.parseInt(header.split("\\?")[1]) + 1) + "MB ]. Wanna receive?");
            
            if (a == 0) {
              validatorsoc.getOutputStream().write(78);
              tmodel.addRow(new String[] { header.split("\\?")[0], validatorsoc.getInetAddress().getHostAddress(), "< " + (Integer.parseInt(header.split("\\?")[1]) + 1) + "MB", "0" });
              new ReciverThread(validatorsoc, tmodel, id++, header);
            }
            else {
              validatorsoc.close();
            }
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    } catch (IOException e) {
      if (e.getMessage().indexOf("JVM_Bind") != -1) {
        JOptionPane.showMessageDialog(null, "Unable to listen on port 1337. you won't be able to receive files");
        flag = true;
      }
      e.printStackTrace();
    }
  }
}
