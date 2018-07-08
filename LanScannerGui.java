package gui;

import LanScanner.Generator;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.table.DefaultTableModel;

public class LanScannerGui extends java.awt.Container implements java.awt.event.ActionListener
{
  Generator geny;
  JButton startscan;
  JButton send;
  JProgressBar bar;
  JTable tbl;
  DefaultTableModel dtm;
  int t = 0;
  String receiver;
  EntryPoint mainframe;
  javax.swing.JScrollPane jsp;
  JTextPane usubnet;
  JTextPane uport;
  JTextPane utimeout;
  String defaultSubnet = null;
  boolean scanstatus = false;
  
  public LanScannerGui(EntryPoint tp) {
    setBackground(java.awt.SystemColor.menu);
    mainframe = tp;
    bar = new JProgressBar();
    bar.setBounds(0, 599, 607, 23);
    tbl = new JTable();
    tbl.setShowGrid(false);
    dtm = new DefaultTableModel(new String[] { "IP-Address", "Device-Name" }, 0)
    {
      public boolean isCellEditable(int row, int column)
      {
        return false;
      }
      
    };
    bar.setStringPainted(true);
    bar.setMaximum(100);
    bar.setMinimum(0);
    bar.setValue(0);
    tbl.setModel(dtm);
    tbl.setSelectionMode(0);
    jsp = new javax.swing.JScrollPane(tbl);
    jsp.setBounds(0, 192, 607, 395);
    
    tbl.getSelectionModel().addListSelectionListener(new javax.swing.event.ListSelectionListener() {
      public void valueChanged(javax.swing.event.ListSelectionEvent event) {
        receiver = tbl.getValueAt(tbl.getSelectedRow(), 0).toString();
      }
    });
    setLayout(null);
    add(jsp);
    add(bar);
    setSize(607, 644);
    
    JToolBar toolBar = new JToolBar();
    toolBar.setBounds(0, 0, 605, 32);
    add(toolBar);
    
    java.awt.Component horizontalGlue = javax.swing.Box.createHorizontalGlue();
    toolBar.add(horizontalGlue);
    send = new JButton("Send");
    toolBar.add(send);
    send.addActionListener(this);
    
    JPanel panel = new JPanel();
    panel.setBounds(0, 34, 607, 161);
    add(panel);
    panel.setLayout(null);
    

    JPanel panel_1 = new JPanel();
    panel_1.setBackground(java.awt.SystemColor.inactiveCaptionBorder);
    panel_1.setBounds(6, 37, 601, 105);
    panel.add(panel_1);
    panel_1.setLayout(null);
    
    usubnet = new JTextPane();
    usubnet.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == 10) {
          if (!scanstatus) {
            if (validxcute() == 1) {
              startscan.setText("Stop Scan");
              scanstatus = true;
            }
          }
          else {
            geny.halt();scanstatus = false;startscan.setText("Start Scan");
          }
          
        }
        
      }
    });
    usubnet.setEnabled(false);
    usubnet.setBounds(108, 11, 112, 20);
    panel_1.add(usubnet);
    
    uport = new JTextPane();
    uport.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == 10) {
          if (!scanstatus) {
            if (validxcute() == 1) {
              startscan.setText("Stop Scan");
              scanstatus = true;
            }
          }
          else {
            geny.halt();scanstatus = false;startscan.setText("Start Scan");
          }
          
        }
        
      }
    });
    uport.setEnabled(false);
    uport.setBounds(108, 42, 65, 20);
    panel_1.add(uport);
    
    JLabel lblSubnetMask = new JLabel("Subnet Mask ");
    lblSubnetMask.setBounds(10, 11, 88, 20);
    panel_1.add(lblSubnetMask);
    
    JLabel lblTimeout = new JLabel("Timeout (ms) ");
    lblTimeout.setToolTipText("Time to wait for a response");
    lblTimeout.setBounds(10, 80, 88, 14);
    panel_1.add(lblTimeout);
    
    JLabel lblPortNo = new JLabel("Port No          ");
    lblPortNo.setToolTipText("This app uses port 1337 for receving files. (Not for  non-techies)");
    lblPortNo.setBounds(10, 48, 74, 14);
    panel_1.add(lblPortNo);
    
    utimeout = new JTextPane();
    utimeout.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == 10) {
          if (!scanstatus) {
            if (validxcute() == 1) {
              startscan.setText("Stop Scan");
              scanstatus = true;
            }
          }
          else {
            geny.halt();scanstatus = false;startscan.setText("Start Scan");
          }
          
        }
        
      }
    });
    utimeout.setEnabled(false);
    utimeout.setBounds(108, 74, 65, 20);
    panel_1.add(utimeout);
    startscan = new JButton("Start Scan");
    startscan.setFont(new java.awt.Font("Tahoma", 0, 11));
    startscan.setBounds(459, 73, 132, 29);
    panel_1.add(startscan);
    startscan.setToolTipText("Search for hosts running this app");
    startscan.addActionListener(this);
    
    JCheckBox chckbxDefaultScan = new JCheckBox("Default Scan");
    chckbxDefaultScan.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(ItemEvent e) { boolean val;
        boolean val;
        if (e.getStateChange() == 1) {
          setDefaultParams(null);
          val = false;
        }
        else {
          val = true;
        }
        usubnet.setEnabled(val);
        uport.setEnabled(val);
        utimeout.setEnabled(val);
      }
    });
    chckbxDefaultScan.setSelected(true);
    chckbxDefaultScan.setBounds(6, 7, 97, 23);
    panel.add(chckbxDefaultScan);
    
    JSeparator separator = new JSeparator();
    separator.setBounds(0, 154, 607, 2);
    panel.add(separator);
    try {
      geny = new Generator(this);
    }
    catch (java.net.SocketException e) {
      JOptionPane.showMessageDialog(null, "Error occured while Initializing the program");
      System.out.println("Exception in lang-ui creating generator");
    }
    catch (java.net.UnknownHostException e) {
      JOptionPane.showMessageDialog(null, "Error occured while Initializing the program");
      System.out.println("Exception in lan-gui creating generator");
    }
  }
  
  public void actionPerformed(ActionEvent evnt) {
    if (evnt.getSource() == startscan)
    {
      if (geny.counter == 0.0F) {
        bar.setString(null);
        validxcute();
        startscan.setText("Stop Scan");
      }
      else
      {
        geny.halt();
        if ((bar.getValue() == 100) || (bar.getString().endsWith("Interrupted"))) {
          bar.setString(null);
          validxcute();
          startscan.setText("Stop Scan");
        }
        else {
          startscan.setText("Restart Scan");
          bar.setString(bar.getString() + " Scan Interrupted");
        }
      }
    }
    else if (evnt.getSource() == send)
      if (receiver == null) {
        JOptionPane.showMessageDialog(null, "Please Select a peer from scan result");
      } else {
        geny.halt();
        mainframe.sendtoip(receiver);
      }
  }
  
  public void setDefaultParams(String dsnet) {
    if (dsnet != null)
      defaultSubnet = dsnet;
    usubnet.setText(defaultSubnet);
    uport.setText("1337");
    utimeout.setText("1000");
  }
  
  public void appendHost(String ip, String hostName) { dtm.addRow(new Object[] { ip, hostName }); }
  
  public void updateProgress(int a) {
    if (a > 100) a = 100;
    bar.setValue(a);
    if (a == 100) {
      scanstatus = false;
      bar.setString("Scan Completed");
      startscan.setText("Rescan For Peers");
    }
  }
  
  public int validxcute() {
    try { geny.exec(Validateip.ip(usubnet.getText().trim()), Integer.parseInt(uport.getText().trim()), Integer.parseInt(utimeout.getText().trim()));
      return 1;
    }
    catch (NumberFormatException e) {
      JOptionPane.showMessageDialog(null, "Invalid paramater " + e.getCause()); }
    return 0;
  }
}
