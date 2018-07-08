package gui;

import java.awt.Component;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class ProgressBarRenderer extends JProgressBar implements TableCellRenderer
{
  public ProgressBarRenderer()
  {
    super(0, 100);
    setStringPainted(true);
  }
  

  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
  {
    setValue(Integer.parseInt(value.toString()));
    
    if ((getValue() > 0) && (getValue() < 100)) {
      setString(null);
    } else if (getValue() == 0) {
      setString("Rejected");
    } else if (getValue() == 100) {
      setString("Transfer Successful");
    } else if (getValue() == 404) {
      setString("Transfer Failed");
    }
    return this;
  }
}
