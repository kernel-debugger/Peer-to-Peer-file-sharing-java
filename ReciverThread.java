package dataTransferCore;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import javax.swing.table.DefaultTableModel;


class ReciverThread
  implements Runnable
{
  Socket socky;
  int command;
  int fsize;
  int BUFFER_SIZE = 8192;
  byte[] copier = new byte[BUFFER_SIZE];
  DefaultTableModel tmodel;
  long total;
  long onepercent;
  int checkinc;
  int id;
  String header;
  
  public ReciverThread(Socket socky, DefaultTableModel mb, int id, String head) { this.id = id;
    tmodel = mb;
    this.socky = socky;
    new Thread(this).start();
    header = head;
  }
  
  public void run() {
    try {
      System.out.println(header);
      fsize = Integer.parseInt(header.split("\\?")[1]);
      String fname = header.split("\\?")[0];
      fsize += 1;
      onepercent = (fsize * 1048576 / 100 + 1);
      
      if (!socky.isClosed()) {
        BufferedOutputStream OUTFILEStream = new BufferedOutputStream(new FileOutputStream(fname));
        BufferedInputStream incommingfilestream = new BufferedInputStream(socky.getInputStream());
        socky.getOutputStream().write(78);
        int read;
        while ((read = incommingfilestream.read(copier)) != -1) {
          int read;
          total += read;
          if (checkinc < (int)(total / onepercent)) {
            tmodel.setValueAt(Integer.valueOf(++checkinc), id, 3);
          }
          OUTFILEStream.write(copier, 0, read);
        }
        tmodel.setValueAt(Integer.valueOf(100), id, 3);
        OUTFILEStream.close();
      }
      else
      {
        tmodel.setValueAt(Integer.valueOf(0), id, 3);
        System.out.println("you droped the file");
      }
      
      socky.close();
    }
    catch (IOException e) {
      tmodel.setValueAt(Integer.valueOf(404), id, 3);
      System.out.println(Thread.currentThread().getName() + " reciver theard throwing exception");
    }
  }
}
