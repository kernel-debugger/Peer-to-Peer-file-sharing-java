package gui;

import java.awt.Container;
import java.awt.FileDialog;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class EntryPoint implements java.awt.event.ActionListener
{
  JFrame windowContainer;
  JTabbedPane jtp;
  Container homepageContainer;
  JButton sendButton;
  JButton recvButton;
  String filepath = null;
  String filename = null;
  
  JToolBar toolbar;
  javax.swing.JProgressBar statusbar;
  JScrollPane sendpane;
  JScrollPane receivepane;
  javax.swing.table.DefaultTableModel senderModel;
  javax.swing.table.DefaultTableModel receiverModel;
  int sendFileID = 0;
  
  public EntryPoint() {
    sendButton = new JButton("Send file");
    recvButton = new JButton("Receive file");
    toolbar = new JToolBar();
    toolbar.add(sendButton);
    toolbar.add(recvButton);
    toolbar.setSize(600, 38);
    sendButton.addActionListener(this);
    recvButton.addActionListener(this);
    

    JLabel sendlabel = new JLabel("Outgoing Files");
    sendlabel.setBounds(5, 35, 100, 30);
    
    JSeparator seperator0 = new JSeparator();
    seperator0.setBounds(90, 50, 510, 5);
    
    senderModel = new CustomTabelModel("Sending");
    JTable sendertable = new JTable(senderModel);
    sendertable.getColumnModel().getColumn(3).setCellRenderer(new ProgressBarRenderer());
    sendertable.getColumnModel().getColumn(0).setPreferredWidth(200);
    sendertable.getColumnModel().getColumn(1).setPreferredWidth(130);
    sendertable.getColumnModel().getColumn(2).setPreferredWidth(80);
    sendertable.getColumnModel().getColumn(3).setPreferredWidth(180);
    

    sendpane = new JScrollPane(sendertable);
    sendpane.setBounds(1, 60, 600, 250);
    

    JLabel recivelabel = new JLabel("Incomming Files");
    recivelabel.setBounds(5, 315, 100, 30);
    
    JSeparator seperator = new JSeparator();
    seperator.setBounds(100, 329, 500, 5);
    
    receiverModel = new CustomTabelModel("Receiving");
    JTable receivertable = new JTable(receiverModel);
    receivertable.getColumnModel().getColumn(3).setCellRenderer(new ProgressBarRenderer());
    receivertable.getColumnModel().getColumn(0).setPreferredWidth(200);
    receivertable.getColumnModel().getColumn(1).setPreferredWidth(130);
    receivertable.getColumnModel().getColumn(2).setPreferredWidth(80);
    receivertable.getColumnModel().getColumn(3).setPreferredWidth(180);
    
    receivepane = new JScrollPane(receivertable);
    receivepane.setBounds(1, 345, 600, 250);
    



    homepageContainer = new Container();
    homepageContainer.setLayout(null);
    homepageContainer.add(toolbar);
    
    homepageContainer.add(sendlabel);
    homepageContainer.add(seperator0);
    homepageContainer.add(sendpane);
    homepageContainer.add(recivelabel);
    homepageContainer.add(seperator);
    homepageContainer.add(receivepane);
    
    jtp = new JTabbedPane();
    jtp.addTab("Home", homepageContainer);
    
    JLabel lblNewLabepeerpeerFile = new JLabel("Peer2Peer file sharing tool for Local Area Network's");
    lblNewLabepeerpeerFile.setFont(new java.awt.Font("Tahoma", 0, 10));
    lblNewLabepeerpeerFile.setForeground(java.awt.Color.GRAY);
    lblNewLabepeerpeerFile.setBounds(0, 612, 321, 14);
    homepageContainer.add(lblNewLabepeerpeerFile);
    
    JLabel lblArslanMalik = new JLabel("contact: kaleem.mine@gmail.com");
    lblArslanMalik.setForeground(java.awt.Color.GRAY);
    lblArslanMalik.setFont(new java.awt.Font("Consolas", 2, 10));
    lblArslanMalik.setBounds(394, 612, 206, 14);
    homepageContainer.add(lblArslanMalik);
    jtp.addTab("Peer Scanner", new LanScannerGui(this));
    
    windowContainer = new JFrame("P2P File Sharing");
    windowContainer.setDefaultCloseOperation(3);
    windowContainer.setResizable(false);
    windowContainer.setContentPane(jtp);
    windowContainer.setSize(615, 681);
    windowContainer.setVisible(true);
    new dataTransferCore.Listener(receiverModel);
  }
  
  public void actionPerformed(ActionEvent evnt) {
    if (evnt.getSource() == sendButton) {
      sendtoip(javax.swing.JOptionPane.showInputDialog(null, "Enter IP address of reciver "));
    }
    else if (evnt.getSource() == recvButton)
      try {
        javax.swing.JOptionPane.showMessageDialog(null, "Tell the sender to send file at IP " + java.net.Inet4Address.getLocalHost().getHostAddress());
      }
      catch (HeadlessException e) {
        e.printStackTrace();
      }
      catch (java.net.UnknownHostException e) {
        e.printStackTrace();
      }
  }
  
  public void sendtoip(final String recvrip) {
    try {
      Validateip.ip(recvrip);
      FileDialog chooser = new FileDialog(windowContainer);
      chooser.setMultipleMode(true);
      chooser.setVisible(true);
      final File[] files = chooser.getFiles();
      System.out.println("files sellected " + files.length);
      
      if (files.length != 0)
      {














        new Thread()
        {
          public void run()
          {
            for (File tmp : files)
            {
              filepath = tmp.getAbsolutePath();
              filename = tmp.getName();
              new dataTransferCore.SenderThread(recvrip, filepath, filename, senderModel, sendFileID);
              try {
                sleep(2000L);
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
              sendFileID += 1;
            }
          }
        }.start();
      }
    }
    catch (NumberFormatException e) {
      javax.swing.JOptionPane.showMessageDialog(null, "Invalid Ip Address");
    }
  }
  



  public static void main(String[] asdf)
  {
    new EntryPoint();
    System.out.println("main thred quited");
  }
}
