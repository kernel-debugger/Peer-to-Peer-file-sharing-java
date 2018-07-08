package dataTransferCore;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;



public class SenderThread
  implements Runnable
{
  private int RECEIVER_PORT = 1337;
  private int BUFFER_SIZE = 8192;
  byte[] bt = new byte[BUFFER_SIZE];
  Thread t;
  private String recvrip;
  private String filepath;
  private String filename;
  DefaultTableModel tmodel; private float fsize; private float total = 0.0F;
  private int id;
  int onepercent;
  int read;
  int checkinc = 0;
  Socket soc;
  
  public SenderThread(String a, String b, String c, DefaultTableModel jpb, int id) {
    this.id = id;
    recvrip = a;
    filepath = b;
    filename = c;
    tmodel = jpb;
    t = new Thread(this);
    t.start();
  }
  
  public void run()
  {
    try {
      soc = new Socket(recvrip, RECEIVER_PORT);
      fsize = ((float)new File(filepath).length());
      onepercent = ((int)fsize / 100);
      BufferedInputStream fileStream = new BufferedInputStream(new FileInputStream(filepath));
      InputStream is = soc.getInputStream();
      OutputStream os = soc.getOutputStream();
      int ttt = (int)(fsize / 1048576.0F);
      tmodel.addRow(new String[] { filename, recvrip, new File(filepath).length() / 1048576L + " MB", "0" });
      os.write(32);
      new DataOutputStream(os).writeUTF(filename + "?" + ttt);
      
      int test = is.read();
      
      if (test == 78)
      {
        while ((this.read = fileStream.read(bt)) != -1) {
          total += read;
          if (checkinc < (int)(total / onepercent))
          {
            tmodel.setValueAt(Integer.valueOf(++checkinc), id, 3);
          }
          os.write(bt, 0, read);
        }
        
        fileStream.close();
        tmodel.setValueAt(Integer.valueOf(100), id, 3);
        soc.close();

      }
      else
      {
        tmodel.setValueAt(Integer.valueOf(0), id, 3);
        JOptionPane.showMessageDialog(null, "Other party denied to recive!");
      }
    }
    catch (IOException e) {
      tmodel.setValueAt(Integer.valueOf(404), id, 3);
      JOptionPane.showMessageDialog(null, "Selected Host unreacable at the moment Or its not running the service");
      e.printStackTrace();
    }
  }
}
