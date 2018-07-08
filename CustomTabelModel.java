package gui;

import javax.swing.table.DefaultTableModel;

public class CustomTabelModel extends DefaultTableModel {
  public CustomTabelModel(String tabelfor) {
    super(new String[] { "File ", tabelfor + " to", "Size", "Status" }, 0);
  }
  
  public boolean isCellEditable(int row, int column) {
    if (column == 4) {
      return true;
    }
    return false;
  }
}
